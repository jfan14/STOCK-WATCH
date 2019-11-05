package com.junfan.stockwatch;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

//import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static java.util.Collections.*;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener{

    private final List<Stock> stockList = new ArrayList<>();
    private RecyclerView recyclerView;
    private StockAdapter sAdapter;
    private SwipeRefreshLayout swiper;
    private DatabaseHandler databaseHandler;
    private MainActivity mainAct;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recyclerView = findViewById(R.id.recycler);
        sAdapter = new StockAdapter(stockList, this);
        recyclerView.setAdapter(sAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        swiper = findViewById(R.id.swipe);
        sAdapter.notifyDataSetChanged();
        databaseHandler = new DatabaseHandler(this);
        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(netCheck() == true) {
                    ArrayList<String[]> stocks = databaseHandler.loadStocks();
                    for(String[] tmp: stocks){
                        Stock s = new Stock(tmp[0], tmp[1]);
                        AsyncDataLoader adl = new AsyncDataLoader(MainActivity.this);
                        adl.execute(s);
                    }
                    sAdapter.notifyDataSetChanged();
                    swiper.setRefreshing(false);

                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("No Network Connection");
                    builder.setMessage("Stocks Cannot Be Updated Without A Network Connection");
                    AlertDialog alert = builder.create();
                    alert.show();
                    swiper.setRefreshing(false);
                }
            }
        });
        if (netCheck() == false) {
            return;
        }
        ArrayList<String[]> stocks = databaseHandler.loadStocks();

        for(String[] tmp: stocks){
            Stock s = new Stock(tmp[0], tmp[1]);
            AsyncDataLoader adl = new AsyncDataLoader(this);
            adl.execute(s);
        }


    }

    @Override
    public void onClick(View v){
        int pos = recyclerView.getChildLayoutPosition(v);
        Stock s = stockList.get(pos);
        String symbol = s.getStockSymbol();
        String url = "https://www.marketwatch.com/investing/stock/" + symbol;
        Intent browse = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browse);

    }

    @Override
    public boolean onLongClick(View v){
        final int pos = recyclerView.getChildLayoutPosition(v);
        Stock s = stockList.get(pos);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                databaseHandler.deleteStock(stockList.get(pos).getStockSymbol());
                stockList.remove(pos);
                sAdapter.notifyDataSetChanged();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Do Nothing
            }
        });
        builder.setMessage("Delete Stock Symbol " + stockList.get(pos).getStockSymbol() + "?");
        builder.setTitle("Deletion");
        builder.setIcon(R.drawable.delete);
        AlertDialog dialog = builder.create();
        dialog.show();
        return true;
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        //EditText input = findViewById(R.id.add_stock);
        switch(item.getItemId()){
            case R.id.add_stock:
                if(netCheck() == false) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Stocks cannot be added without network connection");
                    builder.setTitle("No Network Connection");
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else{
                    dialog();
                }
                return true;
            default:
                //Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
                return super.onOptionsItemSelected(item);
        }
    }

    public void dialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setGravity(Gravity.CENTER_HORIZONTAL);
        input.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        builder.setView(input);
        final String in = input.getText().toString();
        final AsyncSymbolLoader asl = new AsyncSymbolLoader(this);
        // asl.execute(in);
        Log.d(TAG, "dialog: " + "here here here");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                boolean inpu = false;
                for(Stock s : stockList){
                    if(input.getText().toString().equals(s.getStockSymbol())) {
                        inpu = true;

                    } /*else{
                        asl.execute(input.getText().toString());
                    }*/
                }
                if(inpu){
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Duplicate Stock");
                    builder.setMessage("Stock Symbol " + input.getText().toString() + " is already displayed");
                    builder.setIcon(R.drawable.warning);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else{
                    asl.execute(input.getText().toString());
                }


            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(MainActivity.this, "Canceled adding", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setMessage("Please enter a stock symbol");
        builder.setTitle("Stock Selection");
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /*public void doNetCheck(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm == null){
            Toast.makeText(this, "Cannot access ConnectivityManager", Toast.LENGTH_SHORT).show();
            return;
        }
        NetworkInfo info = cm.getActiveNetworkInfo();
        if(info != null && info.isConnected()){
            //Toast.makeText(this,"You are connected", Toast.LENGTH_SHORT).show();
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Stocks cannot be added without network connection");
            builder.setTitle("No Network Connection");
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }*/

    public void doRefresh(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm == null){
            Toast.makeText(this, "Cannot access ConnectivityManager", Toast.LENGTH_SHORT).show();
            return;
        }
        NetworkInfo info = cm.getActiveNetworkInfo();
        if(info != null && info.isConnected()){
            //Toast.makeText(this,"You are connected", Toast.LENGTH_SHORT).show();
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("You do not have Internet Connection currently");
            builder.setTitle("No Network Connection");
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        sAdapter.notifyDataSetChanged();
        swiper.setRefreshing(false);
    }

    public void updateData(Stock stock) {
        int i=0;
        for(Stock entry : stockList){
            if(entry.getStockSymbol().equals(stock.getStockSymbol())) break;
            i++;
        }
        stockList.remove(i);
        stockList.add(i,stock);
        sAdapter.notifyDataSetChanged();
    }

    public void addData(Stock stock){
        for(Stock entry: stockList){
            if(entry.getStockSymbol().equals(stock.getStockSymbol())) return;
        }
        stockList.add(stock);
        sort(stockList, new Comparator<Stock>() {
            @Override
            public int compare(Stock o1, Stock o2) {
                return o1.getStockSymbol().compareTo(o2.getStockSymbol());
            }
        });
        databaseHandler.addStock(stock);
        sAdapter.notifyDataSetChanged();
    }

    public boolean netCheck(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if(netInfo != null && netInfo.isConnectedOrConnecting()){
            return true;
        }else{
            return false;
        }
    }
}
