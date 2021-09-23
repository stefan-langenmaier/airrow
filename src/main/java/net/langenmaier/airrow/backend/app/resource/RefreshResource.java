package net.langenmaier.airrow.backend.app.resource;

import java.util.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import net.langenmaier.airrow.backend.app.dto.JsonNavigationState;
import net.langenmaier.airrow.backend.app.dto.JsonRefreshData;
import net.langenmaier.airrow.backend.app.dto.JsonRefreshTarget;
import net.langenmaier.airrow.backend.app.model.Session;
import net.langenmaier.airrow.backend.app.model.Target;
import net.langenmaier.airrow.backend.app.service.RefreshService;

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

	@POST
	@Path("/target")
	public Target getTarget(JsonRefreshTarget rt) {
		if (rt == null) {
			return null;
		}

		LOGGER.info("Get Target: " + rt.refCode);
		Target t = rs.getTarget(rt);

		return t;
	}

}