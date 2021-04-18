package net.langenmaier.airrow.backend.app.dto;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.OffsetDateTimeSerializer;

import net.langenmaier.airrow.backend.app.helper.OffsetDateTimeDeserializer;
import net.langenmaier.airrow.backend.app.model.Capability;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EsCapabilityDto {
    public Boolean canUpload;
    
	// https://quarkusio.zulipchat.com/#narrow/stream/187030-users/topic/vert.2Ex.20Json.20does.20not.20share.20the.20jackson.20objectmapper.20from/near/233819035
	// once Quarkus 2.x is released the ObjectMapper is shared between vertx and quarkus
    @JsonDeserialize(using = OffsetDateTimeDeserializer.class)
	@JsonSerialize(using = OffsetDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd'T'HHmmss.SSSZ")
	public OffsetDateTime updatedAt;

    public static EsCapabilityDto of(Capability c) {
		EsCapabilityDto ecd = new EsCapabilityDto();
		ecd.canUpload = c.canUpload;
		ecd.updatedAt = c.updatedAt;
        
		return ecd;
	}

}
