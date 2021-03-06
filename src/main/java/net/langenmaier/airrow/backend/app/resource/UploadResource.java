package net.langenmaier.airrow.backend.app.resource;

import java.util.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

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