package net.langenmaier.strohstern.data.storage.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import net.langenmaier.strohstern.data.storage.model.Location;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EsTarget {
	
	public String status;
	public String refCode;
	public String mimeType;
	public String fileHash;
	public Location location;

}
