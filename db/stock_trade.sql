-- 创建数据库
create database stock_trade;

-- 使用数据库
use stock_trade;

-- 创建user表
CREATE TABLE user (
                      userId INT auto_increment PRIMARY KEY,
                      userName VARCHAR(16) NOT NULL,
                      password VARCHAR(16) NOT NULL,
                      balance DOUBLE,
                      cashBalance DOUBLE
);

-- 创建stock表
CREATE TABLE stock (
                       stockCode INT auto_increment PRIMARY KEY,
                       stockName VARCHAR(16) NOT NULL,
                       currentPrice DOUBLE NOT NULL,
                       increasePercentage DOUBLE,
                       volume INT NOT NULL,
                       marketCap DOUBLE NOT NULL,
                       Profit DOUBLE,
                       userName VARCHAR(16) NOT NULL
);

-- 创建transaction表
CREATE TABLE transaction (
                             transactionId INT auto_increment PRIMARY KEY,
                             transactionType VARCHAR(6),
                             stockName VARCHAR(16),
                             quantity INT NOT NULL,
                             transactionPrice DOUBLE NOT NULL,
                             transactionDate TIMESTAMP NOT NULL,
                             userName VARCHAR(16)
);

-- 创建ownedStocks表
CREATE TABLE ownedStocks (
                             id INT auto_increment PRIMARY KEY,
                             stockName VARCHAR(16),
                             userName VARCHAR(16)
);


-- 向表中插入初始数据
INSERT INTO stock_trade.ownedstocks (id, stockName, userName) VALUES (2, '2222', 'user1');
INSERT INTO stock_trade.ownedstocks (id, stockName, userName) VALUES (32, '3333', 'user2');
INSERT INTO stock_trade.ownedstocks (id, stockName, userName) VALUES (34, '1111', 'user1');
INSERT INTO stock_trade.ownedstocks (id, stockName, userName) VALUES (35, '3333', 'user1');
INSERT INTO stock_trade.ownedstocks (id, stockName, userName) VALUES (1, '4444', 'user2');
INSERT INTO stock_trade.ownedstocks (id, stockName, userName) VALUES (6, '5555', 'user2');
INSERT INTO stock_trade.ownedstocks (id, stockName, userName) VALUES (3, '6666', 'user3');
INSERT INTO stock_trade.ownedstocks (id, stockName, userName) VALUES (4, '7777', 'user3');
INSERT INTO stock_trade.ownedstocks (id, stockName, userName) VALUES (5, '8888', 'user3');

INSERT INTO stock_trade.ownedstocks (id, stockName, userName) VALUES (11, '9999', 'user2');
INSERT INTO stock_trade.ownedstocks (id, stockName, userName) VALUES (12, '1010', 'user2');
INSERT INTO stock_trade.ownedstocks (id, stockName, userName) VALUES (13, '1110', 'user2');
INSERT INTO stock_trade.ownedstocks (id, stockName, userName) VALUES (14, '1212', 'user2');
INSERT INTO stock_trade.ownedstocks (id, stockName, userName) VALUES (15, '1313', 'user2');
INSERT INTO stock_trade.ownedstocks (id, stockName, userName) VALUES (16, '1414', 'user2');
INSERT INTO stock_trade.ownedstocks (id, stockName, userName) VALUES (17, '1515', 'user2');
INSERT INTO stock_trade.ownedstocks (id, stockName, userName) VALUES (18, '1616', 'user2');
INSERT INTO stock_trade.ownedstocks (id, stockName, userName) VALUES (19, '1717', 'user2');
INSERT INTO stock_trade.ownedstocks (id, stockName, userName) VALUES (20, '1818', 'user2');


