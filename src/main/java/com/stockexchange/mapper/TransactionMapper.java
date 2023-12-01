package com.stockexchange.mapper;

import com.stockexchange.pojo.Transaction;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Package: com.stockexchange.mapper
 * ClassName: TransactionMapper
 * Description 交易数据操作
 *
 * @author 成果
 * @Create 2023/11/18/ 00:42
 * @Version 1.0
 */
@Mapper
public interface TransactionMapper {
    // 根据用户名查找交易记录
    @Select("select * from stock_trade.transaction where userName = #{userName}")
    List<Transaction> userTransaction(String userName);

    // 查找全部交易信息
    @Select("select * from stock_trade.transaction")
    List<Transaction> allTransaction();

    // 添加交易记录
    @Insert("insert into stock_trade.transaction(transactionType, stockName, quantity, transactionPrice, transactionDate, userName)" +
            " values(#{transactionType}, #{stockName}, #{quantity}, #{transactionPrice}, now(), #{userName})")
    void addTransaction(Transaction transaction);
}
