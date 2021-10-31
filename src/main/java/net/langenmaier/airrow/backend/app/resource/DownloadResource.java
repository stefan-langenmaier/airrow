package net.langenmaier.airrow.backend.app.resource;

import java.util.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.jboss.resteasy.annotations.jaxrs.PathParam;

import net.langenmaier.airrow.backend.app.model.Download;
import net.langenmaier.airrow.backend.app.service.DownloadService;


@Path("/download/")
public class DownloadResource {

	private final static Logger LOGGER = Logger.getLogger(DownloadResource.class.getName());
	
	@Inject
	DownloadService service;
	
	@GET
	@Path("/{fileHash}")
	public Response upload(@PathParam String fileHash) {
		LOGGER.info("download started");
		Download download = service.get(fileHash, "fileHash");
		ResponseBuilder response = Response.ok(download.file, download.mediaType).header("Content-Disposition", "attachment; filename=\"" + download.fileName + "\"");
		return response.build();
	}

	@GET
	@Path("/preview/{fileHash}")
	public Response downloadPreview(@PathParam String fileHash) {
		LOGGER.info("download started");
		Download download = service.getPreview(fileHash);
		ResponseBuilder response = Response.ok(download.file, download.mediaType).header("Content-Disposition", "attachment; filename=\"" + download.fileName + "\"");
		return response.build();
	}

	@GET
	@Path("/object/{fileHash}")
	public Response downloadObject(@PathParam String fileHash) {
		LOGGER.info("download started");
		Download download = service.getObject(fileHash);
		ResponseBuilder response = Response.ok(download.file, download.mediaType).header("Content-Disposition", "attachment; filename=\"" + download.fileName + "\"");
		return response.build();
	}

	@GET
	@Path("/background/{fileHash}")
	public Response downloadBackground(@PathParam String fileHash) {
		LOGGER.info("download started");
		Download download = service.getBackground(fileHash);
		ResponseBuilder response = Response.ok(download.file, download.mediaType).header("Content-Disposition", "attachment; filename=\"" + download.fileName + "\"");
		return response.build();
	}

}