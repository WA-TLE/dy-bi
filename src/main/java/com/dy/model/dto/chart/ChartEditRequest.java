package com.dy.model.dto.chart;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 编辑请求
 *

 */
@Data
public class ChartEditRequest implements Serializable {


    /**
     * id
     */

    private Long id;
    /**
     * 分析目标
     */
    private String goal;

    /**
     * 图表名称
     */
    private String name;

    /**
     * 图表类型
     */
    private String chartType;



    private static final long serialVersionUID = 1L;
}