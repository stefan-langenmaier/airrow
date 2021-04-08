package net.langenmaier.strohstern.data.storage.service;

import java.io.IOException;
import java.util.UUID;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

import io.quarkus.scheduler.Scheduled;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import net.langenmaier.strohstern.data.storage.dto.EsCapabilityDto;
import net.langenmaier.strohstern.data.storage.helper.FileUtil;
import net.langenmaier.strohstern.data.storage.model.Capability;

@ApplicationScoped
public class CapabilityService {

	@Inject
	RestClient restClient;

	@Inject
	PersonalService ps;

	private final static Logger LOGGER = Logger.getLogger(CapabilityService.class.getName());

	@Scheduled(every="30s")     
    void refresh() {
		Request airrows = new Request("POST", "/airrow/_search?scroll=10s");
		String AIRROWS_QUERY = FileUtil.readString(getClass().getResourceAsStream("/es/all-airrows.json"));
		airrows.setJsonEntity(AIRROWS_QUERY);

		boolean cont;
		do {
			cont = false;
			Response response = null;
			try {
				response = restClient.performRequest(airrows);
			} catch (IOException e) {
				e.printStackTrace();
			}

			String body = null;
			try {
				body = EntityUtils.toString(response.getEntity());
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			JsonObject json = new JsonObject(body); 
			JsonArray hits = json.getJsonObject("hits").getJsonArray("hits");
			for (int i=0; i<hits.size(); ++i) {
				JsonObject target = hits.getJsonObject(i);
				updateCapability(UUID.fromString(target.getString("_id")));
			}
			
			if (hits.size() > 0) {
				cont = true;
				String scrollId = json.getString("_scroll_id");
				airrows = new Request("POST", "/_search/scroll");
				String CONTINUE_QUERY = "{\"scroll\" : \"10s\", \"scroll_id\" : \"" + scrollId + "\"}";
				airrows.setJsonEntity(CONTINUE_QUERY);
			}

		} while (cont);

    }

	public Capability updateCapability(UUID sessionId) {
		LOGGER.info(sessionId.toString());

		Capability cap = buildCapability(sessionId);

		Request live = new Request("PUT", "/airrow-capabilities/_doc/" + sessionId.toString());
		live.setJsonEntity(JsonObject.mapFrom(EsCapabilityDto.of(cap)).toString());

		try {
			restClient.performRequest(live);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return cap;
	}

	private Capability buildCapability(UUID sessionId) {
		return Capability.of(ps.get(sessionId.toString()));
	}


	public Capability getCapability(UUID sessionId) {
		Request request = new Request("GET", "/airrow-capabilities/_search");

		String ENTITY_QUERY = "{\"query\": {\"term\": {\"_id\": \"" + sessionId.toString() + "\"}}}";

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
			return Capability.empty();
		JsonObject target = hits.getJsonObject(0);
		JsonObject source = target.getJsonObject("_source");
		EsCapabilityDto ec = source.mapTo(EsCapabilityDto.class);

		return Capability.of(ec);
	}

}
