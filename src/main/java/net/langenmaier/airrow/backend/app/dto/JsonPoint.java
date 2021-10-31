package net.langenmaier.airrow.backend.app.dto;

import net.langenmaier.airrow.backend.app.helper.GeoTools;
import net.langenmaier.airrow.backend.app.model.Location;
import net.langenmaier.airrow.backend.app.model.Media;

public class JsonPoint {
	public String uuid;
	public String refCode;
	public Location location;

	public String artistName;
	public String artistCountry;
	public String artistPlace;
	public String artistIntroDe;
	public String artistIntroEn;
	public String objectName;
	public String objectYear;
	public String objectPlace;
	public String objectStreet;
	public String objectIdeaDe;
	public String objectIdeaEn;
	public Media object;
	public Media preview;
	public Media background;

	public Double distance;
	
	public static JsonPoint of(EsPointDto ep, Location current) {
		JsonPoint jp = new JsonPoint();

		jp.uuid = ep.uuid;
		jp.refCode = ep.refCode;
		jp.location = ep.location;

		jp.artistName = ep.artistName;
		jp.artistCountry = ep.artistCountry;
		jp.artistPlace = ep.artistPlace;
		jp.artistIntroDe = ep.artistIntroDe;
		jp.artistIntroEn = ep.artistIntroEn;
		jp.objectName = ep.objectName;
		jp.objectYear = ep.objectYear;
		jp.objectPlace = ep.objectPlace;
		jp.objectStreet = ep.objectStreet;
		jp.objectIdeaDe = ep.objectIdeaDe;
		jp.objectIdeaEn = ep.objectIdeaEn;
		jp.object = ep.object;
		jp.preview = ep.preview;
		jp.background = ep.background;


		jp.distance = GeoTools.getDistance(ep.location, current);

		return jp;
	}
}
