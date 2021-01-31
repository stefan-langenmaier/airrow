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

	@Inject
	RestClient restClient;

	@ConfigProperty(name = "airrow.debug")
	Boolean debug;

	@ConfigProperty(name = "airrow.search.walkDistance")
	String walkDistance;

	@ConfigProperty(name = "airrow.search.scale")
	Double scale;

	@ConfigProperty(name = "airrow.search.ttl")
	String ttl;

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
		Request request = new Request("GET", "/airrow/_search/template");

		String NEAREST_POINT_QUERY = null;
		NEAREST_POINT_QUERY = FileUtil.readString(getClass().getResourceAsStream("/es/query-template.json"));
		NEAREST_POINT_QUERY = NEAREST_POINT_QUERY.replaceAll("\"_SELF_\"", "\"" + sd.uuid + "\"");
		NEAREST_POINT_QUERY = NEAREST_POINT_QUERY.replaceAll("\"_STATUS_\"", "\"" + sd.status + "\"");
		NEAREST_POINT_QUERY = NEAREST_POINT_QUERY.replaceAll("\"_LATITUDE_\"", String.format("%.6f", sd.location.lat));
		NEAREST_POINT_QUERY = NEAREST_POINT_QUERY.replaceAll("\"_LONGITUDE_\"", String.format("%.6f", sd.location.lon));
		NEAREST_POINT_QUERY = NEAREST_POINT_QUERY.replaceAll("\"_SCALE_\"", String.format("%.2f", scale));
		NEAREST_POINT_QUERY = NEAREST_POINT_QUERY.replaceAll("\"_TTL_\"", "\"" + ttl + "\"");
		NEAREST_POINT_QUERY = NEAREST_POINT_QUERY.replaceAll("\"_WALK_DISTANCE_\"", "\"" + walkDistance + "\"");

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
		if (sd.status != null && sd.status.contains("😷")) {
			d.angle = (d.angle+180)%360;
		}
		
		if (debug) {
			d.target = t;
		}

		return d;
	}
}
