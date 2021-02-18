package com.remototech.remototechapi.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CandidateDTO {

	private UUID uuid;
	private String name;
	private String candidatureStatus;
	private String linkedInUrl;
	@JsonFormat(pattern = "dd/MM/yyyy")
	private LocalDateTime dateApplied;

}
