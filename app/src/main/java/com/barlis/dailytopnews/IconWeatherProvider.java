package com.barlis.dailytopnews;

public class IconWeatherProvider
{
    public static int getImageIcon(String weatherDescription) {
        int weatherIcon;
        switch (weatherDescription) {
            case "CLEAR SKY":
                weatherIcon = R.mipmap.clear_sky;
                break;
            case "FEW CLOUDS":
                weatherIcon = R.mipmap.few_clouds;
                break;
            case "SCATTERED CLOUDS":
                weatherIcon = R.mipmap.few_clouds;
                break;
            case "BROKEN CLOUDS":
                weatherIcon = R.mipmap.few_clouds;
                break;
            case "SHOWER RAIN":
                weatherIcon = R.mipmap.shower_rain;
                break;
            case "RAIN":
                weatherIcon = R.mipmap.rain;
                break;
            case "THUNDERSTORM":
                weatherIcon = R.mipmap.thunderstorm;
                break;
            case "SNOW":
                weatherIcon = R.mipmap.snow;
                break;
            case "MIST":
                weatherIcon = R.mipmap.mist;
                break;
            default:
                weatherIcon = R.mipmap.ic_launcher;
        }
        return weatherIcon;
    }
}
