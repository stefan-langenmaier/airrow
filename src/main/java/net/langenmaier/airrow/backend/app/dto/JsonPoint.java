package net.langenmaier.airrow.backend.app.dto;

import net.langenmaier.airrow.backend.app.helper.GeoTools;
import net.langenmaier.airrow.backend.app.model.Location;

public class JsonPoint {
	public String uuid;
	public String status;
	public Double distance;
	public String mimeType;
	
	public static JsonPoint of(EsPointDto ep, Location current) {
		JsonPoint jp = new JsonPoint();

		jp.uuid = ep.uuid;
		jp.status = ep.status;
		jp.mimeType = ep.mimeType;
		jp.distance = GeoTools.getDistance(ep.location, current);

		return jp;
	}
}
