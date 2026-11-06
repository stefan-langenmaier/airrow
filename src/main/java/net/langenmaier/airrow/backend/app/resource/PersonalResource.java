package net.langenmaier.airrow.backend.app.resource;

import java.util.UUID;
import java.util.logging.Logger;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.CookieParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import net.langenmaier.airrow.backend.app.dto.JsonPersonal;
import net.langenmaier.airrow.backend.app.dto.JsonPersonalInformation;
import net.langenmaier.airrow.backend.app.service.PersonalService;

@Path("/personal")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PersonalResource {

	private final static Logger LOGGER = Logger.getLogger(PersonalResource.class.getName());
	
	@Inject
	PersonalService ps;
	
	@GET
	public JsonPersonalInformation get(@CookieParam("sessionId") String sessionId) {
		if (sessionId == null) {
			return null;
		}

		JsonPersonalInformation ns = ps.get(sessionId);
		LOGGER.info("Personal info retrieved: " + sessionId);

		return ns;
	}

	@POST
	public JsonPersonalInformation post(JsonPersonal personal) {
		if (personal == null) {
			return null;
		}
		UUID sessionId = personal.uuid;

		JsonPersonalInformation ns = ps.get(sessionId.toString());
		LOGGER.info("Personal info retrieved: " + sessionId);

		return ns;
	}

}
