package net.langenmaier.airrow.backend.app.service;

import java.io.IOException;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import co.elastic.clients.transport.rest5_client.low_level.Request;
import co.elastic.clients.transport.rest5_client.low_level.Response;
import co.elastic.clients.transport.rest5_client.low_level.Rest5Client;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import net.langenmaier.airrow.backend.app.dto.JsonDownloadDto;
import net.langenmaier.airrow.backend.app.helper.FileUtil;
import net.langenmaier.airrow.backend.app.model.Download;

@ApplicationScoped
public class DownloadService {

	@Inject
	Rest5Client restClient;

	public Download get(String fileHash) {
		Request downloadRequest = new Request("GET", "/airrow-points/_search");

		String DOWNLOAD_QUERY = FileUtil.readString(getClass().getResourceAsStream("/es/query-download.json"));
		DOWNLOAD_QUERY = DOWNLOAD_QUERY.replaceAll("\"_FILE_HASH_\"", "\"" + fileHash + "\"");
		downloadRequest.setJsonEntity(DOWNLOAD_QUERY);

		Response response = null;
		try {
			response = restClient.performRequest(downloadRequest);
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

		JsonDownloadDto jddto  = source.mapTo(JsonDownloadDto.class);

		return Download.of(jddto);
	}

}
