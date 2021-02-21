package net.langenmaier.strohstern.data.storage.service;

import java.io.IOException;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import net.langenmaier.strohstern.data.storage.dto.EsLiveRatingDto;
import net.langenmaier.strohstern.data.storage.dto.EsRatingDto;
import net.langenmaier.strohstern.data.storage.dto.JsonRateData;
import net.langenmaier.strohstern.data.storage.model.Entity;
import net.langenmaier.strohstern.data.storage.model.Rating;

@ApplicationScoped
public class RateService {

	@Inject
	RestClient restClient;

	public void rate(Rating r) {
		Request live = new Request("POST", "/airrow/_doc/?routing=1");
		live.setJsonEntity(JsonObject.mapFrom(EsLiveRatingDto.of(r)).toString());

		Request rating = new Request("POST", "/airrow-ratings/_doc/");
		rating.setJsonEntity(JsonObject.mapFrom(EsRatingDto.of(r)).toString());

		try {
			restClient.performRequest(live);
			restClient.performRequest(rating);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Entity getEntity(JsonRateData rd) {
		Request request = new Request("GET", "/airrow/_search");

		String ENTITY_QUERY = "{\"query\": {\"term\": {\"refCode\": \"" + rd.refCode + "\"}}}";

		request.setJsonEntity(ENTITY_QUERY);
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
		Entity e = new Entity();
		e.uuid = UUID.fromString(target.getString("_id"));

		return e;
	}
}
