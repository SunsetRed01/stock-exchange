package com.stockexchange;

import com.stockexchange.view.MainView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Package: com.stockexchange
 * ClassName: Start
 * Description
 *
 * @author
 * @Create 2023/11/21/ 00:49
 * @Version 1.0
 */
@Component
public class AutoStart implements CommandLineRunner {

    @Autowired
    public MainView view;

    // 重写run方法，以便springboot项目启动时自动执行menuView方法
    @Override
    public void run(String... args) throws Exception {
        view.menuView();
    }
}
