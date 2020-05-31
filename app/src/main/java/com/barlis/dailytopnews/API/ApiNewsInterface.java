package com.barlis.dailytopnews.API;

import com.barlis.dailytopnews.Model.Headlines;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiNewsInterface
{
    @GET("top-headlines")
    Call<Headlines> getHeadlines(@Query("country") String country,
                                 @Query("apiKey") String apiKey
    );

    @GET("everything")
    Call<Headlines> getSpecificData(@Query("q") String query,
                                    @Query("apiKey") String apiKey
    );

    @GET("top-headlines")
    Call<Headlines> getCategories(@Query("country") String country,
                                  @Query("category") String category,
                                  @Query("apiKey") String apiKey);
}
