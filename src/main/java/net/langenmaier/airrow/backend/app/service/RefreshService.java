package net.langenmaier.airrow.backend.app.service;

import java.io.IOException;

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
import net.langenmaier.airrow.backend.app.dto.EsLiveTrajectoryDto;
import net.langenmaier.airrow.backend.app.dto.EsTarget;
import net.langenmaier.airrow.backend.app.dto.EsTrajectoryDto;
import net.langenmaier.airrow.backend.app.dto.JsonCapabilityDto;
import net.langenmaier.airrow.backend.app.dto.JsonNavigationState;
import net.langenmaier.airrow.backend.app.dto.JsonRefreshTarget;
import net.langenmaier.airrow.backend.app.enumeration.SearchState;
import net.langenmaier.airrow.backend.app.helper.FileUtil;
import net.langenmaier.airrow.backend.app.helper.GeoTools;
import net.langenmaier.airrow.backend.app.model.Session;
import net.langenmaier.airrow.backend.app.model.Target;

@ApplicationScoped
public class RefreshService {

	private static final Double MIN_FOUND_DISTANCE = 10d;

	@Inject
	RestClient restClient;

	@ConfigProperty(name = "airrow.search.walkDistance")
	String walkDistance;

	@ConfigProperty(name = "airrow.search.scale")
	Double scale;

	@ConfigProperty(name = "airrow.search.ttl")
	String ttl;

	@ConfigProperty(name = "airrow.search.minAccuracy")
	Integer minAccuracy;

	@Inject
	CapabilityService cs;

	public JsonNavigationState refresh(Session s) {
		Request live = new Request("PUT", "/airrow/_doc/" + s.uuid.toString());
		live.setJsonEntity(JsonObject.mapFrom(EsLiveTrajectoryDto.of(s)).toString());

		Request trajectory = new Request("POST", "/airrow-trajectories/_doc/");
		trajectory.setJsonEntity(JsonObject.mapFrom(EsTrajectoryDto.of(s)).toString());

		try {
			if (s.accuracy <= minAccuracy) {
				// only update if accurate
				restClient.performRequest(live);
			}
			restClient.performRequest(trajectory);
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (s.accuracy <= minAccuracy) {
			JsonNavigationState d = getNavigationState(s);
			return d;
		} else {
			// only return a direction when accurate
			return null;
		}
	}

	public JsonNavigationState getNavigationState(Session s) {
		Request request = new Request("GET", "/airrow/_search/template");

		String NEAREST_POINT_QUERY = null;
		NEAREST_POINT_QUERY = FileUtil.readString(getClass().getResourceAsStream("/es/query-template.json"));
		NEAREST_POINT_QUERY = NEAREST_POINT_QUERY.replaceAll("\"_SELF_\"", "\"" + s.uuid + "\"");
		NEAREST_POINT_QUERY = NEAREST_POINT_QUERY.replaceAll("\"_STATUS_\"", "\"" + s.status + "\"");
		NEAREST_POINT_QUERY = NEAREST_POINT_QUERY.replaceAll("\"_LATITUDE_\"", String.format("%.6f", s.location.lat));
		NEAREST_POINT_QUERY = NEAREST_POINT_QUERY.replaceAll("\"_LONGITUDE_\"", String.format("%.6f", s.location.lon));
		NEAREST_POINT_QUERY = NEAREST_POINT_QUERY.replaceAll("\"_SCALE_\"", String.format("%.2f", scale));
		NEAREST_POINT_QUERY = NEAREST_POINT_QUERY.replaceAll("\"_TTL_\"", "\"" + ttl + "\"");
		NEAREST_POINT_QUERY = NEAREST_POINT_QUERY.replaceAll("\"_WALK_DISTANCE_\"", "\"" + walkDistance + "\"");

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
		if (hits.size() == 0)
			return null;
		JsonObject target = hits.getJsonObject(0);
		JsonObject source = target.getJsonObject("_source");
		EsTarget et = source.mapTo(EsTarget.class);
		
		JsonNavigationState ns = new JsonNavigationState();
		ns.target = Target.of(et);
		ns.geo_distance = GeoTools.getDistance(s.location, et.location);
		ns.angle = GeoTools.getWebAngle(s.location, et.location);
		if (ns.geo_distance < MIN_FOUND_DISTANCE) {
			ns.searchState = SearchState.FOUND;
		} else {
			ns.target.clean();
		}
		if (s.status != null && s.status.contains("😷")) {
			ns.angle = (ns.angle+180)%360;
		}

		ns.capability = JsonCapabilityDto.of(cs.getCapability(s.uuid));
		return ns;
	}

	public Target getTarget(JsonRefreshTarget rt) {
		Request request = new Request("GET", "/airrow-points/_search");

		String POINT_QUERY = null;
		POINT_QUERY = FileUtil.readString(getClass().getResourceAsStream("/es/query-refCode.json"));
		POINT_QUERY = POINT_QUERY.replaceAll("\"_REF_CODE_\"", "\"" + rt.refCode + "\"");

		request.setJsonEntity(POINT_QUERY);
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
		if (hits.size() == 0)
			return null;
		JsonObject target = hits.getJsonObject(0);
		JsonObject source = target.getJsonObject("_source");
		EsTarget et = source.mapTo(EsTarget.class);

		return Target.of(et);
	}

}
