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
import net.langenmaier.airrow.backend.app.model.Upload;

public class EsPointDto {
    @JsonIgnore
    public String uuid;
    public String refCode;

    public String creator;
	public Location location;
    public String status;
    public Integer accuracy;
    public Boolean permanent = true;

    public String mimeType;
	public String fileName;
	public String fileHash;

	@JsonDeserialize(using = OffsetDateTimeDeserializer.class)
	@JsonSerialize(using = OffsetDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd'T'HHmmss.SSSZ")
	public OffsetDateTime updatedAt;

	public static EsPointDto of(Upload upload) {
        EsPointDto eed = new EsPointDto();

        eed.uuid = upload.uuid.toString();
        eed.refCode = DigestUtils.sha256Hex(eed.uuid);
        
		Location location = new Location();
		location.lon = upload.location.lon;
		location.lat = upload.location.lat;
        eed.location = location;
        
        eed.creator = upload.creator.toString();
        eed.status = upload.status;
        eed.accuracy = upload.accuracy;
        eed.fileName = upload.fileName;
        eed.fileHash = upload.fileHash;
        eed.mimeType = upload.mimeType;
        
        eed.updatedAt = upload.updatedAt;
        
		return eed;
	}
}
