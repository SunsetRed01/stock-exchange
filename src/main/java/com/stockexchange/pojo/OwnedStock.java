package com.stockexchange.pojo;

/**
 * Package: com.stockexchange.pojo
 * ClassName: OwnedStock
 * Description 用户持有的股票
 *
 * @author
 * @Create 2023/11/27/ 17:53
 * @Version 1.0
 */
public class OwnedStock {
    Integer id;
    String userName;
    String stockName;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }
}
