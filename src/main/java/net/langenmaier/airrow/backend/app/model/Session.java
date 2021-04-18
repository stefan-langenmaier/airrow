package net.langenmaier.airrow.backend.app.model;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

import net.langenmaier.airrow.backend.app.dto.JsonRefreshData;
import net.langenmaier.airrow.backend.app.helper.EmojiUtils;

public class Session {

	public UUID uuid;
	
	public Location location;
	public String status;
	public Integer accuracy;

	public OffsetDateTime updatedAt;

	public static Session of(JsonRefreshData ud) {
		Session s = new Session();
		s.uuid = ud.uuid;
		
		s.location = ud.location;
		s.status = EmojiUtils.stripNonEmojis(ud.status);
		s.accuracy = ud.accuracy;

		ZoneOffset zoneOffSet= ZoneOffset.of("+00:00");
		s.updatedAt = OffsetDateTime.now(zoneOffSet);

		return s;
	}

	public String toString() {
		return "{" + uuid.toString() + ", " + location.toString() + ", " + status + ", " + accuracy + "}";
	}
}
