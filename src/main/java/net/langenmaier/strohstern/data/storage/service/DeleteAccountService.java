package net.langenmaier.strohstern.data.storage.service;

import java.io.IOException;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.elasticsearch.client.Request;
import org.elasticsearch.client.RestClient;

@ApplicationScoped
public class DeleteAccountService {

	@Inject
	RestClient restClient;

	public void delete(UUID sessionId) {
		Request delete = new Request("POST", "/airrow-trajectories/_delete_by_query");
		String DELETE_QUERY = "{\"query\": {\"term\": {\"creator\": \"" + sessionId.toString() + "\"}}}";
		delete.setJsonEntity(DELETE_QUERY);

		try {
			restClient.performRequest(delete);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
