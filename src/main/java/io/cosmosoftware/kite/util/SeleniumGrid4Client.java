package io.cosmosoftware.kite.util;

import com.fasterxml.jackson.databind.JsonNode;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

import java.util.concurrent.CompletableFuture;

public interface SeleniumGrid4Client {
    /**
     * Talk to selenium grid 4
     *
     * @return text response from selenium grid
     */


    @POST("/graphql")
  @Headers("accept: application/json")
  CompletableFuture<JsonNode> getGrid4Session(@Body RequestBody body);
}

