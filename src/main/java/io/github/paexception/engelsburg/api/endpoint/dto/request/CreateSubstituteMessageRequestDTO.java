package io.github.paexception.engelsburg.api.endpoint.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotNull;
import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateSubstituteMessageRequestDTO {

	@NotNull
	private Date date;
	private String absenceTeachers;
	private String absenceClasses;
	private String affectedClasses;
	private String affectedRooms;
	private String blockedRooms;
	private String messages;

}
