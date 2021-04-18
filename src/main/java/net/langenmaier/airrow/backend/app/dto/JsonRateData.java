package net.langenmaier.airrow.backend.app.dto;

import java.util.UUID;

import net.langenmaier.airrow.backend.app.enumeration.RatingState;
import net.langenmaier.airrow.backend.app.model.Location;

public class JsonRateData {
	public Location location;
	public String status;
	public RatingState rating;
	public UUID uuid;
	public String refCode;
}
