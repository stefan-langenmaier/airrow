package net.langenmaier.airrow.backend.app.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EsPersonalInformation {
	public String refCode;
}
