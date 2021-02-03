package net.langenmaier.strohstern.data.storage;

import java.util.UUID;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

@Path("/point")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PointResource {
	
	private final static Logger LOGGER = Logger.getLogger(PointResource.class.getName());
	
	@Inject
	TargetService ts;
	
	@ConfigProperty(name = "airrow.search.minAccuracy")
	Integer minAccuracy;
	
	@POST
	@Path("/{sessionId}")
	public Direction create(@PathParam UUID sessionId, UpdateData ud) {
		if (ud ==  null) {
			throw new InvalidTrajectoryPoint();
		}
		SessionData sd = SessionData.of(ud);
		sd.uuid = sessionId.toString();
		ts.updateSession(sd);
		LOGGER.info("location persisted");
		
		if (sd.accuracy < minAccuracy) {
			Direction d = findDirection(sd);
			return d;
		} else {
			// only return a direction when accurate
			return null;
		}
	}

	private Direction findDirection(SessionData sd) {
		Target t = ts.findTarget(sd);
		Direction d = ts.getDirection(sd, t);
		return d;
	}

}