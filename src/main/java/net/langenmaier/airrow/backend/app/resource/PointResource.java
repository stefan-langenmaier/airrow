package net.langenmaier.airrow.backend.app.resource;

import java.util.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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