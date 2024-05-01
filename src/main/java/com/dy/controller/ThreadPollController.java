package com.dy.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dy.annotation.AuthCheck;
import com.dy.common.BaseResponse;
import com.dy.common.DeleteRequest;
import com.dy.common.ErrorCode;
import com.dy.common.ResultUtils;
import com.dy.constant.UserConstant;
import com.dy.exception.BusinessException;
import com.dy.exception.ThrowUtils;
import com.dy.manager.AIManager;
import com.dy.manager.CosManager;
import com.dy.manager.RedissonManager;
import com.dy.model.dto.chart.*;
import com.dy.model.entity.Chart;
import com.dy.model.entity.User;
import com.dy.model.vo.BiResponseVO;
import com.dy.service.ChartService;
import com.dy.service.UserService;
import com.dy.utils.ExcelUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

import static com.dy.constant.ChartConstant.GEN_CHART_BY_AI;

/**
 * 帖子接口
 */
@RestController
@RequestMapping("/thread")
@Slf4j
@Profile({"dev", "local"})
public class ThreadPollController {

    //  注入线程池对象
    @Resource
    private ThreadPoolExecutor threadPoolExecutor;

    @GetMapping("/add")
    public void add(String name) {

        CompletableFuture.runAsync(() ->{
            log.info("执行任务中: " + name + ".执行人: " + Thread.currentThread().getName());

            try {
                Thread.sleep(600000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }, threadPoolExecutor);



    }

    @GetMapping("/get")
    public String get() {
        Map<String, Object> map = new HashMap<>();
        int size = threadPoolExecutor.getQueue().size();
        map.put("队列长度", size);
        long taskCount = threadPoolExecutor.getTaskCount();
        map.put("任务总数", taskCount);
        long completedTaskCount = threadPoolExecutor.getCompletedTaskCount();
        map.put("已完成任务数", completedTaskCount);
        int activeCount = threadPoolExecutor.getActiveCount();
        map.put("正在工作的线程数", activeCount);
        return JSONUtil.toJsonStr(map);
    }



}
