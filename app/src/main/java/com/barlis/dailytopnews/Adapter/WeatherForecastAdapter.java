package com.barlis.dailytopnews.Adapter;

import android.app.AlarmManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.barlis.dailytopnews.Common.Common;
import com.barlis.dailytopnews.IconWeatherProvider;
import com.barlis.dailytopnews.Model.WeatherForecastResult;
import com.barlis.dailytopnews.R;
import com.squareup.picasso.Picasso;

public class WeatherForecastAdapter extends RecyclerView.Adapter<WeatherForecastAdapter.MyViewHolder>
{
    Context context;
    WeatherForecastResult weatherForecastResult;

    public WeatherForecastAdapter(Context context, WeatherForecastResult weatherForecastResult)
    {
        this.context = context;
        this.weatherForecastResult = weatherForecastResult;
    }

    public WeatherForecastAdapter()
    {

    }

    public WeatherForecastResult getWeatherForecastResult() {
        return weatherForecastResult;
    }

    public void setWeatherForecastResult(WeatherForecastResult weatherForecastResult)
    {
        this.weatherForecastResult = weatherForecastResult;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(context).inflate(R.layout.weather_foecast_cell, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {
        //Load icon
        Picasso.get().load(IconWeatherProvider.getImageIcon(weatherForecastResult.list.get(position).weather.get(0).getDescription().toUpperCase())).into(holder.img_weather);

        holder.txt_city_name.setText(new StringBuilder(weatherForecastResult.city.name) + ", " + weatherForecastResult.city.country);
        holder.txt_date_time.setText(new StringBuilder(Common.convertUnixToDate(weatherForecastResult.list.get(position).dt)));
        holder.txt_description.setText(new StringBuilder(weatherForecastResult.list.get(position).weather.get(0).getDescription()));
        holder.txt_temperature.setText(new StringBuilder(String.valueOf(weatherForecastResult.list.get(position).main.getTemp())).append("°C"));
        holder.txt_temp_max.setText(new StringBuilder(String.valueOf(weatherForecastResult.list.get(position).main.getTemp_max())).append("°C"));
        holder.txt_temp_min.setText(new StringBuilder(String.valueOf(weatherForecastResult.list.get(position).main.getTemp_min())).append("°C"));
    }

    @Override
    public int getItemCount()
    {
        return weatherForecastResult.list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView txt_date_time, txt_description, txt_temperature, txt_city_name, txt_temp_max, txt_temp_min;
        ImageView img_weather;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);

            img_weather = itemView.findViewById(R.id.img_weather);

            txt_city_name = itemView.findViewById(R.id.txt_city_name);
            txt_temp_max = itemView.findViewById(R.id.txt_temp_max);
            txt_temp_min = itemView.findViewById(R.id.txt_temp_min);
            txt_date_time = itemView.findViewById(R.id.txt_date_time);
            txt_description = itemView.findViewById(R.id.txt_description);
            txt_temperature = itemView.findViewById(R.id.txt_temperature);
        }
    }
}
