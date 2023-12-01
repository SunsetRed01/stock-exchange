package com.stockexchange.pojo;

/**
 * Package: com.stockexchange.pojo
 * ClassName: Stock
 * Description 股票类
 *
 * @author
 * @Create 2023/11/17/ 20:46
 * @Version 1.0
 */
public class Stock {
    private Integer stockCode; // 股票代码
    private String stockName; // 股票名称
    private double currentPrice; // 当前股价
    private double increasePercentage; // 涨跌幅
    private int volume; // 持有量
    private double marketCap; // 总市值 = 持有量 * 当前股价
    private double Profit; // 收益率   表示股票盈亏
    private String userName; // 股票拥有者名字


    // 构造方法
    // 要输入的参数:股票名称，当前股价
    public Stock(String stockName, double currentPrice) {
        this.stockName = stockName;
        this.currentPrice = currentPrice;
    }

    public Stock() {
    }

    // get和set方法
    public Integer getStockCode() {
        return stockCode;
    }

    public void setStockCode(Integer stockCode) {
        this.stockCode = stockCode;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public double getIncreasePercentage() {
        return increasePercentage;
    }

    public void setIncreasePercentage(double increasePercentage) {
        this.increasePercentage = increasePercentage;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public double getMarketCap() {
        return marketCap;
    }

    public void setMarketCap(double marketCap) {
        this.marketCap = marketCap;
    }

    public double getProfit() {
        return Profit;
    }

    public void setProfit(double profit) {
        Profit = profit;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
