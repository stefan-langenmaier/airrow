package net.langenmaier.strohstern.data.storage.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonDownloadDto {
    public String fileHash;
    public String mimeType;
}
