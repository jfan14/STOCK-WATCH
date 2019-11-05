package com.junfan.stockwatch;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView stockSymbol;
        public TextView companyName;
        public TextView price;
        public TextView priceChange;
        public TextView changePercent;

        public MyViewHolder(View view){
            super(view);
            stockSymbol = view.findViewById(R.id.stockSymbol);
            companyName = view.findViewById(R.id.companyName);
            price = view.findViewById(R.id.price);
            priceChange = view.findViewById(R.id.priceChange);
            changePercent = view.findViewById(R.id.changePercent);
        }
}
