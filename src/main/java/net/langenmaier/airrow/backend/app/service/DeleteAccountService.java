package net.langenmaier.airrow.backend.app.service;

import java.io.IOException;
import java.util.UUID;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import co.elastic.clients.transport.rest5_client.low_level.Request;
import co.elastic.clients.transport.rest5_client.low_level.Rest5Client;

@ApplicationScoped
public class DeleteAccountService {

	@Inject
	Rest5Client restClient;

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
