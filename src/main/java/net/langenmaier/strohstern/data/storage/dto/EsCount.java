package net.langenmaier.strohstern.data.storage.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EsCount {
	public Long count;
}
