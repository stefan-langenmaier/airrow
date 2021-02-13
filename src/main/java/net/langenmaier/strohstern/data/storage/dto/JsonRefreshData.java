package net.langenmaier.strohstern.data.storage.dto;

import java.util.UUID;

import net.langenmaier.strohstern.data.storage.model.Location;

public class JsonRefreshData {
	public Location location;
	public String status;
	public Integer accuracy;
	public UUID uuid;
}
