package net.langenmaier.strohstern.data.storage.dto;

import net.langenmaier.strohstern.data.storage.helper.GeoTools;
import net.langenmaier.strohstern.data.storage.model.Location;

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
