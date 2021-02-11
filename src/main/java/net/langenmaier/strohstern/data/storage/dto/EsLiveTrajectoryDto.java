package net.langenmaier.strohstern.data.storage.dto;

import java.time.OffsetDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.OffsetDateTimeSerializer;

import org.apache.commons.codec.digest.DigestUtils;

import net.langenmaier.strohstern.data.storage.Location;
import net.langenmaier.strohstern.data.storage.OffsetDateTimeDeserializer;
import net.langenmaier.strohstern.data.storage.SessionData;

public class EsLiveTrajectoryDto {
	@JsonIgnore
    public String uuid;
    public String refCode;
	
	public Location location;
	public String status;
	public Integer accuracy;

	public String relation = "entity";

	@JsonDeserialize(using = OffsetDateTimeDeserializer.class)
	@JsonSerialize(using = OffsetDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd'T'HHmmss.SSSZ")
	public OffsetDateTime updatedAt;

	public static EsLiveTrajectoryDto of(SessionData sd) {
		EsLiveTrajectoryDto esd = new EsLiveTrajectoryDto();
		
		esd.uuid = sd.uuid.toString();
        esd.refCode = DigestUtils.sha256Hex(esd.uuid);

		Location location = new Location();
		location.lon = sd.location.lon;
		location.lat = sd.location.lat;
		esd.location = location;
		esd.status = sd.status;
		esd.accuracy = sd.accuracy;
		esd.updatedAt = sd.updatedAt;
		return esd;
	}
}
