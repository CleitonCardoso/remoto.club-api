package com.remototech.remototechapi.entities;

import lombok.Getter;

@Getter
public enum CandidatureStatus {

	APPLIED("Aplicado"),
	REJECTED("Rejeitado"),
	ACCEPTED("Aceito"),
	OFFERED("Oferta feita"),
	HIRED("Contratado");

	private String description;

	private CandidatureStatus(String description) {
		this.description = description;
	}
}
