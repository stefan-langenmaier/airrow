package net.langenmaier.strohstern.data.storage.model;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

import net.langenmaier.strohstern.data.storage.dto.JsonRateData;
import net.langenmaier.strohstern.data.storage.enumeration.RatingState;
import net.langenmaier.strohstern.data.storage.helper.EmojiUtils;

public class Rating {

	public UUID creator;
	public UUID entity;
	
	public String status;
	public RatingState rating;

	public OffsetDateTime updatedAt;

	public static Rating of(JsonRateData rd, UUID entity) {
		Rating r = new Rating();
		r.creator = rd.uuid;
		r.entity = entity;

		r.status = EmojiUtils.stripNonEmojis(rd.status);
		r.rating = rd.rating;

		ZoneOffset zoneOffSet= ZoneOffset.of("+00:00");
		r.updatedAt = OffsetDateTime.now(zoneOffSet);

		return r;
	}

	public String toString() {
		return "{" + creator.toString() + ", " + status + ", " + rating + "}";
	}
}
