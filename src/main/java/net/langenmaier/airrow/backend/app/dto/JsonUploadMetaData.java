package net.langenmaier.airrow.backend.app.dto;

import java.util.UUID;

import net.langenmaier.airrow.backend.app.model.Location;


public class JsonUploadMetaData {
    public UUID creator;
	public Location location;
	public String status;
    public Integer accuracy;
    public String fileName;
}
