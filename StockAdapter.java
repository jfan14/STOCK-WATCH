package com.junfan.stockwatch;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class StockAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private List<Stock> stockList;
    private MainActivity mainAct;

    public StockAdapter(List<Stock> new_stockList, MainActivity ma){
        this.stockList = new_stockList;
        mainAct = ma;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stock_list_row, parent, false);
        itemView.setOnClickListener(mainAct);
        itemView.setOnLongClickListener(mainAct);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Stock stock = stockList.get(position);
        holder.stockSymbol.setText(stock.getStockSymbol());
        holder.companyName.setText(stock.getCompanyName());
        holder.price.setText(Double.toString(stock.getPrice()));
        //holder.priceChange.setText(Double.toString(stock.getPriceChange()));
        holder.changePercent.setText("(" + stock.getChangePercent() + ")");
        if(stock.getPriceChange() > 0){
            holder.priceChange.setText("▲ " + String.valueOf(stock.getPriceChange()));
            holder.stockSymbol.setTextColor(Color.GREEN);
            holder.companyName.setTextColor(Color.GREEN);
            holder.price.setTextColor(Color.GREEN);
            holder.changePercent.setTextColor(Color.GREEN);
            holder.priceChange.setTextColor(Color.GREEN);
        }else{
            holder.priceChange.setText("▼ " + String.valueOf(stock.getPriceChange()));
            holder.stockSymbol.setTextColor(Color.RED);
            holder.companyName.setTextColor(Color.RED);
            holder.price.setTextColor(Color.RED);
            holder.changePercent.setTextColor(Color.RED);
            holder.priceChange.setTextColor(Color.RED);
        }
    }

    public int getItemCount(){
        return stockList.size();
    }
}
