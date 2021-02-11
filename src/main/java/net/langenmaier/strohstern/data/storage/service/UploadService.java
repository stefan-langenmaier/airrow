package net.langenmaier.strohstern.data.storage.service;

import java.io.IOException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.elasticsearch.client.Request;
import org.elasticsearch.client.RestClient;

import io.vertx.core.json.JsonObject;
import net.langenmaier.strohstern.data.storage.dto.EsPointDto;
import net.langenmaier.strohstern.data.storage.dto.EsLivePointDto;
import net.langenmaier.strohstern.data.storage.model.Upload;

@ApplicationScoped
public class UploadService {

	@Inject
	RestClient restClient;

	public void register(Upload upload) {
		Request live = new Request("PUT", "/airrow/_doc/" + upload.uuid.toString());
		live.setJsonEntity(JsonObject.mapFrom(EsLivePointDto.of(upload)).toString());

		Request entity = new Request("PUT", "/airrow-points/_doc/" + upload.uuid.toString());
		entity.setJsonEntity(JsonObject.mapFrom(EsPointDto.of(upload)).toString());
		try {
			restClient.performRequest(live);
			restClient.performRequest(entity);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
