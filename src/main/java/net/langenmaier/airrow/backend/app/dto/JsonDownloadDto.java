package net.langenmaier.airrow.backend.app.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonDownloadDto {
    public String fileHash;
    public String mimeType;
    public String fileName;
}
