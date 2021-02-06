package net.langenmaier.strohstern.data.storage.dto;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.OffsetDateTimeSerializer;

import net.langenmaier.strohstern.data.storage.Location;
import net.langenmaier.strohstern.data.storage.OffsetDateTimeDeserializer;
import net.langenmaier.strohstern.data.storage.model.Upload;

public class EsEntityDto {
    public String creator;
	public Location location;
    public String status;
    public Integer accuracy;
	public String fileName;
	public String fileHash;

	@JsonDeserialize(using = OffsetDateTimeDeserializer.class)
	@JsonSerialize(using = OffsetDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd'T'HHmmss.SSSZ")
	public OffsetDateTime updatedAt;

	public static EsEntityDto of(Upload upload) {
        EsEntityDto eed = new EsEntityDto();
        
		Location location = new Location();
		location.lon = upload.location.lon;
		location.lat = upload.location.lat;
        eed.location = location;
        
        eed.creator = upload.creator.toString();
        eed.status = upload.status;
        eed.accuracy = upload.accuracy;
        eed.fileName = upload.status;
        eed.fileHash = upload.status;
        
        eed.updatedAt = upload.updatedAt;
        
		return eed;
	}
}
