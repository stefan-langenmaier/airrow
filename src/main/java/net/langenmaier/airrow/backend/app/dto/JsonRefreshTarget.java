package net.langenmaier.airrow.backend.app.dto;

import java.util.UUID;

import net.langenmaier.airrow.backend.app.model.Location;

public class JsonRefreshTarget {
	public UUID uuid;
	public String refCode;
	public Location location;
}
