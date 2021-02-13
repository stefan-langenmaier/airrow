package net.langenmaier.strohstern.data.storage.resource;

import java.util.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.jboss.resteasy.annotations.jaxrs.PathParam;

import net.langenmaier.strohstern.data.storage.model.Download;
import net.langenmaier.strohstern.data.storage.service.DownloadService;


@Path("/download/")
public class DownloadResource {

	private final static Logger LOGGER = Logger.getLogger(DownloadResource.class.getName());
	
	@Inject
	DownloadService service;
	
	@GET
	@Path("/{fileHash}")
	public Response upload(@PathParam String fileHash) {
		LOGGER.info("download received");
		Download download = service.get(fileHash);
		LOGGER.info("upload stored on disk");
		ResponseBuilder response = Response.ok(download.file, download.mediaType);
		return response.build();
	}

}