package com.stockexchange.service;

import com.stockexchange.mapper.StockMapper;
import com.stockexchange.mapper.UserMapper;
import com.stockexchange.pojo.Stock;
import com.stockexchange.pojo.Transaction;
import com.stockexchange.pojo.User;
import com.stockexchange.service.StockService;
import com.stockexchange.view.MainView;
import com.stockexchange.view.StockView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Package: com.stockexchange.service
 * ClassName: StockService
 * Description 股票服务
 *
 * @author
 * @Create 2023/11/17/ 20:46
 * @Version 1.0
 */
@Service
public class StockService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private StockMapper stockMapper;
    @Autowired
    private StockView stockView;


    /**
     * 功能：添加股票
     *      先调用UserMapper中的findByUserName()来根据用户名查找用户
     *      再调用StockMapper中的findStockByName()来根据股票名查找股票：
     *      如果查找到的stock值不为null则返回0（股票已存在）
     *      如果查找到的stock值为null则
     *          先使stock.volume（股票持有量）等于1，根据输入的currentPrice（当前股价）来设置stock.currentPrice并减少user.balance（现金）
     *          调用StockMapper中的addStock()将输入的股票信息添加到数据库
     *          返回1（添加成功）
     */
    public int addStock(String userName, String stockName, double currentPrice, int quantity) {
        User user = userMapper.findByUserName(userName); // 根据用户名在数据库中查找用户
        Stock stock = stockMapper.findStockByName(stockName); // 根据股票名在数据库中查找股票

        // 如果用户不存在或股票已存在
        if(user == null || stock != null){
            return 0;
        }

        // 创建新的股票并设置属性
        stock = new Stock(stockName, currentPrice);
        stock.setVolume(quantity); // 设置持有数量
        stock.setMarketCap(currentPrice * quantity); // 设置总市值


        user.setBalance(user.getBalance() - currentPrice); // 计算用户的余额
        stock.setUserName(userName);

        stockMapper.addStock(stock); // 添加股票到数据库
        userMapper.updateUserInfo(user); // 在数据库中更新用户余额
        userMapper.addOwnedStock(userName, stockName); // 向拥有的股票列表添加股票

        // 更新股票和用户信息窗口
        List<Stock> stocks = stockList();
        stockView.updateTableAndUserInfo(stocks, user);

        return 1;
    }

    public int updateStockInfo(String oldStockName, String newStockName) {
        // 调用 StockMapper 的 findStockByName 方法来查找股票
        Stock stock = stockMapper.findStockByName(oldStockName);

        // 检查是否找到了股票
        if (stock == null) {
            // 股票不存在
            return 0;
        } else {
            // 股票存在，更新股票信息
            stock.setStockName(newStockName); // 设置新的股票名
            stockMapper.updateStockInfo(stock); // 更新股票信息

            return 1; // 修改成功
        }
    }

    public List<Stock> stockList() {
        List<Stock> stockList = stockMapper.stockList();

        if(stockList == null || stockList.isEmpty()){
            return null;
        }
        return stockList;
    }

}
