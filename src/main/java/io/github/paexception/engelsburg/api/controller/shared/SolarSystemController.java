package io.github.paexception.engelsburg.api.controller.shared;

import io.github.paexception.engelsburg.api.endpoint.dto.SolarSystemDTO;
import io.github.paexception.engelsburg.api.util.Error;
import io.github.paexception.engelsburg.api.util.Result;
import org.springframework.stereotype.Component;

/**
 * Controller to handle all solar system actions.
 */
@Component
public class SolarSystemController {

	private SolarSystemDTO dto;

	/**
	 * Retrieve the current status of the solar system.
	 *
	 * @return the status
	 */
	public Result<SolarSystemDTO> info() {
		return this.dto != null ? Result.of(this.dto) : Result.of(Error.NOT_FOUND, "solar_system");
	}

	/**
	 * Update current status of the solar system.
	 *
	 * @param dto with status.
	 */
	public void update(SolarSystemDTO dto) {
		this.dto = dto;
	}

}
