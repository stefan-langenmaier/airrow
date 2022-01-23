package net.langenmaier.airrow.backend.app.model;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

import net.langenmaier.airrow.backend.app.dto.JsonRefreshData;

public class Session {

	public UUID uuid;
	
	public Location location;
	public String targetRefCode;
	public Integer accuracy;

	public OffsetDateTime updatedAt;

	public static Session of(JsonRefreshData ud) {
		Session s = new Session();
		s.uuid = ud.uuid;
		
		s.location = ud.location;
		s.targetRefCode = ud.targetRefCode;
		s.accuracy = ud.accuracy;

		ZoneOffset zoneOffSet= ZoneOffset.of("+00:00");
		s.updatedAt = OffsetDateTime.now(zoneOffSet);

		return s;
	}

	public String toString() {
		return "{" + uuid.toString() + ", " + location.toString() + ", " + targetRefCode + ", " + accuracy + "}";
	}
}
