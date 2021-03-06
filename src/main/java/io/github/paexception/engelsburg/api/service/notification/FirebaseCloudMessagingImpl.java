package io.github.paexception.engelsburg.api.service.notification;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import io.github.paexception.engelsburg.api.endpoint.dto.NotificationDTO;
import io.github.paexception.engelsburg.api.util.Environment;
import io.github.paexception.engelsburg.api.util.LoggingComponent;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@Component
public class FirebaseCloudMessagingImpl extends LoggingComponent {

	private final ObjectMapper objectMapper = new ObjectMapper();

	public FirebaseCloudMessagingImpl() {
		super(FirebaseCloudMessagingImpl.class);
	}

	/**
	 * Initiates the firebase app to send notifications.
	 */
	@Bean
	public void init() {
		try {
			FileInputStream serviceAccount = new FileInputStream(Environment.GOOGLE_ACCOUNT_CREDENTIALS);

			FirebaseOptions options = FirebaseOptions.builder()
					.setCredentials(GoogleCredentials.fromStream(serviceAccount))
					.build();

			FirebaseApp.initializeApp(options);
		} catch (IOException e) {
			this.logError("Couldn't initialize firebase cloud messaging", e);
		}

	}

	/**
	 * Sends notification to topics.
	 *
	 * @param type    of notification
	 * @param payload with information
	 * @param topics  to send to
	 */
	public void sendNotificationToTopics(String type, Object payload, String... topics) {
		try {
			Message.Builder messageBuilder = Message.builder()
					.putData("type", type)
					.putData("data", this.objectMapper.writeValueAsString(payload));

			for (String topic : topics)
				if (topic.matches("[A-ZÄÖÜa-zäöü]"))
					FirebaseMessaging.getInstance().send(messageBuilder.setTopic(topic
							.replace("Ä", "AE")
							.replace("Ö", "OE")
							.replace("Ü", "UE")
							.replace("ä", "ae")
							.replace("ö", "oe")
							.replace("ü", "ue")
							.toLowerCase()).build(), !Environment.PRODUCTION);
		} catch (JsonProcessingException | FirebaseMessagingException e) {
			this.logError("Couldn't send notification", e);
		}
	}

	/**
	 * Send advanced notifications to many.
	 *
	 * @param type of notification
	 * @param dtos with notification tokens and info
	 */
	public void sendAdvancedNotifications(String type, List<NotificationDTO> dtos) {
		dtos.forEach(dto -> {
			try {
				if (dto.getTokens().size() > 0) {
					MulticastMessage multicastMessage = MulticastMessage.builder()
							.addAllTokens(dto.getTokens())
							.putData("type", type)
							.putData("data", this.objectMapper.writeValueAsString(dto.getDto()))
							.build();
					FirebaseMessaging.getInstance().sendMulticast(multicastMessage);
				}
			} catch (JsonProcessingException | FirebaseMessagingException e) {
				this.logError("Couldn't send notification", e);
			}
		});
	}

}
