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
import net.langenmaier.airrow.backend.app.model.Media;
import net.langenmaier.airrow.backend.app.model.Upload;

public class EsPointDto {
    @JsonIgnore
    public String uuid;
    public String refCode;

    public String creator;
    public Location location;
    public Integer accuracy;
    public Boolean permanent = true;
    public String artistName;
    public String artistCountry;
    public String artistPlace;
    public String artistIntroDe;
    public String artistIntroEn;
    public String objectName;
    public String objectYear;
    public String objectPlace;
    public String objectStreet;
    public String objectIdeaDe;
    public String objectIdeaEn;
    public Media object;
    public Media preview;
    public Media background;

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
        eed.accuracy = upload.accuracy;

        eed.artistName = upload.artistName;
        eed.artistCountry = upload.artistCountry;
        eed.artistPlace = upload.artistPlace;
        eed.artistIntroDe = upload.artistIntroDe;
        eed.artistIntroEn = upload.artistIntroEn;
        eed.objectName = upload.objectName;
        eed.objectYear = upload.objectYear;
        eed.objectPlace = upload.objectPlace;
        eed.objectStreet = upload.objectStreet;
        eed.objectIdeaDe = upload.objectIdeaDe;
        eed.objectIdeaEn = upload.objectIdeaEn;
        eed.object = upload.object;
        eed.preview = upload.preview;
        eed.background = upload.background;

        eed.updatedAt = upload.updatedAt;

        return eed;
    }
}
