package com.stockexchange.mapper;

import com.stockexchange.pojo.OwnedStock;
import com.stockexchange.pojo.Stock;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Package: com.stockexchange.mapper
 * ClassName: StockMapper
 * Description 股票数据操作
 *
 * @author 成果
 * @Create 2023/11/17/ 20:47
 * @Version 1.0
 */
@Mapper
public interface StockMapper {
    // 添加股票
    @Insert("insert into stock_trade.stock(stockName, currentPrice, increasePercentage, volume, marketCap, Profit, userName)" +
            " values(#{stockName}, #{currentPrice}, #{increasePercentage}, #{volume}, #{marketCap}, #{Profit}, #{userName})")
    void addStock(Stock stock);

    // 根据股票名寻找股票
    @Select("select * from stock_trade.stock where stockName = #{stockName}")
    Stock findStockByName(String stockName);

    // 查看用户持有的股票
    @Select("select * from stock_trade.ownedstocks where userName = #{userName}")
    List<OwnedStock> userStockList(String userName);

    // 查看用户持有的股票是否重复
    @Select("select * from stock_trade.ownedstocks where stockName = #{stockName}")
    List<OwnedStock> findOwnedStock(String stockName);

    // 根据股票名删除股票
    @Delete("delete from stock_trade.stock where stockName = #{stockName}")
    void deleteStock(String stockName);

    // 根据股票名修改股票信息
    @Update("update stock_trade.stock set stockName = #{stockName}, currentPrice = #{currentPrice}, increasePercentage = #{increasePercentage}, volume = #{volume}, marketCap = #{marketCap}, Profit = #{Profit} where stockName = #{stockName}")
    void updateStockInfo(Stock stock);

    // 查询股票列表
    @Select("select * from stock_trade.stock")
    List<Stock> stockList();
}
