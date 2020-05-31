package com.barlis.dailytopnews.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.barlis.dailytopnews.API.ApiNewsClient;
import com.barlis.dailytopnews.Adapter.NewsAdapter;
import com.barlis.dailytopnews.Model.Articles;
import com.barlis.dailytopnews.Model.Headlines;
import com.barlis.dailytopnews.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SportsFragment extends Fragment
{
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private final String API_KEY = "YOUR_API_HERE";
    private NewsAdapter adapter;
    private List<Articles> articles = new ArrayList<>();
    private View rootView;
    private String category = "sports", country;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_sports, container, false);

        swipeRefreshLayout = rootView.findViewById(R.id.swipeRefresh);
        recyclerView = rootView.findViewById(R.id.recyclerView);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        final String country = getCountry();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                retrieveJson(category, country, API_KEY);
            }
        });

        retrieveJson(category, country, API_KEY);

        return rootView;
    }

    private void retrieveJson(String category, String country, String apiKey)
    {
        swipeRefreshLayout.setRefreshing(true);
        Call<Headlines> call;

        if(!category.equals(""))
        {
            call = ApiNewsClient.getInstance().getApi().getCategories(country, category, apiKey);
        }
        else
        {
            call = ApiNewsClient.getInstance().getApi().getHeadlines(country, apiKey);
        }

        call.enqueue(new Callback<Headlines>()
        {
            @Override
            public void onResponse(Call<Headlines> call, Response<Headlines> response)
            {
                if (response.isSuccessful() && response.body().getArticles() != null)
                {
                    if(!articles.isEmpty())
                    {
                        articles.clear();
                    }

                    articles = response.body().getArticles();
                    adapter = new NewsAdapter(getContext(), articles);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<Headlines> call, Throwable t)
            {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getCountry()
    {
        Locale locale = Locale.getDefault();
//        String country = locale.getCountry();
        country = "il";
        return country.toLowerCase();
    }
}
