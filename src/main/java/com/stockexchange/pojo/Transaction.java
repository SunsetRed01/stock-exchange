package com.stockexchange.pojo;

import java.time.LocalDateTime;

/**
 * Package: com.stockexchange.pojo
 * ClassName: Transaction
 * Description 交易类，用于记录买入卖出的交易信息
 *
 * @author
 * @Create 2023/11/17/ 23:52
 * @Version 1.0
 */
public class Transaction {
    private String transactionType; // 交易类型 0买入 1卖出
    private String stockName; // 交易所对应的股票
    private int quantity; // 交易数量

    public Transaction() {
    }

    private double transactionPrice; // 交易金额
    private LocalDateTime transactionDate; // 交易日期，这是日期时间类
    private String userName; // 交易用户


    public Transaction(String transactionType, String stockName, int quantity, double transactionPrice, String userName) {
        this.transactionType = transactionType;
        this.stockName = stockName;
        this.quantity = quantity;
        this.transactionPrice = transactionPrice;
        this.userName = userName;
    }

    // get和set方法
    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTransactionPrice() {
        return transactionPrice;
    }

    public void setTransactionPrice(double transactionPrice) {
        this.transactionPrice = transactionPrice;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
