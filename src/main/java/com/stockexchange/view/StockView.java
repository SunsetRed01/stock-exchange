package com.stockexchange.view;

import com.stockexchange.pojo.Stock;
import com.stockexchange.pojo.User;
import com.stockexchange.utils.ColorRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.text.DecimalFormat;
import java.util.List;

@Component
public class StockView extends JFrame {

    private List<Stock> stocks;
    private DefaultTableModel tableModel;
    private JTable table;
    private JLabel userInfoLabel; // 新增用户信息标签

    public void initializeComponents(User user) {
        // 初始化Swing组件
        String[] columnNames = {"股票名称", "当前价格", "涨跌幅", "持有数量", "总市值"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);

        // 设置布局管理器
        setLayout(new BorderLayout());

        // 将表格添加到窗口中
        add(new JScrollPane(table), BorderLayout.CENTER);

        // 将用户信息标签添加到窗口底部
        userInfoLabel = new JLabel("用户名: " + user.getUserName() +
                " | 余额: $" + formatDecimal(user.getBalance()) +
                " | 总资产: $" + formatDecimal(user.getCashBalance()));
        add(userInfoLabel, BorderLayout.SOUTH);
        Font labelFont = userInfoLabel.getFont();
        labelFont = labelFont.deriveFont(labelFont.getSize() * 1.5f);
        userInfoLabel.setFont(labelFont);
        add(userInfoLabel, BorderLayout.SOUTH);


        // 设置窗口大小和关闭操作
        setTitle("股票大厅");
        setSize(1600, 1000);
        setLocationRelativeTo(null); // 居中显示
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        // 将窗体设置为常驻窗口
        setAlwaysOnTop(true);

        // 设置背景颜色为黑色
        table.setBackground(Color.BLACK);
        table.setForeground(Color.WHITE);

        // 设置涨跌幅字体颜色为红绿两色
        table.getColumnModel().getColumn(2).setCellRenderer(new ColorRenderer());

        // 设置表格中的字体颜色为偏灰白色（RGB值为220, 220, 220）
        Color grayWhite = new Color(220, 220, 220);
        table.setForeground(grayWhite);


        // 添加窗口缩放事件监听器
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // 根据窗口大小调整表格行高和字体大小
                int rowHeight = (int) (getHeight() * 0.06);
                table.setRowHeight(rowHeight);

                Font font = table.getFont();
                font = font.deriveFont((float) (rowHeight * 0.5));
                table.setFont(font);

                // 同时调整表头字体大小
                JTableHeader header = table.getTableHeader();
                header.setFont(font);
            }
        });

        // 为涨跌幅列设置渲染器
        TableColumn increasePercentageColumn = table.getColumnModel().getColumn(2);
        increasePercentageColumn.setCellRenderer(new ColorRenderer());

    }


    // 将股票信息添加到表格
    private void addStockToTable(Stock stock) {
        Object[] rowData = {
                stock.getStockName(),
                formatDecimal(stock.getCurrentPrice()),
                formatDecimal(stock.getIncreasePercentage()) + "%",
                stock.getVolume(),
                "$" + formatDecimal(stock.getMarketCap())
        };
        tableModel.addRow(rowData);
    }


    // 格式化小数，保留两位小数
    private String formatDecimal(double value) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return decimalFormat.format(value);
    }

    // 更新表格
    public void updateTable(List<Stock> updatedStocks) {
        this.stocks = updatedStocks;
        // 清空表格
        tableModel.setRowCount(0);
        // 添加股票信息到表格
        for (Stock stock : stocks) {
            addStockToTable(stock);
        }
    }

    // 更新用户信息标签
    public void updateUserInfo(User updatedUser) {
        userInfoLabel.setText("用户名: " + updatedUser.getUserName() +
                " | 余额: $" + formatDecimal(updatedUser.getBalance()) +
                " | 总资产: $" + formatDecimal(updatedUser.getCashBalance()));
    }

    // 同时更新表格和用户信息标签
    public void updateTableAndUserInfo(List<Stock> updatedStocks, User updatedUser) {
        updateTable(updatedStocks);
        updateUserInfo(updatedUser);
    }
}