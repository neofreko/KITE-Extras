package io.cosmosoftware.kite.util;

import com.fasterxml.jackson.databind.JsonNode;
import io.cosmosoftware.kite.report.KiteLogger;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import org.json.JSONObject;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.concurrent.CompletableFuture;

public class SelGridClient {
    private static final KiteLogger logger = KiteLogger.getLogger(TestUtils.class.getName());

    /**
     * Get session information from selenium 4 session
     * @param hubIpOrDns selenium hub host
     * @param sessionId selenium session id
     * @param port selenium hub port
     * @return json node {"data": { "session": { "uri": "abc", "nodeUri": "xyz"}}}
     */
    static JsonNode getSeleniumSessionInfo(String hubIpOrDns, String sessionId, int port) {
        JsonNode responseJsonNode = null;
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // debugging
        // java.net.Proxy proxyTest = new java.net.Proxy(java.net.Proxy.Type.HTTP, new InetSocketAddress("localhost", 8080));

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        // httpClient.proxy(proxyTest);
        // httpClient.addInterceptor(interceptor);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(hubIpOrDns + ":" + port)
                .addConverterFactory(JacksonConverterFactory.create())
                .client(httpClient.build())
                .build();

        SeleniumGrid4Client retrofitClient = retrofit.create(SeleniumGrid4Client.class);
        JSONObject jsonObject = new JSONObject();
        // body:
        // {
        //  "query":"{ session (id: \"" + sessionId + "\") { id, capabilities, startTime, uri, nodeId, nodeUri, sessionDurationMillis, slot { id, stereotype, lastStarted } } } "
        // }
        jsonObject.put("query", "{ session (id: \"" + sessionId + "\") { id, nodeUri } } ");

        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        CompletableFuture<JsonNode> response = retrofitClient.getGrid4Session(body);
        try {
            responseJsonNode = response.get();
            logger.debug("Response: " + responseJsonNode.toString());
        } catch (Exception e) {
            logger.error("Exception while talking to the grid", e);
        }

        return responseJsonNode;
    }
}
