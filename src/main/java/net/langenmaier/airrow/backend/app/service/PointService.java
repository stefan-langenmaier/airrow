package net.langenmaier.airrow.backend.app.service;

import java.io.IOException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import net.langenmaier.airrow.backend.app.dto.EsPointDto;
import net.langenmaier.airrow.backend.app.dto.JsonPoint;
import net.langenmaier.airrow.backend.app.dto.JsonPointId;
import net.langenmaier.airrow.backend.app.dto.JsonPointsInformation;
import net.langenmaier.airrow.backend.app.dto.JsonPointsRequest;
import net.langenmaier.airrow.backend.app.helper.FileUtil;

@ApplicationScoped
public class PointService {

	@Inject
	RestClient restClient;

	public JsonPointsInformation list(JsonPointsRequest pr) {
		Request pi = new Request("GET", "/airrow-points/_search");

		String PI_QUERY = FileUtil.readString(getClass().getResourceAsStream("/es/query-creator-id.json"));
		PI_QUERY = PI_QUERY.replaceAll("\"_CREATOR_ID_\"", "\"" + pr.uuid + "\"");
		pi.setJsonEntity(PI_QUERY);

		Response responsePI = null;
		try {
			responsePI = restClient.performRequest(pi);
		} catch (IOException e) {
			e.printStackTrace();
		}

		String bodyPI = null;
		try {
			bodyPI = EntityUtils.toString(responsePI.getEntity());
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		JsonPointsInformation jpi = new JsonPointsInformation();

		JsonObject json = new JsonObject(bodyPI); 
		JsonArray hits = json.getJsonObject("hits").getJsonArray("hits");
		if (hits.size() > 0) {
			for (int i=0; i<10 && i<hits.size(); i++) {
				JsonObject target = hits.getJsonObject(i);
				JsonObject source = target.getJsonObject("_source");
				EsPointDto ep = source.mapTo(EsPointDto.class);
				ep.uuid = target.getString("_id");
				JsonPoint jp = JsonPoint.of(ep, pr.location);

				jpi.points.add(jp);
			}
		}

		return jpi;
	}

	public JsonPointsInformation listPublic(JsonPointsRequest pr) {
		Request pi = new Request("GET", "/airrow-points/_search");

		String PI_QUERY = FileUtil.readString(getClass().getResourceAsStream("/es/query-permanent-points.json"));
		pi.setJsonEntity(PI_QUERY);

		Response responsePI = null;
		try {
			responsePI = restClient.performRequest(pi);
		} catch (IOException e) {
			e.printStackTrace();
		}

		String bodyPI = null;
		try {
			bodyPI = EntityUtils.toString(responsePI.getEntity());
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		JsonPointsInformation jpi = new JsonPointsInformation();

		JsonObject json = new JsonObject(bodyPI);
		JsonArray hits = json.getJsonObject("hits").getJsonArray("hits");
		if (hits.size() > 0) {
			for (int i=0; i<10 && i<hits.size(); i++) {
				JsonObject target = hits.getJsonObject(i);
				JsonObject source = target.getJsonObject("_source");
				EsPointDto ep = source.mapTo(EsPointDto.class);
				// don't add the uuid because it is used to modify the point
				ep.uuid = null;
				JsonPoint jp = JsonPoint.of(ep, pr.location);

				jpi.points.add(jp);
			}
		}

		return jpi;
	}

	public void delete(JsonPointId point) {
		Request points = new Request("POST", "/airrow-points/_delete_by_query");

		String PI_QUERY = FileUtil.readString(getClass().getResourceAsStream("/es/query-session-id.json"));
		PI_QUERY = PI_QUERY.replaceAll("\"_SESSION_ID_\"", "\"" + point.uuid + "\"");

		points.setJsonEntity(PI_QUERY);

		try {
			restClient.performRequest(points);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
