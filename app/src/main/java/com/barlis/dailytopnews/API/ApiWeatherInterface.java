package com.barlis.dailytopnews.API;

import com.barlis.dailytopnews.Model.WeatherForecastResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiWeatherInterface
{
    @GET("forecast")
    Observable<WeatherForecastResult> getForecastWeatherByLatLng(@Query("lat") String lat, @Query("lon") String lng, @Query("appid") String appid, @Query("units") String unit);
}
