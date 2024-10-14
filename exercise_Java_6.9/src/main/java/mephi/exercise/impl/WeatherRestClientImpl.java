package mephi.exercise.impl;

import mephi.exercise.PropertyLoader;
import mephi.exercise.WeatherRestClient;
import org.apache.http.client.utils.URIBuilder;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class WeatherRestClientImpl implements WeatherRestClient {

    private final PropertyLoader propertyLoader;

    public WeatherRestClientImpl(PropertyLoader propertyLoader) {
        this.propertyLoader = propertyLoader;
    }

    @Override
    public String getWeather(String limit) {
        try {
            URIBuilder uriBuilder = new URIBuilder(propertyLoader.getProperty("api.host"));
            uriBuilder.setPort(Integer.parseInt(propertyLoader.getProperty("api.port")));
            uriBuilder.setPath(propertyLoader.getProperty("api.path"));
            uriBuilder.setParameter(propertyLoader.getProperty("api.param.lat"), propertyLoader.getProperty("api.param.lat.value"));
            uriBuilder.setParameter(propertyLoader.getProperty("api.param.lon"), propertyLoader.getProperty("api.param.lon.value"));
            if (limit != null && !limit.isEmpty() && !"0".equals(limit)) {
                uriBuilder.setParameter(propertyLoader.getProperty("api.param.limit"), limit);
            }

            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uriBuilder.build())
                    .GET()
                    .header(propertyLoader.getProperty("api.key.name"), propertyLoader.getProperty("api.key.value"))
                    .build();

            HttpResponse<String> response = client
                    .send(request, HttpResponse.BodyHandlers.ofString());

            return response.body();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
