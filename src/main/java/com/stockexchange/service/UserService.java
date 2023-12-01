package com.stockexchange.service;

import com.stockexchange.mapper.StockMapper;
import com.stockexchange.mapper.TransactionMapper;
import com.stockexchange.mapper.UserMapper;
import com.stockexchange.pojo.OwnedStock;
import com.stockexchange.pojo.Stock;
import com.stockexchange.pojo.Transaction;
import com.stockexchange.pojo.User;
import com.stockexchange.view.MainView;
import com.stockexchange.view.StockView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Package: com.stockexchange.service
 * ClassName: UserService
 * Description 用户服务
 *
 * @author
 * @Create 2023/11/17/ 20:41
 * @Version 1.0
 */
@Service
public class UserService {
    // 创建持久层对象
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private StockMapper stockMapper;
    @Autowired
    private TransactionMapper transactionMapper;
    @Autowired
    private StockService stockService;
    @Autowired
    private StockView stockView;

    /**
     * 功能：用户登录
     *      调用UserMapper中的findByUserName()来根据用户名查找用户：
     *      如果查找到的user值为null则返回0（用户不存在）
     *      如果查找到的user.getPassword和传入的passowrd值相同，则返回1（登录成功）
     *      如果查找到的user.getPassword和传入的passowrd值不相同，则返回2（密码错误）
     */
    public int userLogin(String userName, String password) {
        // 根据用户名查询用户
        User user = userMapper.findByUserName(userName);

        if (user == null) {
            return 0; // 用户不存在
        }
        if (user.getPassword().equals(password)) {
            return 1; // 登录成功
        }

        return 2; // 密码错误
    }

    /**
     * 功能：用户注册
     * 先将balance（用户余额）和cashBalanc(总资产)设为0
     * 然后调用UserMapper中的regedit()将用户名，密码，用户余额，总资产添加到数据库
     *
     * @return
     */
    public int regedit(String userName, String password, double balance) {
        User user = userMapper.findByUserName(userName);

        if (user != null) {
            return 0; // 注册失败
        }

//        String md5String = Md5Util.getMD5String(password); // 密码加密
        String md5String = password;
        double cashBalance = balance;

        userMapper.regedit(userName, md5String, balance, cashBalance); // 调用持久层的对应方法，将用户信息添加到数据库

        return 1; // 注册成功
    }

    /**
     * 功能：修改用户信息
     *      先调用UserMapper中的findByUserName()来根据用户名查找用户：
     *      如果查找到的user值为null则返回0（用户不存在）
     *      如果查找到的user值不为null则
     *          先执行user.setUserName(newUserName)，user.setPassowrd(newPassword)
     *          再调用UserMapper中的updateUserInfo()来修改用户名称和密码并返回1（修改成功）
     */
    public int updateUserInfo(String userName, String newUserName, String newPassword) {
        User user = userMapper.findByUserName(userName);

        if(user == null){
            return 0;
        }else{
            user.setUserName(newUserName);
            user.setPassword(newPassword);
            userMapper.updateUserInfo(user);

            return 1;
        }
    }

    public int updateUserInfo(Integer userId, String newUserName, String newPassword) {
        User user = userMapper.findByUserId(userId);

        if(user == null){
            return 0;
        }else{
            user.setUserName(newUserName);
            user.setPassword(newPassword);
            userMapper.updateUserInfoById(user);

            return 1;
        }
    }

    /**
     * 功能：查看用户持有的股票
     *      先调用UserMapper中的findByUserName()来根据用户名查找用户：
     *      如果查找到的user值为null则返回null（用户不存在）
     *      如果查找到的user值不为null则调用StockMapper中的userStockList()来修查看用户持有的股票并返回1（查找成功）
     */
    public List<OwnedStock> userStockList(String userName) {
        User user = userMapper.findByUserName(userName);

        if(user == null){
            return null;
        }else {
            List<OwnedStock> stockList = stockMapper.userStockList(userName);
            return stockList;
        }
    }

