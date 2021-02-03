package net.langenmaier.strohstern.data.storage.dto;

import java.time.OffsetDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.OffsetDateTimeSerializer;

import net.langenmaier.strohstern.data.storage.Location;
import net.langenmaier.strohstern.data.storage.OffsetDateTimeDeserializer;
import net.langenmaier.strohstern.data.storage.SessionData;

public class EsSessionDto {
	public Location location;
	public String status;

	@JsonDeserialize(using = OffsetDateTimeDeserializer.class)
	@JsonSerialize(using = OffsetDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd'T'HHmmss.SSSZ")
	public OffsetDateTime updatedAt;

	public static EsSessionDto of(SessionData sd) {
		EsSessionDto esd = new EsSessionDto();
		Location location = new Location();
		location.lon = sd.location.lon;
		location.lat = sd.location.lat;
		esd.location = location;
		esd.status = sd.status;
		esd.updatedAt = sd.updatedAt;
		return esd;
	}
}
