package io.github.paexception.engelsburg.api.endpoint.reserved;

import com.auth0.jwt.interfaces.DecodedJWT;
import io.github.paexception.engelsburg.api.controller.reserved.GradeController;
import io.github.paexception.engelsburg.api.endpoint.dto.request.CreateGradeRequestDTO;
import io.github.paexception.engelsburg.api.endpoint.dto.request.UpdateGradeRequestDTO;
import io.github.paexception.engelsburg.api.spring.auth.AuthScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;

/**
 * RestController for grade actions.
 */
@RestController
public class GradeEndpoint {

	@Autowired
	private GradeController gradeController;

	/**
	 * Create a new grade.
	 *
	 * @see GradeController#createGrade(CreateGradeRequestDTO, DecodedJWT)
	 */
	@AuthScope("grade.write.self")
	@PostMapping("/grade")
	public Object createGrade(@RequestBody @Valid CreateGradeRequestDTO dto, DecodedJWT jwt) {
		return this.gradeController.createGrade(dto, jwt).getHttpResponse();
	}

	/**
	 * Update a grade.
	 *
	 * @see GradeController#updateGrade(UpdateGradeRequestDTO, DecodedJWT)
	 */
	@AuthScope("grade.write.self")
	@PatchMapping("/grade")
	public Object updateGrade(@RequestBody @Valid UpdateGradeRequestDTO dto, DecodedJWT jwt) {
		return this.gradeController.updateGrade(dto, jwt).getHttpResponse();
	}

	/**
	 * Get all grades or by subject.
	 *
	 * @see GradeController#getGrades(String, DecodedJWT)
	 */
	@AuthScope("grade.read.self")
	@GetMapping("/grade")
	public Object getGrade(@RequestParam(value = "subject", required = false) String subject, DecodedJWT jwt) {
		return this.gradeController.getGrades(subject, jwt).getHttpResponse();
	}

	/**
	 * Delete a grade by gradeId.
	 *
	 * @see GradeController#deleteGrade(int, DecodedJWT)
	 */
	@AuthScope("grade.delete.self")
	@DeleteMapping("/grade/{gradeId}")
	public Object deleteGrade(@PathVariable int gradeId, DecodedJWT jwt) {
		return this.gradeController.deleteGrade(gradeId, jwt).getHttpResponse();
	}

}
