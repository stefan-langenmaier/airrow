package net.langenmaier.airrow.backend.app.resource;

import java.util.logging.Logger;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import net.langenmaier.airrow.backend.app.dto.JsonRateData;
import net.langenmaier.airrow.backend.app.model.Entity;
import net.langenmaier.airrow.backend.app.model.Rating;
import net.langenmaier.airrow.backend.app.service.RateService;

@Path("/rate")
@Consumes(MediaType.APPLICATION_JSON)
public class RateResource {

	private final static Logger LOGGER = Logger.getLogger(RateResource.class.getName());
	
	@Inject
	RateService rs;
	
	@POST
	public Response rate(JsonRateData rd) {
		if (rd == null) {
			return null;
		}
		Entity e = rs.getEntity(rd);
		Rating rating = Rating.of(rd, e.uuid);

		rs.rate(rating);
		LOGGER.info("Rating added: " + rating.creator.toString());

		return Response.ok().build();
	}

}