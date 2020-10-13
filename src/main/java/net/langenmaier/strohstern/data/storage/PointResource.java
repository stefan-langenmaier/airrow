package net.langenmaier.strohstern.data.storage;

import java.util.UUID;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.transaction.Transactional;
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
	@Transactional
	@Path("/{sessionId}")
	public Direction create(@PathParam UUID sessionId, UpdateData ud) {
		if (ud ==  null) {
			throw new InvalidTrajectoryPoint();
		}
		Session s = Session.findByUuid(sessionId);
		if (s == null) {
			if (ud.accuracy < minAccuracy) {
				LOGGER.info("create new session");
				s = new Session();
				s.uuid = sessionId.toString();
			} else {
				// only start persisting once we have one accurate data point
				return null;
			}
		}
		TrajectoryPoint tp = TrajectoryPoint.of(ud);
		tp.refresh(s, minAccuracy);
		tp.persist();
		s.persist();
		LOGGER.info("location persisted");
		
		Direction d = findDirection(s);
		
		return d;
	}

	private Direction findDirection(Session s) {
		Target t = ts.findTarget(s);
		Direction d = ts.getDirection(s, t);
		return d;
	}

}