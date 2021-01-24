package net.langenmaier.strohstern.data.storage;

import java.io.IOException;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

@ApplicationScoped
public class TargetService {

	private static final Double MIN_FOUND_DISTANCE = 10d;

	// @Inject
	// public EntityManager em;

	@Inject
	RestClient restClient;

	@ConfigProperty(name = "airrow.debug")
	Boolean debug;

	@ConfigProperty(name = "airrow.search.walkDistance")
	Integer walkDistance;

	@ConfigProperty(name = "airrow.search.scale")
	Integer scale;

	@ConfigProperty(name = "airrow.search.ttl")
	Integer ttl;

	public Integer updateSession(UUID sessionId, SessionData sd) {
		Request request = new Request("PUT", "/airrow/_doc/" + sessionId);

		request.setJsonEntity(JsonObject.mapFrom(sd).toString());

		Response response = null;
		try {
			response = restClient.performRequest(request);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response.getStatusLine().getStatusCode();
	}

	public Target findTarget(SessionData sd) {
		Request request = new Request("GET", "/airrow/_search");

		String NEAREST_POINT_QUERY = null;
		NEAREST_POINT_QUERY = FileUtil.readString(getClass().getResourceAsStream("/es/query-template.json"));
		
		System.out.println(NEAREST_POINT_QUERY);
		request.setJsonEntity(NEAREST_POINT_QUERY);
		Response response = null;
		try {
			response = restClient.performRequest(request);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String responseBody = null;
		try {
			responseBody = EntityUtils.toString(response.getEntity());
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		JsonObject json = new JsonObject(responseBody); 
		JsonArray hits = json.getJsonObject("hits").getJsonArray("hits");
		JsonObject target = hits.getJsonObject(0);
		JsonObject source = target.getJsonObject("_source");
		SessionData targetData = source.mapTo(SessionData.class);
		
		Target t = new Target();
		t.latitude = targetData.location.lat;
		t.longitude = targetData.location.lon;
		t.status = targetData.status;
		t.distance = target.getDouble("_score");
		t.geo_distance = GeoTools.getDistance(sd.location, targetData.location);

		return t;
	}

	// public Target findTarget(Session s) {
	// 	Target t = null;
	// 	try {
	// 		String NEAREST_POINT_SQL = new StringBuilder()
	// 			.append("SELECT ")
	// 				.append("get_distance_in_meters_between_geo_locations(start.latitude, start.longitude, target.latitude, target.longitude) AS geo_distance,")
	// 				.append("emoji_distance(start.status, target.status, ?2) AS emoji_distance,")
	// 				.append("target.id AS targetId, ")
	// 				.append("target.latitude AS targetLatitude, ")
	// 				.append("target.longitude AS targetLongitude, ")
	// 				.append("target.status AS targetStatus")
	// 			.append(" FROM Session AS start")
	// 			.append(" INNER JOIN Session AS target ")
	// 				.append("ON start.id != target.id ")
	// 				.append("AND start.uuid = ?1 ")
	// 				.append("AND (")
	// 					.append("(target.updatedAt > (NOW() - INTERVAL ?3 SECOND))")
	// 					.append(" OR ")
	// 					.append("(target.permanent = TRUE)")
	// 				.append(")")
	// 			.append("ORDER BY IF(geo_distance<?4,emoji_distance,1), (geo_distance*emoji_distance) ")
	// 			.append("LIMIT 1;")
	// 			.toString();
	// 		Object o = em.createNativeQuery(NEAREST_POINT_SQL)
	// 				.setParameter(1, s.uuid)
	// 				.setParameter(2, scale)
	// 				.setParameter(3, ttl)
	// 				.setParameter(4, walkDistance)
	// 				.getSingleResult();
	// 		if (o instanceof Object[]) {
	// 			t = new Target();
	// 			Object[] of = (Object[]) o;

	// 			try {
	// 				t.geo_distance = ((BigDecimal) of[0]).doubleValue();
	// 				t.emoji_distance = (Double) of[1];
	// 				t.id = ((BigInteger) of[2]).longValue();
	// 				t.latitude = (Double) of[3];
	// 				t.longitude = (Double) of[4];
	// 				t.status = (String) of[5];
	// 			} catch (NullPointerException npe) {
	// 				npe.printStackTrace();
	// 				System.out.println(of);
	// 				return null;
	// 			}
	// 		}
	// 	} catch (NoResultException e) {
	// 		return null;
	// 	}
	// 	return t;
	// }

	// http://www.movable-type.co.uk/scripts/latlong.html
	public Direction getDirection(SessionData sd, Target t) {
		if (t == null) return null;

		double latStart = Math.toRadians(sd.location.lat);
		double longStart = Math.toRadians(sd.location.lon);
		double latEnd = Math.toRadians(t.latitude);
		double longEnd = Math.toRadians(t.longitude);

		Direction d = new Direction(GeoTools.getWebAngle(Math.toDegrees(GeoTools.getBearing(latStart, longStart, latEnd, longEnd))), t.status);
		if (t.geo_distance < MIN_FOUND_DISTANCE) {
			d.searchState = SearchState.FOUND;
		}
		
		// corona mode
		if (s.status != null && s.status.contains("😷")) {
			d.angle = (d.angle+180)%360;
		}
		
		if (debug) {
			d.target = t;
		}

		return d;
	}
}
