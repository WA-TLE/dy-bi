package com.dy.model.dto.chart;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 更新请求
 *

 */
@Data
public class ChartUpdateRequest implements Serializable {


    /**
     * id
     */

    private Long id;
    /**
     * 分析目标
     */
    private String goal;

    /**
     * 图表类型
     */
    private String chartType;

    /**
     * 生成分析结论
     */
    private String genChart;



    private static final long serialVersionUID = 1L;
}