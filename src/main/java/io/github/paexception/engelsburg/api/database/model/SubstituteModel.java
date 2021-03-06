package io.github.paexception.engelsburg.api.database.model;

import io.github.paexception.engelsburg.api.endpoint.dto.SubstituteDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class SubstituteModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int substituteId;

	@NotNull
	private Date date;
	@NotBlank
	private String className;
	private int lesson;
	private String subject;
	@NotBlank
	private String substituteTeacher;
	@NotBlank
	private String teacher;
	@NotBlank
	private String type;
	private String substituteOf;
	private String room;
	private String text;

	public SubstituteDTO toResponseDTO() {
		return new SubstituteDTO(
				this.date,
				this.className,
				this.lesson,
				this.subject,
				this.substituteTeacher,
				this.teacher,
				this.type,
				this.substituteOf,
				this.room,
				this.text
		);
	}

}
