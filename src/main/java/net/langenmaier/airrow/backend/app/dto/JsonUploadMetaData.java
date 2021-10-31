package net.langenmaier.airrow.backend.app.dto;

import java.util.UUID;

import net.langenmaier.airrow.backend.app.model.Location;

public class JsonUploadMetaData {
	public UUID creator;
	public Location location;
	public String status;
	public Integer accuracy;
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
	public String objectMediaName;
	public String previewMediaName;
	public String backgroundMediaName;
}
