package net.langenmaier.airrow.backend.app.dto;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.OffsetDateTimeSerializer;

import net.langenmaier.airrow.backend.app.helper.OffsetDateTimeDeserializer;
import net.langenmaier.airrow.backend.app.model.Location;
import net.langenmaier.airrow.backend.app.model.Session;

public class EsTrajectoryDto {

	public String uuid;
	public Location location;

	@JsonDeserialize(using = OffsetDateTimeDeserializer.class)
	@JsonSerialize(using = OffsetDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd'T'HHmmss.SSSZ")
	public OffsetDateTime updatedAt;

	public static EsTrajectoryDto of(Session s) {
		EsTrajectoryDto etd = new EsTrajectoryDto();
		etd.uuid = s.uuid.toString();
		Location location = new Location();
		location.lon = s.location.lon;
		location.lat = s.location.lat;
		etd.location = location;
		etd.updatedAt = s.updatedAt;
		return etd;
	}
}
