package net.langenmaier.airrow.backend.app.dto;

import java.io.File;

import javax.ws.rs.FormParam;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.annotations.providers.multipart.PartType;

public class MultipartUpload {
    @FormParam("file")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    public File file;

    @FormParam("meta")
    @PartType(MediaType.APPLICATION_JSON)
    public JsonUploadMetaData umd;
}
