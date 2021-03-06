package io.github.paexception.engelsburg.api.endpoint.reserved;

import com.auth0.jwt.interfaces.DecodedJWT;
import io.github.paexception.engelsburg.api.controller.userdata.UserDataController;
import io.github.paexception.engelsburg.api.spring.auth.AuthScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * RestController for user data actions.
 */
@Validated
@RestController
public class UserDataEndpoint {

	@Autowired
	private UserDataController userDataController;

	/**
	 * Get data of user.
	 *
	 * @see UserDataController#getUserData(DecodedJWT)
	 */
	@AuthScope("user.data.read.self")
	@GetMapping("/user/data")
	public Object getData(DecodedJWT jwt) {
		return this.userDataController.getUserData(jwt).getHttpResponse();
	}

	/**
	 * Delete all data of user.
	 *
	 * <b>The whole account will be deleted and there is no way of restoring</b>
	 *
	 * @see UserDataController#deleteUserData(DecodedJWT)
	 */
	@AuthScope("user.data.delete.self")
	@DeleteMapping("/user/data")
	public Object deleteData(DecodedJWT jwt) {
		return this.userDataController.deleteUserData(jwt).getHttpResponse();
	}

}