INSERT INTO stock_trade.stock (stockCode, stockName, currentPrice, increasePercentage, volume, marketCap, Profit, userName) VALUES (1, '1111', 503.95, 0.181, 100000, 50395000.00, 0, 'user1');
INSERT INTO stock_trade.stock (stockCode, stockName, currentPrice, increasePercentage, volume, marketCap, Profit, userName) VALUES (2, '2222', 496.50, 0.047, 100000, 49650000.00, 0, 'user1');
INSERT INTO stock_trade.stock (stockCode, stockName, currentPrice, increasePercentage, volume, marketCap, Profit, userName) VALUES (3, '3333', 480.86, 1.52, 3100000, 14906660.00, 0, 'user1');
INSERT INTO stock_trade.stock (stockCode, stockName, currentPrice, increasePercentage, volume, marketCap, Profit, userName) VALUES (4, '4444', 102.76, 0.123, 5100000, 52407600.00, 0, 'user2');
INSERT INTO stock_trade.stock (stockCode, stockName, currentPrice, increasePercentage, volume, marketCap, Profit, userName) VALUES (5, '5555', 854.90, 0.045, 220000, 18807800.00, 0, 'user2');
INSERT INTO stock_trade.stock (stockCode, stockName, currentPrice, increasePercentage, volume, marketCap, Profit, userName) VALUES (6, '6666', 678.120, 0.078, 4100000,  2780292000, 0, 'user3');
INSERT INTO stock_trade.stock (stockCode, stockName, currentPrice, increasePercentage, volume, marketCap, Profit, userName) VALUES (7, '7777', 78.551, 0.056, 3100000, 243508100,0, 'user3');
INSERT INTO stock_trade.stock (stockCode, stockName, currentPrice, increasePercentage, volume, marketCap, Profit, userName) VALUES (8, '8888', 923.338, 0.032, 669200, 617803593.6, 0, 'user3');
INSERT INTO stock_trade.stock (stockCode, stockName, currentPrice, increasePercentage, volume, marketCap, Profit, userName) VALUES (9, '9999', 50.03, 0.050, 569200, 28482096, 0, 'user2');
INSERT INTO stock_trade.stock (stockCode, stockName, currentPrice, increasePercentage, volume, marketCap, Profit, userName) VALUES (10, '1010', 653.208, 0.060, 220000, 143705760.0, 0, 'user2');
INSERT INTO stock_trade.stock (stockCode, stockName, currentPrice, increasePercentage, volume, marketCap, Profit, userName) VALUES (11, '1110', 70.357, 0.035, 390000,  27439230.0, 0, 'user2');
INSERT INTO stock_trade.stock (stockCode, stockName, currentPrice, increasePercentage, volume, marketCap, Profit, userName) VALUES (12, '1212', 180.401, 0.040, 400100, 72180040.1, 0, 'user2');
INSERT INTO stock_trade.stock (stockCode, stockName, currentPrice, increasePercentage, volume, marketCap, Profit, userName) VALUES (13, '1313', 290.455, 0.045, 569200, 165249126.0, 0, 'user2');
INSERT INTO stock_trade.stock (stockCode, stockName, currentPrice, increasePercentage, volume, marketCap, Profit, userName) VALUES (14, '1414', 57.505, 0.050, 666000,  38294830.00, 0, 'user2');
INSERT INTO stock_trade.stock (stockCode, stockName, currentPrice, increasePercentage, volume, marketCap, Profit, userName) VALUES (15, '1515', 110.558, 0.055, 70000, 7739060.0, 0, 'user2');
INSERT INTO stock_trade.stock (stockCode, stockName, currentPrice, increasePercentage, volume, marketCap, Profit, userName) VALUES (16, '1616', 90.601, 0.060, 88000, 7972888.0, 0, 'user2');
INSERT INTO stock_trade.stock (stockCode, stockName, currentPrice, increasePercentage, volume, marketCap, Profit, userName) VALUES (17, '1717', 130.650, 0.065, 90000, 11758500.0, 0, 'user2');
INSERT INTO stock_trade.stock (stockCode, stockName, currentPrice, increasePercentage, volume, marketCap, Profit, userName) VALUES (18, '1818', 14.701, 0.070, 100000, 1470100.0, 0, 'user2');
INSERT INTO stock_trade.stock (stockCode, stockName, currentPrice, increasePercentage, volume, marketCap, Profit, userName) VALUES (19, '1919', 25.50, 0.055, 150000, 3825000.0, 0, 'user3');
INSERT INTO stock_trade.stock (stockCode, stockName, currentPrice, increasePercentage, volume, marketCap, Profit, userName) VALUES (20, '2020', 33.75, 0.040, 200000, 6750000.0, 0, 'user4');
INSERT INTO stock_trade.stock (stockCode, stockName, currentPrice, increasePercentage, volume, marketCap, Profit, userName) VALUES (21, '2121', 40.60, 0.030, 250000, 10150000.0, 0, 'user5');
INSERT INTO stock_trade.stock (stockCode, stockName, currentPrice, increasePercentage, volume, marketCap, Profit, userName) VALUES (22, '2222', 17.85, 0.065, 120000, 2142000.0, 0, 'user6');
INSERT INTO stock_trade.stock (stockCode, stockName, currentPrice, increasePercentage, volume, marketCap, Profit, userName) VALUES (23, '2323', 52.30, 0.050, 90000, 4707000.0, 0, 'user7');
INSERT INTO stock_trade.stock (stockCode, stockName, currentPrice, increasePercentage, volume, marketCap, Profit, userName) VALUES (24, '2424', 320.45, 0.035, 80000, 25636000.0, 0, 'user8');
INSERT INTO stock_trade.stock (stockCode, stockName, currentPrice, increasePercentage, volume, marketCap, Profit, userName) VALUES (25, '2525', 45.80, 0.040, 150000, 6870000.0, 0, 'user9');
INSERT INTO stock_trade.stock (stockCode, stockName, currentPrice, increasePercentage, volume, marketCap, Profit, userName) VALUES (26, '2626', 250.30, 0.025, 100000, 25030000.0, 0, 'user10');
INSERT INTO stock_trade.stock (stockCode, stockName, currentPrice, increasePercentage, volume, marketCap, Profit, userName) VALUES (27, '2727', 55.95, 0.050, 120000, 6714000.0, 0, 'user11');
INSERT INTO stock_trade.stock (stockCode, stockName, currentPrice, increasePercentage, volume, marketCap, Profit, userName) VALUES (28, '2828', 480.10, 0.030, 70000, 33607000.0, 0, 'user12');
INSERT INTO stock_trade.stock (stockCode, stockName, currentPrice, increasePercentage, volume, marketCap, Profit, userName) VALUES (29, '2929', 75.60, 0.045, 85000, 6426000.0, 0, 'user13');


