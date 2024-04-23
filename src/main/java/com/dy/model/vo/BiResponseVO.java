package com.dy.model.vo;

import lombok.Data;

/**
 * @Author: dy
 * @Date: 2024/4/22 18:50
 * @Description: 返回的图表视图
 */
@Data
public class BiResponseVO {

    private Long chartId;

    private String genResult;

    private String genChart;
}
