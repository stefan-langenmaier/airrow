package net.langenmaier.airrow.backend.app.dto;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.OffsetDateTimeSerializer;

import net.langenmaier.airrow.backend.app.helper.OffsetDateTimeDeserializer;
import net.langenmaier.airrow.backend.app.model.Rating;
import net.langenmaier.airrow.backend.app.model.Relation;

public class EsLiveRatingDto {
	public String creator;

	public String status;
	public String rating;

	public Relation relation = new Relation();

	@JsonDeserialize(using = OffsetDateTimeDeserializer.class)
	@JsonSerialize(using = OffsetDateTimeSerializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd'T'HHmmss.SSSZ")
	public OffsetDateTime updatedAt;

	public static EsLiveRatingDto of(Rating r) {
		EsLiveRatingDto erd = new EsLiveRatingDto();
		erd.relation.name = "rating";
		erd.relation.parent = r.entity.toString();

		erd.creator = r.creator.toString();

		erd.status = r.status;
		erd.rating = r.rating.toString();
		
		erd.updatedAt = r.updatedAt;
		return erd;
	}
}