INSERT INTO stock_trade.transaction (transactionId, transactionType, stockName, quantity, transactionPrice, transactionDate, userName) VALUES (1, 'buy', '3333', 22, 107.56, '2023-11-29 10:08:21', 'user1');
INSERT INTO stock_trade.transaction (transactionId, transactionType, stockName, quantity, transactionPrice, transactionDate, userName) VALUES (2, 'buy', '3333', 1, 471.80, '2023-11-29 10:10:19', 'user2');
INSERT INTO stock_trade.transaction (transactionId, transactionType, stockName, quantity, transactionPrice, transactionDate, userName) VALUES (3, 'buy', '1111', 3, 146.30, '2023-11-29 10:12:28', 'user1');
INSERT INTO stock_trade.transaction (transactionId, transactionType, stockName, quantity, transactionPrice, transactionDate, userName) VALUES (4, 'buy', '1111', 3, 146.47, '2023-11-29 10:14:57', 'user1');
INSERT INTO stock_trade.transaction (transactionId, transactionType, stockName, quantity, transactionPrice, transactionDate, userName) VALUES (5, 'buy', '3333', 1, 368.54, '2023-11-29 10:16:10', 'user1');
INSERT INTO stock_trade.transaction (transactionId, transactionType, stockName, quantity, transactionPrice, transactionDate, userName) VALUES (6, 'sell', '3333', 22, 102.53, '2023-11-29 10:08:46', 'user1');
INSERT INTO stock_trade.transaction (transactionId, transactionType, stockName, quantity, transactionPrice, transactionDate, userName) VALUES (7, 'sell', '1111', 4, 194.41, '2023-11-29 10:14:16', 'user1');
INSERT INTO stock_trade.transaction (transactionId, transactionType, stockName, quantity, transactionPrice, transactionDate, userName) VALUES (8, 'buy', '4444', 100, 102.47, '2023-11-30 09:30:00', 'user3');
INSERT INTO stock_trade.transaction (transactionId, transactionType, stockName, quantity, transactionPrice, transactionDate, userName) VALUES (9, 'sell', '4444', 50, 102.00, '2023-11-30 09:45:00', 'user3');
INSERT INTO stock_trade.transaction (transactionId, transactionType, stockName, quantity, transactionPrice, transactionDate, userName) VALUES (10, 'buy', '5555', 200, 85.43, '2023-11-30 10:00:00', 'user4');
INSERT INTO stock_trade.transaction (transactionId, transactionType, stockName, quantity, transactionPrice, transactionDate, userName) VALUES (11, 'sell', '5555', 150, 86.00, '2023-11-30 10:30:00', 'user4');
INSERT INTO stock_trade.transaction (transactionId, transactionType, stockName, quantity, transactionPrice, transactionDate, userName) VALUES (12, 'buy', '6666', 300, 67.89, '2023-11-30 11:00:00', 'user5');
INSERT INTO stock_trade.transaction (transactionId, transactionType, stockName, quantity, transactionPrice, transactionDate, userName) VALUES (13, 'sell', '6666', 100, 68.50, '2023-11-30 11:15:00', 'user5');
INSERT INTO stock_trade.transaction (transactionId, transactionType, stockName, quantity, transactionPrice, transactionDate, userName) VALUES (14, 'buy', '7777', 250, 78.90, '2023-11-30 11:30:00', 'user6');
INSERT INTO stock_trade.transaction (transactionId, transactionType, stockName, quantity, transactionPrice, transactionDate, userName) VALUES (15, 'sell', '7777', 120, 79.00, '2023-11-30 12:00:00', 'user6');
INSERT INTO stock_trade.transaction (transactionId, transactionType, stockName, quantity, transactionPrice, transactionDate, userName) VALUES (16, 'buy', '8888', 400, 92.34, '2023-11-30 12:30:00', 'user7');
INSERT INTO stock_trade.transaction (transactionId, transactionType, stockName, quantity, transactionPrice, transactionDate, userName) VALUES (17, 'sell', '8888', 200, 93.00, '2023-11-30 13:00:00', 'user7');


