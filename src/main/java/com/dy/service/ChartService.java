package com.dy.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dy.model.dto.chart.ChartQueryRequest;
import com.dy.model.dto.user.UserQueryRequest;
import com.dy.model.entity.Chart;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dy.model.entity.User;

/**
* @author 微光
* @description 针对表【chart(图表信息表)】的数据库操作Service
* @createDate 2024-04-16 20:26:47
*/
public interface ChartService extends IService<Chart> {
    QueryWrapper<Chart> getQueryWrapper(ChartQueryRequest chartQueryRequest);
}
