package io.github.paexception.engelsburginfrastructure.endpoint.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetSubstituteMessagesResponseDTO {

	@NotNull
	private List<SubstituteMessageResponseDTO> substituteMessages;

}
