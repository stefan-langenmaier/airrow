package net.langenmaier.airrow.backend.app.dto;

import java.io.File;

import javax.ws.rs.FormParam;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.annotations.providers.multipart.PartType;

public class MultipartUpload {
    @FormParam("objectMedia")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    public File objectMedia;

    @FormParam("previewMedia")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    public File previewMedia;

    @FormParam("backgroundMedia")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    public File backgroundMedia;

    @FormParam("meta")
    @PartType(MediaType.APPLICATION_JSON)
    public JsonUploadMetaData umd;
}
