package net.langenmaier.strohstern.data.storage.resource;

import java.util.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import net.langenmaier.strohstern.data.storage.dto.JsonNavigationState;
import net.langenmaier.strohstern.data.storage.dto.JsonRefreshData;
import net.langenmaier.strohstern.data.storage.model.Session;
import net.langenmaier.strohstern.data.storage.service.RefreshService;

@Path("/refresh")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RefreshResource {
	
	private final static Logger LOGGER = Logger.getLogger(RefreshResource.class.getName());
	
	@Inject
	RefreshService rs;
	
	@POST
	public JsonNavigationState create(JsonRefreshData rd) {
		if (rd == null) {
			return null;
		}
		Session session = Session.of(rd);

		JsonNavigationState ns = rs.refresh(session);
		LOGGER.info("Session refreshed: " + session.uuid.toString());

		return ns;
	}

}