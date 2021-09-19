package net.langenmaier.airrow.backend.app.dto;

import net.langenmaier.airrow.backend.app.helper.GeoTools;
import net.langenmaier.airrow.backend.app.model.Location;

public class JsonPoint {
	public String uuid;
	public String refCode;
	public String status;
	public Location location;
	public Double distance;
	public String mimeType;
	
	public static JsonPoint of(EsPointDto ep, Location current) {
		JsonPoint jp = new JsonPoint();

		jp.uuid = ep.uuid;
		jp.refCode = ep.refCode;
		jp.status = ep.status;
		jp.mimeType = ep.mimeType;
		jp.location = ep.location;
		jp.distance = GeoTools.getDistance(ep.location, current);

		return jp;
	}
}
