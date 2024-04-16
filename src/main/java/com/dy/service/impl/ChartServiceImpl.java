package com.dy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dy.model.entity.Chart;
import com.dy.service.ChartService;
import com.dy.mapper.ChartMapper;
import org.springframework.stereotype.Service;

/**
* @author 微光
* @description 针对表【chart(图表信息表)】的数据库操作Service实现
* @createDate 2024-04-16 20:26:47
*/
@Service
public class ChartServiceImpl extends ServiceImpl<ChartMapper, Chart>
    implements ChartService {

}




