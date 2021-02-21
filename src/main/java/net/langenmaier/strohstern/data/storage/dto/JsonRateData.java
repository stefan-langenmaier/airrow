package net.langenmaier.strohstern.data.storage.dto;

import java.util.UUID;

import net.langenmaier.strohstern.data.storage.enumeration.RatingState;
import net.langenmaier.strohstern.data.storage.model.Location;

public class JsonRateData {
	public Location location;
	public String status;
	public RatingState rating;
	public UUID uuid;
	public String refCode;
}
