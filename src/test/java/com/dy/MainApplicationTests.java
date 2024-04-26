package com.dy;

import javax.annotation.Resource;

import com.dy.common.ChartStatus;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 主类测试
 *

 */
@SpringBootTest
class MainApplicationTests {


    @Test
    void contextLoads() {
        System.out.println(ChartStatus.RUNNING.getStatus());
    }

}
