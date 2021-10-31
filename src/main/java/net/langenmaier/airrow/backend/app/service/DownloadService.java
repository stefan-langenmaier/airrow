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
import net.langenmaier.airrow.backend.app.dto.JsonDownloadDto;
import net.langenmaier.airrow.backend.app.helper.FileUtil;
import net.langenmaier.airrow.backend.app.model.Download;

@ApplicationScoped
public class DownloadService {
	@Inject
	RestClient restClient;

	public Download getPreview(String fileHash) {
		return get(fileHash, "preview");
	}

	public Download getObject(String fileHash) {
		return get(fileHash, "object");
	}

	public Download getBackground(String fileHash) {
		return get(fileHash, "background");
	}

	public Download get(String fileHash, String path) {
		Request downloadRequest = new Request("GET", "/airrow-points/_search");

		String DOWNLOAD_QUERY = FileUtil.readString(getClass().getResourceAsStream("/es/query-download.json"));
		DOWNLOAD_QUERY = DOWNLOAD_QUERY.replaceAll("\"_FILE_HASH_\"", "\"" + fileHash + "\"");
		DOWNLOAD_QUERY = DOWNLOAD_QUERY.replaceAll("\"_PATH_\"", "\"" + path + "\"");
		DOWNLOAD_QUERY = DOWNLOAD_QUERY.replaceAll("\"_PATH_.", "\"" + path + ".");
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
		if (hits.size() == 0) return null;
		JsonObject target = hits.getJsonObject(0);
		JsonObject source = target.getJsonObject("_source");
		source = source.getJsonObject(path);

		JsonDownloadDto jddto  = source.mapTo(JsonDownloadDto.class);

		return Download.of(jddto);
	}

}