INSERT INTO stock_trade.user (userId, userName, password, balance, cashBalance) VALUES (1, 'user1', 'user1', 500000.00, 1000679.46);
INSERT INTO stock_trade.user (userId, userName, password, balance, cashBalance) VALUES (2, 'user2', 'user2', 5000.00, 9751.38);
INSERT INTO stock_trade.user (userId, userName, password, balance, cashBalance) VALUES (3, 'user3', 'user3', 50000.00, 100000.25);
INSERT INTO stock_trade.user (userId, userName, password, balance, cashBalance) VALUES (4, '1234', '1234', 6666.50, 6666.75);
INSERT INTO stock_trade.user (userId, userName, password, balance, cashBalance) VALUES (5, 'user4', 'user4', 10000.00, 20000.20);
INSERT INTO stock_trade.user (userId, userName, password, balance, cashBalance) VALUES (6, 'user5', 'user5', 15000.00, 25000.35);
INSERT INTO stock_trade.user (userId, userName, password, balance, cashBalance) VALUES (7, 'user6', 'user6', 20000.00, 30000.40);
INSERT INTO stock_trade.user (userId, userName, password, balance, cashBalance) VALUES (8, 'user7', 'user7', 25000.00, 35000.55);
INSERT INTO stock_trade.user (userId, userName, password, balance, cashBalance) VALUES (9, 'user8', 'user8', 30000.00, 40000.50);
INSERT INTO stock_trade.user (userId, userName, password, balance, cashBalance) VALUES (10, 'user9', 'user9', 35000.00, 45000.60);
INSERT INTO stock_trade.user (userId, userName, password, balance, cashBalance) VALUES (11, 'user10', 'user10', 40000.00, 50000.70);
INSERT INTO stock_trade.user (userId, userName, password, balance, cashBalance) VALUES (12, 'user11', 'user11', 45000.00, 55000.80);
INSERT INTO stock_trade.user (userId, userName, password, balance, cashBalance) VALUES (13, 'user12', 'user12', 50000.00, 60000.90);
INSERT INTO stock_trade.user (userId, userName, password, balance, cashBalance) VALUES (14, 'user13', 'user13', 55000.00, 65000.00);