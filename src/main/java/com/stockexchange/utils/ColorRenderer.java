package com.stockexchange.utils;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.Color;

/**
 * Package: com.stockexchange.utils
 * ClassName: ColorRenderer
 * Description 修改窗口涨跌幅颜色
 *
 * @author
 * @Create 2023/11/30/ 21:09
 * @Version 1.0
 */

public class ColorRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        // 获取涨跌幅的值
        double increasePercentage = Double.parseDouble(value.toString().replace("%", ""));


        // 根据涨跌幅的正负值设置字体颜色
        if (increasePercentage > 0) {
            cellComponent.setForeground(Color.RED);
        } else if (increasePercentage < 0) {
            cellComponent.setForeground(Color.GREEN);
        }else{
            cellComponent.setForeground(Color.YELLOW);
        }

        return cellComponent;
    }
}
