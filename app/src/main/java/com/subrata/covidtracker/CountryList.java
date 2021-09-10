package com.subrata.covidtracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.subrata.covidtracker.api.ApiUtilities;
import com.subrata.covidtracker.api.CountryData;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CountryList extends AppCompatActivity
        implements CountryAdaptor.ListItemClicked {

    private RecyclerView recyclerView;
    private CountryAdaptor adaptor;
    private List<CountryData> cList;
    private AppCompatEditText searchCountry;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_list);

        cList = new ArrayList<>();
        recyclerView = findViewById(R.id.country_list);
        searchCountry = findViewById(R.id.country_search);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.hasFixedSize();
        adaptor = new CountryAdaptor(cList, this);
        recyclerView.setAdapter(adaptor);

        refreshData();
        searchCountry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(charSequence.toString().isEmpty()){
                   cList.clear();
                   cList.addAll(ExtraThings.getcList());
                   adaptor.notifyDataSetChanged();
                }
                else {
//                Toast.makeText(CountryList.this,    charSequence.toString(), Toast.LENGTH_SHORT).show();

                    List<CountryData> nList = new ArrayList<>();
                    for (CountryData d : ExtraThings.getcList())
                        if (d.getCountry().toLowerCase().startsWith(charSequence.toString().toLowerCase()))
                            nList.add(d);
                    cList.clear();
                    cList.addAll(nList);
                    adaptor.notifyDataSetChanged();
                }

            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }
    private void refreshData(){
        ApiUtilities.getApiInterface().getCountryData()
                .enqueue(new Callback<List<CountryData>>() {
                    @Override
                    public void onResponse(Call<List<CountryData>> call, Response<List<CountryData>> response) {
                        if(!cList.isEmpty()) cList.clear();
                        assert response.body() != null;
                        cList.addAll(response.body());
                        ExtraThings.setcList(response.body());
                        adaptor.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<List<CountryData>> call, Throwable t) {
                        Snackbar.make(findViewById(R.id.country_activity),t.getMessage(),Snackbar.LENGTH_INDEFINITE)
                                .setTextColor(getResources().getColor(R.color.orange))
                                .setBackgroundTint(getResources().getColor(R.color.white))
                                .setActionTextColor(getResources().getColor(R.color.red))
                                .setAction("Back", view -> {
                                    onBackPressed();
                                })
                                .show();
                    }
                });

    }
    @Override
    public void onListItemClicked(View v) {
        CountryData  d = (CountryData) v.getTag();
        String country = d.getCountry();
        Intent intent = new Intent();
        intent.putExtra(ExtraThings.getIntentDataKey(),country);
        setResult(RESULT_OK,intent);
        finish();
    }
}