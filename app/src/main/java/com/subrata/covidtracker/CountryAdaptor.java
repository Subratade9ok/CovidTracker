package com.subrata.covidtracker;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.subrata.covidtracker.api.CountryData;

import java.util.List;

public class CountryAdaptor extends RecyclerView.Adapter<CountryAdaptor.ViewHolder> {

    List<CountryData> cList;
    Context context;
    ListItemClicked listItemClicked;
    public CountryAdaptor(List<CountryData> cList, Context context) {
        this.cList = cList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.list_item_layout, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.list_sl_no.setText(String.valueOf(position+1));
        holder.list_country_name.setText(cList.get(position).getCountry());
        holder.list_total_cases.setText(cList.get(position).getCases());
        holder.itemView.setTag(cList.get(position));
        Glide.with(context).load(cList.get(position).getCountryInfo().get("flag"))
                .into(holder.list_flag);
    }

    @Override
    public int getItemCount() {
        return cList.size();
    }

    interface ListItemClicked{
        public void onListItemClicked(View v);
    }
    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView list_sl_no, list_country_name, list_total_cases;
        private ImageView list_flag;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            listItemClicked = (ListItemClicked) context;
            list_sl_no = itemView.findViewById(R.id.list_sl_no);
            list_country_name = itemView.findViewById(R.id.list_country_name);
            list_total_cases = itemView.findViewById(R.id.list_total_cases);
            list_flag = itemView.findViewById(R.id.list_flag);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listItemClicked.onListItemClicked(view);
                }
            });
        }
    }
}
