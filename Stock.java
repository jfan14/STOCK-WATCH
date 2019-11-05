package com.junfan.stockwatch;

public class Stock {
    private String stockSymbol;
    private String companyName;
    private double price;
    private double priceChange;
    private String changePercent;

    public Stock(String new_stockSymbol, String new_companyName, double new_price, double new_priceChange, String new_changePercent){
        setStockSymbol(new_stockSymbol);
        setCompanyName(new_companyName);
        setPrice(new_price);
        setPriceChange(new_priceChange);
        setChangePercent(new_changePercent);
    }

    public Stock(String new_stockSymbol, String new_companyName){
        setStockSymbol(new_stockSymbol);
        setCompanyName(new_companyName);
    }

    public void setStockSymbol(String stockSymbol){
        this.stockSymbol = stockSymbol;
    }

    public void setCompanyName(String companyName){
        this.companyName = companyName;
    }

    public void setPrice(double price){
        this.price = price;
    }

    public void setPriceChange(double priceChange){
        this.priceChange = priceChange;
    }

    public void setChangePercent(String changePercent){
        this.changePercent = changePercent;
    }

    public String getStockSymbol(){
        return this.stockSymbol;
    }

    public String getCompanyName(){
        return this.companyName;
    }

    public double getPrice(){
        return this.price;
    }

    public double getPriceChange(){
        return this.priceChange;
    }

    public String getChangePercent(){
        return this.changePercent;
    }

    public String toString(){
        return stockSymbol + " " + companyName + " " + price + " " + priceChange + " " + changePercent;
    }
}
