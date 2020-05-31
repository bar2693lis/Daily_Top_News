package com.barlis.dailytopnews.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.barlis.dailytopnews.API.ApiWeatherClient;
import com.barlis.dailytopnews.API.ApiWeatherInterface;
import com.barlis.dailytopnews.Common.Common;
import com.barlis.dailytopnews.IconWeatherProvider;
import com.barlis.dailytopnews.Model.WeatherForecastResult;
import com.barlis.dailytopnews.R;
import com.squareup.picasso.Picasso;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class TodayWeatherFragment extends Fragment
{
    TextView tvCityAndCountryName, tvWeatherDescription, tvCurrentTemp, tvMaxTemp, tvMinTemp, updated_at;
    ImageView ivWeatherIcon;
    LinearLayout weather_panel;
    ProgressBar loading;
    CardView cardView;
    CompositeDisposable compositeDisposable;
    ApiWeatherInterface mService;

    static TodayWeatherFragment instance;

    public static TodayWeatherFragment getInstance()
    {
        if(instance == null)
        {
            instance = new TodayWeatherFragment();
        }
        return instance;
    }

    public TodayWeatherFragment()
    {
        compositeDisposable = new CompositeDisposable();
        Retrofit retrofit = ApiWeatherClient.getInstance();
        mService = retrofit.create(ApiWeatherInterface.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View itemView = inflater.inflate(R.layout.weather_fragment, container, false);

        ivWeatherIcon = itemView.findViewById(R.id.ivWeatherIcon);

        tvCityAndCountryName = itemView.findViewById(R.id.tvCityAndCountryName);
        tvCurrentTemp = itemView.findViewById(R.id.tvCurrentTemp);
        tvWeatherDescription = itemView.findViewById(R.id.tvWeatherDescription);
        updated_at = itemView.findViewById(R.id.updated_at);
        tvMaxTemp = itemView.findViewById(R.id.tvMaxTemp);
        tvMinTemp = itemView.findViewById(R.id.tvMinTemp);

        weather_panel = itemView.findViewById(R.id.weather_panel);

        loading = itemView.findViewById(R.id.loading);

        cardView = itemView.findViewById(R.id.cardView);

        getWeatherInformation();

        return itemView;
    }

    private void getWeatherInformation()
    {
        compositeDisposable.add(mService.getForecastWeatherByLatLng(String.valueOf(Common.current_location.getLatitude()),
                String.valueOf(Common.current_location.getLongitude()),
                Common.APP_ID,
                "metric").subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Consumer<WeatherForecastResult>()
                {
                    @Override
                    public void accept(WeatherForecastResult weatherForecastResult) throws Exception
                    {
                        //Load image
                        Picasso.get().load(IconWeatherProvider.getImageIcon(weatherForecastResult.list.get(0).weather.get(0).getDescription().toUpperCase())).into(ivWeatherIcon);

                        //Load information
                        tvCityAndCountryName.setText(weatherForecastResult.city.name + ", " + weatherForecastResult.city.country);
                        tvWeatherDescription.setText(weatherForecastResult.list.get(0).weather.get(0).getDescription());
                        tvCurrentTemp.setText(weatherForecastResult.list.get(0).main.getTemp() + "°C");
                        updated_at.setText(Common.convertUnixToDate(weatherForecastResult.list.get(0).dt));
                        tvMaxTemp.setText(weatherForecastResult.list.get(0).main.getTemp_max() + "°C");
                        tvMinTemp.setText(weatherForecastResult.list.get(0).main.getTemp_min() + "°C");

                        //Display panel
                        weather_panel.setVisibility(View.VISIBLE);
                        loading.setVisibility(View.GONE);
                    }
                }, new Consumer<Throwable>()
                {
                    @Override
                    public void accept(Throwable throwable) throws Exception
                    {
                        Toast.makeText(getActivity(), "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("msg", "" + throwable.getMessage());
                    }
                }));
    }

    @Override
    public void onStop()
    {
        compositeDisposable.clear();
        super.onStop();
    }
}
