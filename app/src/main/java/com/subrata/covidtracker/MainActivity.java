package com.subrata.covidtracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.subrata.covidtracker.api.ApiUtilities;
import com.subrata.covidtracker.api.CountryData;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity
        extends AppCompatActivity
        implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private TextView totalConfirm, totalActive, totalRecovered, totalDeath, totalTests, critical,
                todayConfirm, todayRecovery, todayDeath,
                activePercent, criticalPercent, deathPercent, recoverPercent,
                errorType, errorMessage, refreshButton,
                country, lastUpdateTime, population, continent, location;
    private MaterialCardView  errorDetailCard, coronaDetailCard;
    private ImageView countryFlag;
    private PieChart pieChart;
    private SwipeRefreshLayout swipeRefresh;
    private List<CountryData> cList;
    private String countryName;
    private LinearLayout countryDetailCard;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initializing the variables
        cList = new ArrayList<>();
        countryName = "India";
        init();
        onRefresh();

        // setting onclick listeners
        swipeRefresh.setOnRefreshListener(this);
        country.setOnClickListener(this);
        refreshButton.setOnClickListener(this);
    }

    @SuppressLint("CutPasteId")
    public void init(){
        totalConfirm = findViewById(R.id.total_confirm);
        totalActive = findViewById(R.id.total_active);
        totalRecovered = findViewById(R.id.total_recovered);
        totalDeath = findViewById(R.id.total_death);
        totalTests = findViewById(R.id.total_tests);
        critical = findViewById(R.id.critical);

        todayConfirm = findViewById(R.id.today_confirm);
        todayRecovery = findViewById(R.id.today_recovered);
        todayDeath = findViewById(R.id.today_death);

        activePercent = findViewById(R.id.active_percent);
        criticalPercent = findViewById(R.id.critical_percent);
        recoverPercent = findViewById(R.id.recover_percent);
        deathPercent = findViewById(R.id.death_percent);

        pieChart = findViewById(R.id.pie_chart);
        country = findViewById(R.id.country_name);
        lastUpdateTime = findViewById(R.id.last_update_time);
        swipeRefresh = findViewById(R.id.swipe_refresh);

        population = findViewById(R.id.population);
        continent = findViewById(R.id.continent);
        location = findViewById(R.id.location);
        countryFlag = findViewById(R.id.country_flag);

        countryDetailCard = findViewById(R.id.country_detail_card);
        coronaDetailCard = findViewById(R.id.corona_detail_card);
        errorDetailCard = findViewById(R.id.error_detail_card);
        errorType = findViewById(R.id.error_type);
        errorMessage = findViewById(R.id.error_message);
        refreshButton = findViewById(R.id.refresh_button);

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.country_name:
                Intent intent = new Intent(MainActivity.this,CountryList.class);
                startActivityForResult(intent, ExtraThings.getRequestCode());
                break;
            case R.id.refresh_button:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) onRefresh();
                break;
            default:
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onRefresh() {
        ApiUtilities.getApiInterface().getCountryData()
                .enqueue(new Callback<List<CountryData>>() {
                    @SuppressLint({"SimpleDateFormat", "SetTextI18n"})
                    @Override
                    public void onResponse(@NonNull Call<List<CountryData>> call, @NonNull Response<List<CountryData>> response) {
                        if(!cList.isEmpty()) cList.clear();
                        assert response.body() != null;
                        cList.addAll(response.body());
                        for(CountryData cd : cList){
                            if(cd.getCountry().equals(countryName)){
                                pieChart.setVisibility(View.VISIBLE);
                                countryDetailCard.setVisibility(View.VISIBLE);
                                coronaDetailCard.setVisibility(View.VISIBLE);
                                errorDetailCard.setVisibility(View.GONE);

                                lastUpdateTime.setText(new SimpleDateFormat("dd-MM-YYYY HH:mm:ss a").format(new Date(Long.parseLong(cd.getUpdated()))).toString());
                                int confirm = Integer.parseInt(cd.getCases());
                                int active = Integer.parseInt(cd.getActive());
                                int recovered = Integer.parseInt(cd.getRecovered());
                                int death = Integer.parseInt(cd.getDeaths());

                                country.setText(countryName);

                                totalActive.setText(NumberFormat.getInstance().format(active));
                                totalConfirm.setText(NumberFormat.getInstance().format(confirm));
                                totalRecovered.setText(NumberFormat.getInstance().format(recovered));
                                totalDeath.setText(NumberFormat.getInstance().format(death));
                                totalTests.setText(NumberFormat.getInstance().format(Integer.parseInt(cd.getTests())));
                                critical.setText(NumberFormat.getInstance().format(Integer.parseInt(cd.getCritical())));

                                todayDeath.setText(NumberFormat.getInstance().format(Integer.parseInt(cd.getTodayDeaths())));
                                todayConfirm.setText(NumberFormat.getInstance().format(Integer.parseInt(cd.getTodayCases())));
                                todayRecovery.setText(NumberFormat.getInstance().format(Integer.parseInt(cd.getTodayRecovered())));

                                activePercent.setText(ExtraThings.getPercent(cd.getActive(),cd.getCases()));
                                recoverPercent.setText(ExtraThings.getPercent(cd.getRecovered(),cd.getCases()));
                                deathPercent.setText(ExtraThings.getPercent(cd.getDeaths(),cd.getCases()));
                                criticalPercent.setText(ExtraThings.getPercent(cd.getCritical(),cd.getCases()));

                                Glide.with(MainActivity.this)
                                        .load(cd.getCountryInfo().get("flag"))
                                        .into(countryFlag);
                                population.setText(NumberFormat.getInstance().format(Integer.parseInt(cd.getPopulation())));
                                continent.setText(cd.getContinent());
                                location.setText(cd.getCountryInfo().get("lat")+", "+cd.getCountryInfo().get("long"));


                                pieChart.clearChart();
//                                pieChart.addPieSlice(new PieModel("Confirm",confirm,getResources().getColor(R.color.confirm)));
                                pieChart.addPieSlice(new PieModel("Recovered",recovered,getResources().getColor(R.color.recovered)));
                                pieChart.addPieSlice(new PieModel("Active",active,getResources().getColor(R.color.active)));
                                pieChart.addPieSlice(new PieModel("Death",death, getResources().getColor(R.color.red)));
                                pieChart.startAnimation();
                                Toast.makeText(getBaseContext(), "Refreshed Successfully", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onFailure(@NonNull Call<List<CountryData>> call, @NonNull Throwable t) {
//                        pieChart.setVisibility(View.GONE);
//                        Snackbar.make(swipeRefresh, "Error : "+t.getMessage(), Snackbar.LENGTH_INDEFINITE)
//                                .setTextColor(getResources().getColor(R.color.orange))
//                                .setActionTextColor(getResources().getColor(R.color.orange))
//                                .setBackgroundTint(getResources().getColor(R.color.cardview_light_background))
//                                .setAction("Refresh", v ->{
//                                    onRefresh();
//                                })
//                                .show();

                        countryDetailCard.setVisibility(View.GONE);
                        coronaDetailCard.setVisibility(View.GONE);
                        errorDetailCard.setVisibility(View.VISIBLE);
                        errorType.setText(t.getMessage());
                        errorMessage.setText(R.string.internet_error);

                    }
                });
        swipeRefresh.setRefreshing(false);
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ExtraThings.getRequestCode() && resultCode == RESULT_OK) {
            countryName = data.getStringExtra(ExtraThings.getIntentDataKey());
//            Toast.makeText(this, countryName, Toast.LENGTH_SHORT).show();
            onRefresh();
        }
    }

    @Override
    public void onBackPressed() {
        if(ExtraThings.getTimeData()+1000 > System.currentTimeMillis()){
            super.onBackPressed();
        }else {
            ExtraThings.setTimeData(System.currentTimeMillis());
            Snackbar.make(swipeRefresh, getResources().getString(R.string.press_back_again), Snackbar.LENGTH_SHORT)
                    .setTextColor(getResources().getColor(R.color.recovered))
                    .setBackgroundTint(getResources().getColor(android.R.color.system_accent1_50))
                    .setActionTextColor(getResources().getColor(R.color.confirm))
                    .setAction("Exit", view -> { super.onBackPressed(); })
                    .show();
        }
    }
}