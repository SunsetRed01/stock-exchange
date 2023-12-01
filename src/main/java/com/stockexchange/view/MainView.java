package com.stockexchange.view;

import com.stockexchange.mapper.StockMapper;
import com.stockexchange.mapper.UserMapper;
import com.stockexchange.pojo.OwnedStock;
import com.stockexchange.pojo.Stock;
import com.stockexchange.pojo.Transaction;
import com.stockexchange.pojo.User;
import com.stockexchange.service.AdminService;
import com.stockexchange.service.StockService;
import com.stockexchange.service.TransactionService;
import com.stockexchange.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static java.lang.System.exit;
import static com.stockexchange.utils.Color.*;

/**
 * Package: com.stockexchange.view
 * ClassName: View
 * Description 控制台界面
 *
 * @author
 * @Create 2023/11/18/ 17:27
 * @Version 1.0
 */
@Component
@SuppressWarnings({"all"})
public class MainView {
    // 创建业务层对象
    @Autowired
    private UserService userService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private StockService stockService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private StockMapper stockMapper;
    @Autowired
    private StockView stockView;

    private String userName;


    //-------------------------------------更新线程------------------------------------------
    // 用于后台股票更新的调度执行器
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> updateTask;

    // 启动或恢复更新线程
    public void startStockUpdateThread() {
        // 如果没有调度或已被取消，则创建新的调度任务
        if (updateTask == null || updateTask.isCancelled()) {
            updateTask = scheduler.scheduleAtFixedRate(this::updateStockInfoInBackground, 0, 5, TimeUnit.SECONDS);
        }
    }

    // 暂停更新线程
    public void stopStockUpdateThread() {
        // 取消定时任务，暂停更新
        if (updateTask != null && !updateTask.isCancelled()) {
            updateTask.cancel(true);
        }
    }

