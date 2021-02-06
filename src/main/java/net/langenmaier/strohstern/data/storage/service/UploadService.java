package net.langenmaier.strohstern.data.storage.service;

import java.io.IOException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.elasticsearch.client.Request;
import org.elasticsearch.client.RestClient;

import io.vertx.core.json.JsonObject;
import net.langenmaier.strohstern.data.storage.dto.EsEntityDto;
import net.langenmaier.strohstern.data.storage.model.Upload;

@ApplicationScoped
public class UploadService {

	@Inject
	RestClient restClient;

	public void register(Upload upload) {
		Request entity = new Request("POST", "/airrow-entities/_doc/");
		entity.setJsonEntity(JsonObject.mapFrom(EsEntityDto.of(upload)).toString());

		try {
			restClient.performRequest(entity);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
