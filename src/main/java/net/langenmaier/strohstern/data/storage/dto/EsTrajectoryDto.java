package net.langenmaier.strohstern.data.storage.dto;

import java.time.OffsetDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.OffsetDateTimeSerializer;

import net.langenmaier.strohstern.data.storage.Location;
import net.langenmaier.strohstern.data.storage.OffsetDateTimeDeserializer;
import net.langenmaier.strohstern.data.storage.SessionData;

public class EsTrajectoryDto {

	public String uuid;
	public Location location;
	public String status;

	@JsonDeserialize(using = OffsetDateTimeDeserializer.class)
	@JsonSerialize(using = OffsetDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd'T'HHmmss.SSSZ")
	public OffsetDateTime updatedAt;

	public static EsTrajectoryDto of(SessionData sd) {
		EsTrajectoryDto etd = new EsTrajectoryDto();
		etd.uuid = sd.uuid.toString();
		Location location = new Location();
		location.lon = sd.location.lon;
		location.lat = sd.location.lat;
		etd.location = location;
		etd.status = sd.status;
		etd.updatedAt = sd.updatedAt;
		return etd;
	}
}
