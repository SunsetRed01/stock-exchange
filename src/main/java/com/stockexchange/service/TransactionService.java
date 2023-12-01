package com.stockexchange.service;

import com.stockexchange.mapper.TransactionMapper;
import com.stockexchange.pojo.Transaction;
import com.stockexchange.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Package: com.stockexchange.service
 * ClassName: TransactionService
 * Description 交易服务
 *
 * @author
 * @Create 2023/11/18/ 00:43
 * @Version 1.0
 */
@Service
public class TransactionService {
    @Autowired
    private TransactionMapper transactionMapper;

    /**
     * 功能：查找用户交易信息
     *      调用TransactionMapper中的userTransaction()来根据用户id查找交易记录：
     *      如果查找到的transaction集合内容为null则返回0（记录为空）
     *      如果查找到的transaction集合内容不为null则返回此集合
     */
//    public List<Transaction> userTransaction(String userName) {
//        return ;
//    }

    /**
     * 功能：查找全部交易信息
     *      调用TransactionMapper中的allTransaction()来查找交易记录：
     *      如果查找到的transaction集合内容为null则返回0（记录为空）
     *      如果查找到的transaction集合内容不为null则返回此集合
     */
    public List<Transaction> userTransaction(String userName) {
        List<Transaction> transactionList = transactionMapper.userTransaction(userName);

        if(transactionList == null || transactionList.isEmpty()){
            return null;
        }
        return transactionList;
    }

    public List<Transaction> allTransaction() {
        List<Transaction> transactionList = transactionMapper.allTransaction();

        if(transactionList == null || transactionList.isEmpty()){
            return null;
        }
        return transactionList;
    }
}
