package net.langenmaier.airrow.backend.app.resource;

import java.util.UUID;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import net.langenmaier.airrow.backend.app.dto.JsonDeleteAccountData;
import net.langenmaier.airrow.backend.app.service.DeleteAccountService;

@Path("/delete-account")
@Consumes(MediaType.APPLICATION_JSON)
public class DeleteAccountResource {

	private final static Logger LOGGER = Logger.getLogger(DeleteAccountResource.class.getName());
	
	@Inject
	DeleteAccountService das;
	
	@POST
	public Response rate(JsonDeleteAccountData dad) {
		if (dad == null) {
			return null;
		}
		UUID sessionId = dad.uuid;

		das.delete(sessionId);
		LOGGER.info("Account deleted: " + sessionId.toString());

		return Response.ok().build();
	}

}