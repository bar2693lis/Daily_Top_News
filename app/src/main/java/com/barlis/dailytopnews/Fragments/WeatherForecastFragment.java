package com.barlis.dailytopnews.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.barlis.dailytopnews.API.ApiWeatherClient;
import com.barlis.dailytopnews.API.ApiWeatherInterface;
import com.barlis.dailytopnews.Adapter.WeatherForecastAdapter;
import com.barlis.dailytopnews.Common.Common;
import com.barlis.dailytopnews.Model.WeatherForecastResult;
import com.barlis.dailytopnews.R;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class WeatherForecastFragment extends Fragment
{
    CompositeDisposable compositeDisposable;
    ApiWeatherInterface mService;

    RecyclerView recycler_forecast;

    static WeatherForecastFragment instance;

    public static WeatherForecastFragment getInstance()
    {
        if(instance == null)
        {
            instance = new WeatherForecastFragment();
        }
        return instance;
    }

    public WeatherForecastFragment()
    {
        compositeDisposable = new CompositeDisposable();
        Retrofit retrofit = ApiWeatherClient.getInstance();
        mService = retrofit.create(ApiWeatherInterface.class);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View itemView = inflater.inflate(R.layout.fragment_weather_forecast, container, false);

        recycler_forecast = itemView.findViewById(R.id.recycler_forecast);
        recycler_forecast.setHasFixedSize(true);
        recycler_forecast.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        getForecastWeatherInformation();

        return itemView;
    }

    @Override
    public void onStop()
    {
        compositeDisposable.clear();
        super.onStop();
    }

    private void getForecastWeatherInformation()
    {
        compositeDisposable.add(mService.getForecastWeatherByLatLng(String.valueOf(Common.current_location.getLatitude()), String.valueOf(Common.current_location.getLongitude()), Common.APP_ID, "metric").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<WeatherForecastResult>() {
            @Override
            public void accept(WeatherForecastResult weatherForecastResult) throws Exception
            {
                displayForecastWeather(weatherForecastResult);
            }
        }, new Consumer<Throwable>()
        {
            @Override
            public void accept(Throwable throwable) throws Exception
            {
                Log.d("Error", "" + throwable.getMessage());
            }
        }));
    }

    private void displayForecastWeather(WeatherForecastResult weatherForecastResult)
    {
        WeatherForecastAdapter adapter = new WeatherForecastAdapter(getContext(), weatherForecastResult);
        recycler_forecast.setAdapter(adapter);
    }
}
