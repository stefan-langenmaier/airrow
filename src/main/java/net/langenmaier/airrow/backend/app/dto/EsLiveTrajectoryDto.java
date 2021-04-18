package net.langenmaier.airrow.backend.app.dto;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.OffsetDateTimeSerializer;

import org.apache.commons.codec.digest.DigestUtils;

import net.langenmaier.airrow.backend.app.helper.OffsetDateTimeDeserializer;
import net.langenmaier.airrow.backend.app.model.Location;
import net.langenmaier.airrow.backend.app.model.Session;

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

	public static EsLiveTrajectoryDto of(Session s) {
		EsLiveTrajectoryDto esd = new EsLiveTrajectoryDto();
		
		esd.uuid = s.uuid.toString();
        esd.refCode = DigestUtils.sha256Hex(esd.uuid);

		Location location = new Location();
		location.lon = s.location.lon;
		location.lat = s.location.lat;
		esd.location = location;
		esd.status = s.status;
		esd.accuracy = s.accuracy;
		esd.updatedAt = s.updatedAt;
		return esd;
	}
}
