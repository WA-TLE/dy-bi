package com.dy.controller;

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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 帖子接口
 */
@RestController
@RequestMapping("/chart")
@Slf4j
public class ChartController {

    @Resource
    private ChartService chartService;

    @Resource
    private UserService userService;

    @Resource
    private CosManager cosManager;

    @Resource
    private AIManager aiManager;


    // region 增删改查

    /**
     * 创建
     *
     * @param chartAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addChart(@RequestBody ChartAddRequest chartAddRequest, HttpServletRequest request) {
        if (chartAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Chart chart = new Chart();
        BeanUtils.copyProperties(chartAddRequest, chart);


        // TODO: 2024/4/16 校验图表

        User loginUser = userService.getLoginUser(request);
        chart.setUserId(loginUser.getId());

        boolean result = chartService.save(chart);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newChartId = chart.getId();
        return ResultUtils.success(newChartId);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteChart(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        Chart oldChart = chartService.getById(id);
        ThrowUtils.throwIf(oldChart == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldChart.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = chartService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新（仅管理员）
     *
     * @param chartUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateChart(@RequestBody ChartUpdateRequest chartUpdateRequest) {
        if (chartUpdateRequest == null || chartUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Chart chart = new Chart();
        BeanUtils.copyProperties(chartUpdateRequest, chart);


        // todo 参数校验
//        chartService.validChart(chart, false);
        long id = chartUpdateRequest.getId();
        // 判断是否存在
        Chart oldChart = chartService.getById(id);
        ThrowUtils.throwIf(oldChart == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = chartService.updateById(chart);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<Chart> getChartVOById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Chart chart = chartService.getById(id);
        if (chart == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(chart);
    }

    /*
     */

    /**
     * 分页获取列表（仅管理员）
     *
     * @param chartQueryRequest
     * @return
     */

    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<Chart>> listChartByPage(@RequestBody ChartQueryRequest chartQueryRequest) {
        long current = chartQueryRequest.getCurrent();
        long size = chartQueryRequest.getPageSize();
        Page<Chart> chartPage = chartService.page(new Page<>(current, size),
                chartService.getQueryWrapper(chartQueryRequest));
        return ResultUtils.success(chartPage);
    }


    // endregion


    /**
     * 编辑（用户）
     *
     * @param chartEditRequest
     * @param request
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editChart(@RequestBody ChartEditRequest chartEditRequest, HttpServletRequest request) {
        if (chartEditRequest == null || chartEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Chart chart = new Chart();
        BeanUtils.copyProperties(chartEditRequest, chart);

        // 参数校验
//        chartService.validChart(chart, false);/**/
        User loginUser = userService.getLoginUser(request);
        long id = chartEditRequest.getId();
        // 判断是否存在
        Chart oldChart = chartService.getById(id);
        ThrowUtils.throwIf(oldChart == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        if (!oldChart.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = chartService.updateById(chart);
        return ResultUtils.success(result);
    }


    /**
     * 文件上传
     *
     * @param multipartFile
     * @param chartFileRequest
     * @param request
     * @return
     */
    @PostMapping("/upload")
    public BaseResponse<BiResponseVO> uploadFile(@RequestPart("file") MultipartFile multipartFile,
                                                 ChartFileRequest chartFileRequest, HttpServletRequest request) {

        //  快速判断 Object 对象是否为 null
        if (multipartFile == null || chartFileRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "上传文件为空");
        }

        User loginUser = userService.getLoginUser(request);


        String name = chartFileRequest.getName();
        String goal = chartFileRequest.getGoal();
        String chartType = chartFileRequest.getChartType();

        //  分析目标为 null
        ThrowUtils.throwIf(StringUtils.isBlank(goal), ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(StringUtils.isNoneBlank(name) && name.length() > 100, ErrorCode.PARAMS_ERROR, "名称过长");

        StringBuilder userInput = new StringBuilder();

        userInput.append("分析需求:").append(goal).append("\n");

        // TODO: 2024/4/22 补充图表类型

        //   将 Excel 文件转换为 csv
        String csv = ExcelUtils.excelToCsv(multipartFile);
        userInput.append("原始数据: ").append(csv).append("\n");

        long biModelId = 1782312022953074689L;

        String result = aiManager.doChat(biModelId, userInput.toString());

        String[] split = result.split("@@@@@@");

        ThrowUtils.throwIf(split.length < 3, ErrorCode.SYSTEM_ERROR, "图表数据分析错误");

        String genChart = split[1].trim();
        String genResult = split[2].trim();




        //  将生成的图表保存到数据库
        Chart chart = new Chart();
        chart.setGoal(goal);
        chart.setName(name);
        chart.setChartData(csv);
        chart.setChartType("折线图");  // TODO: 2024/4/22 图表类型更改
        chart.setGenChart(genChart);
        chart.setGenResult(genResult);
        chart.setUserId(loginUser.getId());

        boolean saveResult = chartService.save(chart);
        ThrowUtils.throwIf(!saveResult, ErrorCode.SYSTEM_ERROR, "图表保存失败");

        BiResponseVO biResponseVO = new BiResponseVO();
        biResponseVO.setChartId(chart.getId());
        biResponseVO.setGenResult(genResult);
        biResponseVO.setGenChart(genChart);

        return ResultUtils.success(biResponseVO);
    }
}
