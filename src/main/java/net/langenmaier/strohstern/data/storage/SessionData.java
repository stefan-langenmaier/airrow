package net.langenmaier.strohstern.data.storage;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.OffsetDateTimeSerializer;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SessionData {

	public String uuid;
	
	public Location location;
	public String status;
	public Integer accuracy;

	@JsonDeserialize(using = OffsetDateTimeDeserializer.class)
	@JsonSerialize(using = OffsetDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd'T'HHmmss.SSSZ")
	public OffsetDateTime updatedAt;

	public static SessionData of(UpdateData ud) {
		SessionData sd = new SessionData();
		Location location = new Location();
		location.lon = ud.longitude;
		location.lat = ud.latitude;
		sd.location = location;

		sd.status = EmojiUtils.stripNonEmojis(ud.status);
		sd.accuracy = ud.accuracy;
		ZoneOffset zoneOffSet= ZoneOffset.of("+00:00");
		sd.updatedAt = OffsetDateTime.now(zoneOffSet);
		return sd;
	}
}
