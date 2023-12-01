package com.stockexchange.mapper;

import com.stockexchange.pojo.User;
import org.apache.ibatis.annotations.*;

/**
 * Package: com.stockexchange.mapper
 * ClassName: UserMapper
 * Description 用户数据操作
 *
 * @author 成果
 * @Create 2023/11/17/ 20:44
 * @Version 1.0
 */
@Mapper
public interface UserMapper {
    // 根据用户名查询用户
    @Select("select * from stock_trade.user where userName = #{userName}")
    User findByUserName(String userName);

    // 向数据库添加用户
    // TODO 股票列表？？？
    @Insert("insert into stock_trade.user(userName, password, balance, cashBalance)" +
            " values(#{userName}, #{password}, #{balance}, #{cashBalance}) ")
    void regedit(String userName, String password, double balance, double cashBalance);

    // 通过userId查询用户信息，返回一个User类型对象
    @Select("select * from stock_trade.user where userId = #{id}")
    User findByUserId(Integer userId);

    // 通过用户名更新用户信息
    @Update("update stock_trade.user set userName = #{userName}, password = #{password}, balance = #{balance}, cashBalance = #{cashBalance} where userName = #{userName}")
    void updateUserInfo(User user);

    // 通过用户id更新用户信息
    @Update("update stock_trade.user set userName = #{userName}, password = #{password}, balance = #{balance}, cashBalance = #{cashBalance} where userId = #{userId}")
    void updateUserInfoById(User user);

    // 根据用户名删除用户
    @Delete("delete from stock_trade.user where userName = #{userName}")
    void deleteUser(String userName);

    // 向拥有的股票列表添加股票
    @Insert("insert into stock_trade.ownedstocks(stockName, userName)" +
            " values(#{stockName}, #{userName})")
    void addOwnedStock(String userName, String stockName);

    // 向拥有的股票列表移除股票
    @Delete("delete from stock_trade.ownedstocks where stockName = #{stockName} && userName = #{userName}")
    void deleteOwnedStock(String userName, String stockName);

}
