package net.langenmaier.airrow.backend.app.resource;

import java.util.logging.Logger;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import net.langenmaier.airrow.backend.app.dto.MultipartUpload;
import net.langenmaier.airrow.backend.app.model.Capability;
import net.langenmaier.airrow.backend.app.model.Upload;
import net.langenmaier.airrow.backend.app.service.CapabilityService;
import net.langenmaier.airrow.backend.app.service.UploadService;

@Path("/upload")
@Consumes(MediaType.MULTIPART_FORM_DATA)
public class UploadResource {
	
	private final static Logger LOGGER = Logger.getLogger(UploadResource.class.getName());
	
	@Inject
	UploadService service;

	@Inject
	CapabilityService cs;
	
	@POST
	public Response upload(@MultipartForm MultipartUpload data) {
		LOGGER.info("upload received");
		Capability cap = cs.updateCapability(data.umd.creator);
		if (!cap.canUpload) {
			LOGGER.info("upload forbidden");
			return Response.status(Status.FORBIDDEN).build();
		}
		Upload upload = Upload.of(data);
		LOGGER.info("upload stored on disk");
		service.register(upload);
		LOGGER.info("upload indexed");
		return Response.status(Status.OK).build();
	}

}