package net.langenmaier.strohstern.data.storage.service;

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
import net.langenmaier.strohstern.data.storage.dto.EsCount;
import net.langenmaier.strohstern.data.storage.dto.EsPersonalInformation;
import net.langenmaier.strohstern.data.storage.dto.JsonPersonalInformation;
import net.langenmaier.strohstern.data.storage.helper.FileUtil;

@ApplicationScoped
public class PersonalService {

	@Inject
	RestClient restClient;

	public JsonPersonalInformation get(String sessionId) {
		Request pi = new Request("GET", "/airrow/_search");
		Request trajectoryRequest = new Request("GET", "/airrow-trajectories/_doc/_count");
		Request pointsRequest = new Request("GET", "/airrow-points/_doc/_count");
		Request ratingsRequest = new Request("GET", "/airrow-ratings/_doc/_count");

		String PI_QUERY = FileUtil.readString(getClass().getResourceAsStream("/es/query-session-id.json"));
		PI_QUERY = PI_QUERY.replaceAll("\"_SESSION_ID_\"", "\"" + sessionId + "\"");
		pi.setJsonEntity(PI_QUERY);

		String COUNT_QUERY = FileUtil.readString(getClass().getResourceAsStream("/es/query-creator-id.json"));
		COUNT_QUERY = COUNT_QUERY.replaceAll("\"_CREATOR_ID_\"", "\"" + sessionId + "\"");
		pointsRequest.setJsonEntity(COUNT_QUERY);
		ratingsRequest.setJsonEntity(COUNT_QUERY);

		COUNT_QUERY = FileUtil.readString(getClass().getResourceAsStream("/es/query-uuid.json"));
		COUNT_QUERY = COUNT_QUERY.replaceAll("\"_UUID_\"", "\"" + sessionId + "\"");
		trajectoryRequest.setJsonEntity(COUNT_QUERY);

		Response responsePI = null;
		Response responseTrajectory = null;
		Response responsePoints = null;
		Response responseRatings = null;
		try {
			responsePI = restClient.performRequest(pi);
			responseTrajectory = restClient.performRequest(trajectoryRequest);
			responsePoints = restClient.performRequest(pointsRequest);
			responseRatings = restClient.performRequest(ratingsRequest);
		} catch (IOException e) {
			e.printStackTrace();
		}

		String bodyPI = null;
		String bodyTrajectory = null;
		String bodyPoints = null;
		String bodyRatings = null;
		try {
			bodyPI = EntityUtils.toString(responsePI.getEntity());
			bodyTrajectory = EntityUtils.toString(responseTrajectory.getEntity());
			bodyPoints = EntityUtils.toString(responsePoints.getEntity());
			bodyRatings = EntityUtils.toString(responseRatings.getEntity());
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		JsonPersonalInformation jpi = new JsonPersonalInformation();

		JsonObject json = new JsonObject(bodyPI); 
		JsonArray hits = json.getJsonObject("hits").getJsonArray("hits");
		if (hits.size() > 0) {
			JsonObject target = hits.getJsonObject(0);
			JsonObject source = target.getJsonObject("_source");
			EsPersonalInformation epi = source.mapTo(EsPersonalInformation.class);

			jpi.refCode = epi.refCode;
		}

		json = new JsonObject(bodyTrajectory);
		EsCount trajectory = json.mapTo(EsCount.class);
		jpi.trajectoryPoints = trajectory.count;

		json = new JsonObject(bodyPoints);
		EsCount points = json.mapTo(EsCount.class);
		jpi.pointsPoints = points.count;

		json = new JsonObject(bodyRatings);
		EsCount ratings = json.mapTo(EsCount.class);
		jpi.ratingsPoints = ratings.count;

		return jpi;
	}

}
