package com.barlis.dailytopnews.Model;

public class Main
{
    private double temp;
    private double temp_min;
    private double temp_max;

    public Main()
    {

    }

    public int getTemp() {
        int t = (int) temp;
        return t;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public int getTemp_min()
    {
        int t = (int) temp_min;
        return t;
    }

    public void setTemp_min(double temp_min) {
        this.temp_min = temp_min;
    }

    public int getTemp_max()
    {
        int t = (int) temp_max;
        return t;
    }

    public void setTemp_max(double temp_max) {
        this.temp_max = temp_max;
    }
}
