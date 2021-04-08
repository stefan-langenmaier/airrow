package net.langenmaier.strohstern.data.storage.resources;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import org.apache.http.HttpHost;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RestClient;
import org.testcontainers.elasticsearch.ElasticsearchContainer;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import net.langenmaier.strohstern.data.storage.helper.FileUtil;

public class Elasticsearch implements QuarkusTestResourceLifecycleManager {

    protected final ElasticsearchContainer container = new ElasticsearchContainer(
            "docker.elastic.co/elasticsearch/elasticsearch-oss:6.8.15");

    @Override
    public Map<String, String> start() {
        container.addEnv("ES_JAVA_OPTS", "-Xms128m -Xmx128m");
        container.start();
        RestClient client = RestClient.builder(HttpHost.create(container.getHttpHostAddress())).build();
        try {
            Request request = null;

            request = new Request("PUT", "/airrow");
            request.setJsonEntity(FileUtil.readString(getClass().getResourceAsStream("/es/setup/put-airrow.json")));
            client.performRequest(request);

            request = new Request("PUT", "/airrow-trajectories");
            request.setJsonEntity(FileUtil.readString(getClass().getResourceAsStream("/es/setup/put-airrow-trajectories.json")));
            client.performRequest(request);

            request = new Request("PUT", "/airrow-points");
            request.setJsonEntity(FileUtil.readString(getClass().getResourceAsStream("/es/setup/put-airrow-points.json")));
            client.performRequest(request);

            request = new Request("PUT", "/airrow-ratings");
            request.setJsonEntity(FileUtil.readString(getClass().getResourceAsStream("/es/setup/put-airrow-ratings.json")));
            client.performRequest(request);

            request = new Request("PUT", "/airrow-capabilities");
            request.setJsonEntity(FileUtil.readString(getClass().getResourceAsStream("/es/setup/put-airrow-capabilities.json")));
            client.performRequest(request);

            request = new Request("POST", "/_scripts/airrow-default-search");
            request.setJsonEntity(FileUtil.readString(getClass().getResourceAsStream("/es/setup/post_scripts-airrow-default-search.json")));
            client.performRequest(request);

            request = new Request("POST", "/airrow/_bulk");
            request.setJsonEntity(FileUtil.readString(getClass().getResourceAsStream("/es/setup/bulk-airrow-default-data.ndjson")));
            client.performRequest(request);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Collections.singletonMap("quarkus.elasticsearch.hosts", container.getHttpHostAddress());
    }

    @Override
    public void stop() {
        container.close();
    }
}
