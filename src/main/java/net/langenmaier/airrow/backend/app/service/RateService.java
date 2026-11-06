package net.langenmaier.airrow.backend.app.service;

import java.io.IOException;
import java.util.UUID;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import co.elastic.clients.transport.rest5_client.low_level.Request;
import co.elastic.clients.transport.rest5_client.low_level.Response;
import co.elastic.clients.transport.rest5_client.low_level.Rest5Client;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import net.langenmaier.airrow.backend.app.dto.EsLiveRatingDto;
import net.langenmaier.airrow.backend.app.dto.EsRatingDto;
import net.langenmaier.airrow.backend.app.dto.JsonRateData;
import net.langenmaier.airrow.backend.app.model.Entity;
import net.langenmaier.airrow.backend.app.model.Rating;

@ApplicationScoped
public class RateService {

	@Inject
	Rest5Client restClient;

	public void rate(Rating r) {
		// refresh forces to update the index immediately
		Request live = new Request("POST", "/airrow/_doc/?routing=1&refresh=true");
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
