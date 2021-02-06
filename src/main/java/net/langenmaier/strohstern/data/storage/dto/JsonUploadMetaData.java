package net.langenmaier.strohstern.data.storage.dto;

import java.util.UUID;

import net.langenmaier.strohstern.data.storage.Location;

public class JsonUploadMetaData {
    public UUID creator;
	public Location location;
	public String status;
    public Integer accuracy;
    public String fileName;
}
