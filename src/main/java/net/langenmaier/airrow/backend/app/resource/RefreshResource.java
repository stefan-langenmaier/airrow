package net.langenmaier.airrow.backend.app.resource;

import java.util.logging.Logger;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import net.langenmaier.airrow.backend.app.dto.JsonNavigationState;
import net.langenmaier.airrow.backend.app.dto.JsonPoint;
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
	public JsonPoint getTarget(JsonRefreshTarget rt) {
		if (rt == null) {
			return null;
		}

		LOGGER.info("Get Target: " + rt.refCode);
		JsonPoint p = rs.getTarget(rt);

		return p;
	}

}