    /**
     * 功能：买入股票
     *      先调用UserMapper中的findByUserName()来根据用户名查找用户
     *      再调用StockMapper中的findStockByName()来根据股票名查找股票：
     *      如果查找到的stock值为null则返回0（股票不存在）
     *      如果查找到的stock值不为null则将查到stock的信息添加到user.ownedStocks（用户股票列表）中
     *      使stock.volume（股票持有量）+1，根据stock.getCurrentPrice()（当前股价）减少user.balance（现金）
     *      返回1（买入成功）
     */
    public int buyStock(String userName, String stockName, int volume) {
        User user = userMapper.findByUserName(userName);
        Stock stock = stockMapper.findStockByName(stockName);

        if(user == null || stock == null){
            return 0;
        }

        // 检查是否已持有股票
//        List<OwnedStock> ownedStock = stockMapper.userStockList(userName);
//        for (OwnedStock own : ownedStock) {
//            if (own.getStockName().equals(stockName)) {
//                System.out.println(own.getStockName());
//                return 0;
//            }
//        }

        userMapper.addOwnedStock(userName, stockName); // 向用户股票列表中添加股票

        // 更新股票信息
        stock.setVolume(stock.getVolume() + volume); // 增加股票持有量
        stockMapper.updateStockInfo(stock);

        // 更新用户信息
        user.setBalance(user.getBalance() - (stock.getCurrentPrice()) * volume); // 根据当前股价减少用户现金
        userMapper.updateUserInfo(user);

        // 添加交易记录
        Transaction transaction = new Transaction("buy", stockName, volume, stock.getCurrentPrice() * volume, userName);
        transactionMapper.addTransaction(transaction);

        // 更新股票和用户信息窗口
        List<Stock> stocks = stockService.stockList();
        stockView.updateTableAndUserInfo(stocks, user);

        return 1;
    }

    /**功能：
     *  通过传入用户名和充值金额
     *  先调用findByUserName匹配用户是否存在
     *  currentBalance获取没更新前的余额，加上amount更新newBalance
     *  传回newBalance
     *
     *  若用户不存在则打印“用户不存在”
     * @param userName
     * @param amount
     */
    public void updateUserFunds(String userName, double amount) {
        // 通过userName获取用户对象
        User user = userMapper.findByUserName(userName);
        if (user != null) {
            // 获取当前资金并增加充值金额
            double currentBalance = user.getBalance();
            double newBalance = currentBalance + amount;

            // 更新用户的资金余额
            user.setBalance(newBalance);

            // 将更新后的用户信息保存到数据库
            userMapper.updateUserInfo(user);
        } else {
            // 可以处理用户不存在的情况，例如打印一条消息
            System.out.println("用户不存在！");
        }
    }

    /**
     * 功能：卖出股票
     *      先调用UserMapper中的findByUserName()来根据用户名查找用户
     *      再调用StockMapper中的findStockByName()来根据股票名查找股票：
     *      如果查找到的stock值为null则返回0（股票不存在）
     *      如果查找到的stock值不为null则将stock的信息从user.ownedStocks（用户股票列表）中移除
     *      使stock.volume（股票持有量）-1，根据stock.currentPrice（当前股价）增加user.balance（现金）
     *      返回1（买入成功）
     */
    public  int sellStock(String userName, String stockName, int volume) {
        User user = userMapper.findByUserName(userName);
        Stock stock = stockMapper.findStockByName(stockName);

        if (user == null || stock == null) {
            return 0; // 卖出失败
        }

        userMapper.deleteOwnedStock(userName, stockName); // 从用户股票列表中移除股票

        // 更新股票信息
        stock.setVolume(stock.getVolume() - volume); // 股票持有量减1
        stockMapper.updateStockInfo(stock);

        // 更新用户信息
        user.setBalance(user.getBalance() + (stock.getCurrentPrice()) * volume); // 根据当前股价增加用户现金
        userMapper.updateUserInfo(user);

        // 添加交易记录
        Transaction transaction = new Transaction("sell", stockName, volume, stock.getCurrentPrice() * volume, userName);
        transactionMapper.addTransaction(transaction);

        // 更新股票和用户信息窗口
        List<Stock> stocks = stockService.stockList();
        stockView.updateTableAndUserInfo(stocks, user);

        return 1; // 卖出成功

    }

}
