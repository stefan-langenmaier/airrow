package net.langenmaier.strohstern.data.storage.dto;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.OffsetDateTimeSerializer;

import net.langenmaier.strohstern.data.storage.helper.OffsetDateTimeDeserializer;
import net.langenmaier.strohstern.data.storage.model.Capability;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EsCapabilityDto {
    public Boolean canUpload;
    
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
