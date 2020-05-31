package com.barlis.dailytopnews.API;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiNewsClient
{
    private static final String BASE_NEWS_URL = "https://newsapi.org/v2/";
    private static ApiNewsClient apiNewsClient;
    private static Retrofit retrofit;

    private ApiNewsClient()
    {
        retrofit = new Retrofit.Builder().baseUrl(BASE_NEWS_URL).addConverterFactory(GsonConverterFactory.create()).build();
    }

    public static synchronized ApiNewsClient getInstance()
    {
        if (apiNewsClient == null)
        {
            apiNewsClient = new ApiNewsClient();
        }
        return apiNewsClient;
    }


    public ApiNewsInterface getApi()
    {
        return retrofit.create(ApiNewsInterface.class);
    }
}
