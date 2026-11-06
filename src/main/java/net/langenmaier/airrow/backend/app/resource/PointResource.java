package net.langenmaier.airrow.backend.app.resource;

import java.util.logging.Logger;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import net.langenmaier.airrow.backend.app.dto.JsonPointId;
import net.langenmaier.airrow.backend.app.dto.JsonPointsInformation;
import net.langenmaier.airrow.backend.app.dto.JsonPointsRequest;
import net.langenmaier.airrow.backend.app.service.PointService;

@Path("/points")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PointResource {

	private final static Logger LOGGER = Logger.getLogger(PointResource.class.getName());
	
	@Inject
	PointService ps;
	
	@POST
	@Path("/list")
	public JsonPointsInformation list(JsonPointsRequest pr) {
		if (pr == null) {
			return null;
		}

		JsonPointsInformation pi = ps.list(pr);
		LOGGER.info("Points info retrieved: " + pr.uuid);

		return pi;
	}

	@POST
	@Path("/list/public")
	public JsonPointsInformation listPublic(JsonPointsRequest pr) {
		if (pr == null) {
			return null;
		}

		JsonPointsInformation pi = ps.listPublic(pr);
		LOGGER.info("Points info retrieved: " + pr.uuid);

		return pi;
	}

	@POST
	@Path("/delete")
	public Response list(JsonPointId point) {
		if (point == null) {
			return null;
		}

		ps.delete(point);
		LOGGER.info("Point delete: " + point.uuid);

		return Response.ok().build();
	}

}