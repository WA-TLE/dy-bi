package com.dy.service.impl;
import java.util.Date;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dy.common.ErrorCode;
import com.dy.constant.CommonConstant;
import com.dy.exception.BusinessException;
import com.dy.model.dto.chart.ChartQueryRequest;
import com.dy.model.dto.user.UserQueryRequest;
import com.dy.model.entity.Chart;
import com.dy.model.entity.User;
import com.dy.service.ChartService;
import com.dy.mapper.ChartMapper;
import com.dy.utils.SqlUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
* @author 微光
* @description 针对表【chart(图表信息表)】的数据库操作Service实现
* @createDate 2024-04-16 20:26:47
*/
@Service
public class ChartServiceImpl extends ServiceImpl<ChartMapper, Chart>
    implements ChartService {


    @Override
    public QueryWrapper<User> getQueryWrapper(ChartQueryRequest chartQueryRequest) {
        if (chartQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }

        Long id = chartQueryRequest.getId();
        String goal = chartQueryRequest.getGoal();
        String chartType = chartQueryRequest.getChartType();
        Long userId = chartQueryRequest.getUserId();
        String sortField = chartQueryRequest.getSortField();
        String sortOrder = chartQueryRequest.getSortOrder();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(id != null, "id", id);
        queryWrapper.eq(StringUtils.isNotBlank(goal), "userRole", goal);
        queryWrapper.like(StringUtils.isNotBlank(chartType), "userProfile", chartType);
        queryWrapper.like(ObjectUtils.isNotEmpty(userId), "userName", userId);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }
    

}




