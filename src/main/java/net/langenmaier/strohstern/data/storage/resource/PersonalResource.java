package net.langenmaier.strohstern.data.storage.resource;

import java.util.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import net.langenmaier.strohstern.data.storage.dto.JsonPersonalInformation;
import net.langenmaier.strohstern.data.storage.service.PersonalService;

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

}