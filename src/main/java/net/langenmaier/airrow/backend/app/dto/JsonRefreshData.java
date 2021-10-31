package net.langenmaier.airrow.backend.app.dto;

import java.util.UUID;

import net.langenmaier.airrow.backend.app.model.Location;

public class JsonRefreshData {
	public Location location;
	public String targetRefCode;
	public Integer accuracy;
	public UUID uuid;
}