    // 股票更新方法
    private void updateStockInfoInBackground() {
        try {
            List<Stock> stocks = stockService.stockList();
            User user = userMapper.findByUserName(this.userName);

            List<OwnedStock> ownedStock = stockMapper.userStockList(userName);


            for (Stock stock : stocks) {
                // 生成随机价格变动
                Random random = new Random();

                //主要是修改了这里，对随机数的浮动范围
                double currentPrice = stock.getCurrentPrice();
                double percentageChange = 0.1; // 10%

                double maxChange = currentPrice * percentageChange; // 最大波动
                double change = (random.nextDouble() * maxChange * 2) - maxChange; // [-maxChange, maxChange] 范围内的随机变化

//                double change = (random.nextDouble() * 200) - 100; // 生成一个在范围 [-100, 100] 内的随机浮点数
//                double change = (random.nextDouble() * 100) - 50; // 生成一个在范围 [-100, 100] 内的随机浮点数

                // 计算当前价格
                double oldCurrentPrice = stock.getCurrentPrice();
                double newCurrentPrice = oldCurrentPrice + change;
                stock.setCurrentPrice(newCurrentPrice);
                // 计算涨跌幅
                double increasePercentage = (newCurrentPrice - oldCurrentPrice) / oldCurrentPrice * 100;
                stock.setIncreasePercentage(increasePercentage);
                // 计算总市值
                double marketCap = newCurrentPrice * stock.getVolume();
                stock.setMarketCap(marketCap);

                for (OwnedStock own : ownedStock) {
                    if (own.getStockName().equals(stock.getStockName())) {
                        // 计算用户资产
                        double cashBalance = user.getCashBalance() + (change * stock.getVolume());
                        user.setCashBalance(cashBalance);
                    }
                }
            }

            // 更新窗口
            stockView.updateTableAndUserInfo(stocks, user);

            // 将更新的内容保存到数据库
            saveUpdatedStocksToDatabase(stocks);
            saveUpdatedUserInfoToDatabase(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 将更新的股票信息保存到数据库
    private void saveUpdatedStocksToDatabase(List<Stock> updatedStocks) {
        for (Stock stock : updatedStocks) {
            // 调用相应的股票服务或DAO保存股票信息到数据库
            stockMapper.updateStockInfo(stock);
        }
    }

    // 将更新的用户信息保存到数据库
    private void saveUpdatedUserInfoToDatabase(User updatedUser) {
        // 调用相应的用户服务或DAO保存用户信息到数据库
        userMapper.updateUserInfo(updatedUser);
    }
    //-------------------------------------更新线程------------------------------------------

    //-------------------------------------主菜单------------------------------------------
    /**
     * 功能：展示选项
     * 登录: 调用方法loginView();
     * 注册：调用方法regeditView();
     * 退出: 调用exit(0);
     */
    public void menuView(){
        System.out.println(ANSI_BLUE + "═══════════════════════════════════" + ANSI_RESET);
        System.out.println(ANSI_BLUE + "              主菜单" + ANSI_RESET);
        System.out.println(ANSI_BLUE + "═══════════════════════════════════" + ANSI_RESET);
        System.out.println("\t 1. 用户登录");
        System.out.println("\t 2. 用户注册");
        System.out.println("\t 3. 管理员登录");
        System.out.println(ANSI_RED +   "\t 4. 退出" + ANSI_RESET);
        System.out.println(ANSI_BLUE + "═══════════════════════════════════" + ANSI_RESET);
        System.out.print("请选择操作（1-4）：");

        Scanner s = new Scanner(System.in);
        char choose = s.next().charAt(0);

        switch (choose) {
            case '1' :
                userLoginView();
                break;
            case '2' :
                regeditView();
                break;
            case '3' :
                adminLoginView();
                break;
            case '4':
                System.out.print("正在退出...");
                exit(0);
                break;
            default :
                System.out.println(ANSI_RED + "输入值不合法！" + ANSI_RESET);
                menuView();
        }
    }

    /**
     * 功能：用户登录
     *      提示用户输入账号和密码：
     *      调用UserService中的userLogin()方法进行校验
     *      如果账号和密码匹配则调用方法userView()
     *      不匹配则输出提示信息并重新调用本函数
     * 返回: 调用menuView();
     */
    public void userLoginView(){
        System.out.println("请输入账号和密码：");

        Scanner s = new Scanner(System.in);
        this.userName = s.nextLine();
        String password = s.nextLine();

        int isEqual = userService.userLogin(userName, password);

        // 判断是否登录成功
        if (isEqual == 0) {
            System.out.println(ANSI_RED + "用户不存在！" + ANSI_RESET);
            offerReturnToMenu();
        }
        if (isEqual == 1) {
            System.out.println(ANSI_GREEN + "登录成功！" + ANSI_RESET);

            // 调用股票和用户信息窗口
            try {
                List<Stock> stocks = stockService.stockList();
                User user = userMapper.findByUserName(this.userName);

                stockView.initializeComponents(user);
                stockView.updateTableAndUserInfo(stocks, user);

                // 启动更新器
                startStockUpdateThread();
            } catch (Exception e) {
                e.printStackTrace();
            }

            userView();

        }
        if (isEqual == 2) {
            System.out.println(ANSI_RED + "密码错误！" + ANSI_RESET);
            offerReturnToMenu();
        }
    }


    /**
     这是一个用于解决用户登录没有账号密码卡住新建的函数
     userLoginView中用户密码输入错误后会进入offerReturnToMenu方法
     ，有一次输入R返回menuView的机会
     */
    private void offerReturnToMenu() {
        System.out.println("按 'R' 返回主菜单，按其他键重新尝试登录：");
        Scanner s = new Scanner(System.in);
        String input = s.nextLine();

        if (input.equalsIgnoreCase("R")) {
            menuView();
        } else {
            userLoginView();
        }
    }

    /**
     * 功能：管理员登录
     *      提示输入账号和密码：
     *      调用AdminService中的adminLogin()方法进行校验
     *      如果账号和密码匹配则调用方法adminView()
     *      不匹配则输出提示信息并重新调用本函数
     * 返回: 调用menuView();
     */
    public void adminLoginView(){
        System.out.println("请输入账号和密码：");

        Scanner s = new Scanner(System.in);
        this.userName = s.nextLine();
        String password = s.nextLine();

        int isEqual = adminService.adminLogin(userName, password);

        // 判断是否登录成功
        if (isEqual == 0) {
            System.out.println(ANSI_RED + "账号或密码错误！" + ANSI_RESET);
            adminLoginView();
        }
        if (isEqual == 1) {
            System.out.println(ANSI_GREEN + "登录成功！" + ANSI_GREEN);
            adminView();
        }
    }

    /**
     * 功能：用户注册
     *      提示用户输入用户名和密码：
     *      调用UserService中的regedit方法进行注册
     *      如果用户名不重复且输入合法则调用userView()
     *      否则输出提示信息并重新调用本函数
     * 返回: 调用menuView();
     */
    public void regeditView(){
        Scanner s = new Scanner(System.in);
        double balance = 0;

        System.out.print("请输入用户名：");
        this.userName = s.nextLine();
        System.out.print("请输入密码：");
        String password = s.nextLine();
        System.out.print("请输入初始资产（数字）：");
        balance = s.nextDouble();

        try {
            int isSuc = userService.regedit(this.userName, password, balance);

            if (isSuc == 1) {
                System.out.println(ANSI_GREEN + "注册成功！请自行登录..." + ANSI_RESET);
                userLoginView();
            } else {
                System.out.println(ANSI_RED + "用户名或密码错误！请重新操作" + ANSI_RESET);
                menuView();
            }
        } catch (Exception e) {
            System.out.println(ANSI_RED + "出现错误：" + e.getMessage() + ANSI_RESET);
            e.printStackTrace();
        } finally {
            menuView();
        }
    }
//-------------------------------------主菜单------------------------------------------


//-------------------------------------用户界面------------------------------------------
    /**
     * 功能：用户界面
     * 股票管理：选择后调用userStockManagerView()
     * 个人交易记录：选择后调用TransactionService中的userTransaction()打印个人的交易记录
     *              询问是否要输出日志文件：
     *              是则输出日志到txt文件
     *              否则调用userView()
     * 修改用户信息：选择后调用updateUserInfoView()
     * 股票和用户信息常驻：选择后调用StockView中的？？？
     * 登出: 调用menuView();
     */
    public void userView(){
        System.out.println(ANSI_GREEN + "═══════════════════════════════════" + ANSI_RESET);
        System.out.println(ANSI_GREEN + "              用户界面" + ANSI_RESET);
        System.out.println(ANSI_GREEN + "═══════════════════════════════════" + ANSI_RESET);
        System.out.println("\t 1. 股票管理");
        System.out.println("\t 2. 交易记录");
        System.out.println("\t 3. 修改用户个人信息");
        System.out.println("\t 4. 充值");
        System.out.println("\t 5. 股票大厅");
        System.out.println(ANSI_RED + "\t 6. 账户登出" + ANSI_RESET);
        System.out.println(ANSI_GREEN + "═══════════════════════════════════" + ANSI_RESET);
        System.out.print("请选择操作（1-6）：");

        Scanner scanner = new Scanner(System.in);
        char choose = scanner.next().charAt(0);
        scanner.nextLine();

        switch (choose) {
            case '1' : // 股票管理
                userStockManagerView();
                break;
            case '2' : // 交易记录
                try {
                    List<Transaction> reList = transactionService.userTransaction(userName);

                    if (reList != null && !reList.isEmpty()) {
                        System.out.println("+--------------+--------------+--------------+--------------+---------------------+---------------------+");
                        System.out.println("| 交易类型       | 交易用户       | 交易股票      | 交易数量       | 交易金额              | 交易日期             |");
                        System.out.println("+--------------+--------------+--------------+--------------+---------------------+---------------------+");

                        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

                        for (Transaction transaction : reList) {
                            String formattedDate = transaction.getTransactionDate().format(dateFormatter);

                            System.out.printf("| %-12s | %-12s | %-12s | %-12s | %-12s | %-20s |\n",
                                    transaction.getTransactionType(),
                                    transaction.getUserName(),
                                    transaction.getStockName(),
                                    transaction.getQuantity(),
                                    transaction.getTransactionPrice(),
                                    formattedDate);
                        }
                        System.out.println("+--------------+--------------+--------------+--------------+---------------------+---------------------+");
                    } else {
                        System.out.println(ANSI_RED + "没有交易记录！" + ANSI_RESET);
                    }

//                    System.out.println("是否需要打印日志文件(YES/NO)");
//                    String s = scanner.nextLine();
//
//                    if (s.equals("YES")) {
//                        LocalDate currentDate = LocalDate.now();
//                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd"); // 定义日期格式
//                        String nowDate = currentDate.format(formatter);
//
//                        String fileName = userName+"的交易信息"+nowDate +".txt";//重新定义文件名
//
//                        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
//                        for (Transaction transaction : reList) {
//                            writer.write(transaction.toString()); // 假设 toString() 方法返回适合的字符串表示
//                            writer.newLine();//换行
//                        }
//                    }
//                    if (s.equals("NO")) {
//                        System.out.println("已为您返回到功能界面");
//                        userView();
//                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    userView();
                }
                break;
            case '3' : // 修改用户信息
                updateUserInfoView();
                break;
            case '4':
                rechargeFunds();
                break;
            case '5':
                stockView.setVisible(true);

                userView();
                break;
            case '6':
                stockView.setVisible(false);
                stopStockUpdateThread(); // 停止更新器
                menuView();
                break;
            default :
                System.out.println(ANSI_RED + "输入值不合法！" + ANSI_RESET);
        }

    }

    /**
     * 功能： 充值用户余额 ， 通过调用新加方法updateUserFunds修改用户余额
     *         再调用getBalance 打印用户更新的余额
     */
    private void rechargeFunds() {
        System.out.print("请输入充值金额（美元）：");
        Scanner scanner = new Scanner(System.in);
        double amount = scanner.nextDouble();

        // 更新用户资金
        userService.updateUserFunds(this.userName, amount);

        // 使用 UserMapper 获取更新后的用户信息以显示新余额
        User updatedUser = userMapper.findByUserName(this.userName);
        if (updatedUser != null) {
            double newBalance = updatedUser.getBalance(); // 假设 User 类有一个 getBalance 方法
            System.out.println(ANSI_GREEN + "充值成功！当前余额：" + newBalance + ANSI_RESET); // 显示新余额
        } else {
            System.out.println(ANSI_RED + "无法获取用户信息，充值可能未成功。" + ANSI_RESET);
        }

        userView();
    }

    /**
     * 功能：修改用户信息界面
     *      提示用户重新输入用户信息：
     *      调用UserService中的updateUserInfo()修改用户信息
     *      如果修改成功则输出提示信息并调用userView()
     *      如果修改失败则输出提示信息并回退到上一界面
     */
    public void updateUserInfoView() {
        Scanner a = new Scanner(System.in);
        System.out.print("请重新输入新用户名：");
        String newUserName = a.nextLine();
        System.out.print("请重新输入新密码：");
        String newPassword = a.nextLine();

        try {
            User user = userMapper.findByUserName(this.userName);
            int isSuc = userService.updateUserInfo(user.getUserId(), newUserName, newPassword);

            if(isSuc == 1){
                System.out.println(ANSI_GREEN + "修改成功！请重新登录" + ANSI_RESET);
                userLoginView();
            }
            else{
                System.out.println(ANSI_RED + "修改失败！请重新修改" + ANSI_RED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            userView();
        }
    }

    /**
     * 功能：股票管理界面界面（用户）
     * 查看持有的股票：选择后选择后调用UserService中的userStockList()方法并重新调用本函数
     * 买入股票：选择后调用buyStockView()
     * 卖出股票：选择后调用sellStockView()
     * 添加股票：选择后调用addStockView()
     * 返回: 调用userView();
     */
    public void userStockManagerView() {
        System.out.println(ANSI_GREEN + "============== 股票管理 ==============" + ANSI_RESET);
        System.out.println("\t 1. 查看持有的股票");
        System.out.println("\t 2. 买入股票");
        System.out.println("\t 3. 卖出股票");
        System.out.println("\t 4. 添加股票");
        System.out.println(ANSI_RED + "\t 5. 返回" + ANSI_RESET);
        System.out.println(ANSI_GREEN + "====================================" + ANSI_RESET);
        System.out.print("请选择操作(1-5):");

        Scanner s = new Scanner(System.in);
        char choose = s.next().charAt(0);

        switch (choose) {
            case '1': {
                try {
                    List<OwnedStock> ownedStocks = userService.userStockList(userName);

                    if (ownedStocks != null && !ownedStocks.isEmpty()) {
                        System.out.println("+--------------+--------------+");
                        System.out.println("| 股票名称       | 用户名        |");
                        System.out.println("+--------------+--------------+");

                        for (OwnedStock ownedStock : ownedStocks) {
                            System.out.printf("| %-12s | %-12s |\n",
                                    ownedStock.getStockName(),
                                    ownedStock.getUserName());
                        }
                        System.out.println("+--------------+--------------+");
                    } else {
                        System.out.println(ANSI_RED + "未持有股票！" + ANSI_RESET);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    userStockManagerView();
                }
            }
            case '2': {
                buyStockView();
            }
            case '3': {
                sellStockView();
            }
            case '4': {
                addStockView();
            }
            case '5': {
                userView();
            }
        }
    }

    /**
     * 功能：买入股票界面
     *      提示用户输入要买入股票的id：
     *      调用UserService中的buyStock()方法查找股票
     *      如果买入成功则调用userStockManagerView()
     *      如果买入失败则输出提示信息并回退到上一界面
     */
    public void buyStockView() {
        Scanner s = new Scanner(System.in);
        System.out.print("请输入要买入股票的名称：");
        String stockName = s.nextLine();
        System.out.print("请输入要买入股票的数量：");
        int volume = s.nextInt();

        try {
            Stock stock = stockMapper.findStockByName(stockName);
            User user = userMapper.findByUserName(this.userName);

//            if (volume > stock.getVolume()) {
//                System.out.println(ANSI_RED + "数量过多！请重新操作" + ANSI_RESET);
//                userStockManagerView();
//            }

            if (user.getBalance() < 0 || user.getBalance() < stock.getCurrentPrice()) {
                System.out.println(ANSI_RED + "余额不足！" + ANSI_RESET);
                userStockManagerView();
            }

            int isSuc = userService.buyStock(this.userName, stockName, volume);

            if (isSuc == 0) {
                System.out.println(ANSI_RED + "买入失败！请重新进行操作" + ANSI_RESET);
            } else {
                System.out.println(ANSI_GREEN + "买入成功！" + ANSI_RESET);
            }
        } catch (Exception e) {
            System.out.println(ANSI_RED + "发生错误，无法完成买入操作" + ANSI_RESET);
            e.printStackTrace();
        } finally {
            userStockManagerView(); // 发生异常时返回股票管理界面
        }
    }

    /**
     * 功能：卖出股票界面
     *      提示用户输入要卖出股票的id：
     *      调用UserService中的sellStock()方法查找股票
     *      如果卖出成功则输出提示信息并调用userStockManagerView()
     *      如果卖出失败则输出提示信息并回退到上一界面
     */
    public void sellStockView() {
        Scanner s = new Scanner(System.in);
        System.out.print("请输入要卖出股票的名称：");
        String stockName = s.nextLine();
        System.out.print("请输入要卖出股票的数量：");
        int volume = s.nextInt();

        try {
            Stock stock = stockMapper.findStockByName(stockName);
            User user = userMapper.findByUserName(this.userName);

//            if (volume > stock.getVolume()) {
//                System.out.println(ANSI_RED + "数量过多！请重新操作" + ANSI_RESET);
//                userStockManagerView();
//            }

            int sellResult = userService.sellStock(this.userName, stockName, volume); // 调用UserService中的sellStock()方法卖出股票

            if (sellResult == 1) {
                System.out.println(ANSI_GREEN + "卖出成功！" + ANSI_RESET);
            } else {
                System.out.println(ANSI_RED + "卖出失败！请重新进行操作" + ANSI_RESET);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            userStockManagerView(); // 返回用户股票管理界面
        }
    }

    /**
     * 功能：添加股票界面
     *      提示用户输入股票相关信息：
     *      调用UserService中的addStock()方法添加股票
     *      如果添加成功则输出提示信息并调用userStockManagerView()
     *      如果添加失败则输出提示信息并回退到上一界面
     */
    public void addStockView() {
        // 提示输入股票相关信息
        Scanner s = new Scanner(System.in);
        System.out.print("请输入股票名：");
        String stockName = s.next();
        System.out.print("请输入当前股价：");
        Double currentPrice = s.nextDouble();
        System.out.print("请输入持有数量：");
        int quantity = s.nextInt();

        // 添加股票
        try {
            int isSuc = stockService.addStock(this.userName, stockName, currentPrice, quantity);

            // 检查股票是否添加成功
            if (isSuc == 1) {
                System.out.println(ANSI_GREEN + "股票添加成功！" + ANSI_RESET);
            } else {
                System.out.print(ANSI_RED + "股票添加失败！请重新进行操作" + ANSI_RESET);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            userStockManagerView();
        }
    }
//-------------------------------------用户界面------------------------------------------


//-------------------------------------管理员界面------------------------------------------
    /**
     * 功能：管理员界面
     * 用户管理：调用userManageView()方法
     * 股票管理：调用adminStockManageView()方法
     * 查看交易记录：调用TransactionService中的allTransaction()打印所有交易记录
     *              询问是否要输出日志文件：
     *              是则输出日志到txt文件
     *              否则调用adminView()
     * 股票和用户信息常驻：选择后调用StockView中的？？？
     * 登出: 调用menuView();
     */
    public void adminView(){
        System.out.println(ANSI_PURPLE + "═══════════════════════════════════" + ANSI_RESET);
        System.out.println(ANSI_PURPLE + "              管理员界面" + ANSI_RESET);
        System.out.println(ANSI_PURPLE + "═══════════════════════════════════" + ANSI_RESET);
        System.out.println("\t 1. 用户管理");
        System.out.println("\t 2. 删除股票");
        System.out.println("\t 3. 查看交易记录");
        System.out.println(ANSI_RED +   "\t 4. 退出" + ANSI_RESET);
        System.out.println(ANSI_PURPLE + "═══════════════════════════════════" + ANSI_RESET);
        System.out.print("请选择操作（1-4）：");

        Scanner s = new Scanner(System.in);
        char choose = s.next().charAt(0);

        switch (choose) {
            case '1':
                userManageView();
                break;
            case '2':
                adminStockManageView();
                break;
            case '3':
                try {
                    List<Transaction> reList = transactionService.allTransaction();

                    if (reList != null && !reList.isEmpty()) {
                        System.out.println("+--------------+--------------+--------------+--------------+---------------------+---------------------+");
                        System.out.println("| 交易类型       | 交易用户       | 交易股票      | 交易数量       | 交易金额              | 交易日期             |");
                        System.out.println("+--------------+--------------+--------------+--------------+---------------------+---------------------+");

                        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

                        for (Transaction transaction : reList) {
                            String formattedDate = transaction.getTransactionDate().format(dateFormatter);

                            System.out.printf("| %-12s | %-12s | %-12s | %-12s | %-12s | %-20s |\n",
                                    transaction.getTransactionType(),
                                    transaction.getUserName(),
                                    transaction.getStockName(),
                                    transaction.getQuantity(),
                                    transaction.getTransactionPrice(),
                                    formattedDate);
                        }
                        System.out.println("+--------------+--------------+--------------+--------------+---------------------+---------------------+");
                    } else {
                        System.out.println(ANSI_RED + "没有交易记录！" + ANSI_RESET);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    adminView();
                }
                break;
//            case '4':
//                try {
//                    List<Stock> stocks = stockService.stockList();
//                    User user = userMapper.findByUserName(this.userName);
//
//                    stockView.initializeComponents(user);
//                    stockView.updateTableAndUserInfo(stocks, user);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                } finally {
//                    userView();
//                }
//                break;
            case '4': // 返回
                menuView();
                break;
            default :
                System.out.println(ANSI_RED + "输入值不合法！" + ANSI_RESET);

        }
    }

    /**
     * 功能：管理股票
     * 删除股票：提示输入要删除的股票名称:
     *          调用AdminService中的deleteStock()方法删除股票
     *          删除成功则输出提示信息并调用stockManageView()
     *          失败则输出提示信息并重新调用本函数
     * 修改股票信息：提示输入要修改的股票id:
     *              调用AdminService中的updateStockInfo()方法修改股票信息
     *              修改成功则输出提示信息并调用stockManageView()
     *              失败则输出提示信息并重新调用本函数
     * 返回: 调用adminView();
     */
    public void adminStockManageView(){
        Scanner s = new Scanner(System.in);
        System.out.print("请输入要删除的股票名：");
        String stockName = s.nextLine();

        try {
            int isSuc = adminService.deleteStock(stockName);

            //检查股票是否添加成功
            if(isSuc == 1){
                System.out.println(ANSI_GREEN + "删除股票成功！" + ANSI_RESET);
            }else{
                System.out.println(ANSI_RED + "删除股票失败！请重新进行操作" + ANSI_RESET);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            adminView();
        }
    }

    /**
     * 功能：管理用户
     * 删除用户：提示输入要删除的用户名:
     *          调用AdminService中的deleteUser()方法删除用户
     *          删除成功则输出提示信息并调用stockManageView()
     *          失败则输出提示信息并重新调用本函数
     * 修改用户信息：提示输入要修改的用户id:
     *              调用UserService中的updateUserInfo()方法修改用户信息
     *              修改成功则输出提示信息并调用stockManageView()
     *              失败则输出提示信息并重新调用本函数
     * 返回: 调用adminView();
     */
    public void userManageView(){
        System.out.println(ANSI_PURPLE + "============== 用户管理 ==============" + ANSI_RESET);
        System.out.println("\t 1: 删除用户");
        System.out.println("\t 2: 修改用户信息");
        System.out.println(ANSI_RED + "\t 3: 返回" + ANSI_RESET);
        System.out.println(ANSI_PURPLE + "====================================" + ANSI_RESET);
        System.out.print("请选择操作(1-3):");

        Scanner s = new Scanner(System.in);
        char choose = s.next().charAt(0);

        switch (choose) {
            case '1':
                System.out.println("请输入要删除的用户名");
                String userName = s.next();

                try {
                    int isSuc = adminService.deleyeUser(userName);

                    if (isSuc == 0) {
                        System.out.println("删除失败！请重新操作");
                    } else {
                        System.out.println("用户删除成功！");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    userManageView();
                }
            case '2':
                System.out.println("请输入要修改的用户名字");
                String oldUserName = s.next();
                System.out.println("请输入新用户名");
                String newUserName= s.next();
                System.out.println("请输入新密码");
                String newPassword= s.next();

                try {
                    int isSuc = userService.updateUserInfo(oldUserName, newUserName, newPassword);

                    if (isSuc == 0){
                        System.out.println(ANSI_RED + "修改失败！请重新操作" + ANSI_RESET);
                    }
                    if (isSuc == 1){
                        System.out.println(ANSI_GREEN + "修改成功！" + ANSI_RESET);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    userManageView();
                }
            case '3':
                adminView();
            default:
                System.out.println(ANSI_RED + "输入不合法！请重新输入" + ANSI_RESET);

        }
    }
//-------------------------------------管理员界面------------------------------------------
}
