package io.github.paexception.engelsburg.api.authentication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequestDTO {

	@Email
	@NotBlank
	private String email;
	@NotBlank
	private String password;
	private boolean teacher;
	private String className;
	private String teacherAbbreviation;

}