package com.dy.mq;

import cn.hutool.core.util.StrUtil;
import com.dy.common.ChartStatus;
import com.dy.common.ErrorCode;
import com.dy.exception.BusinessException;
import com.dy.manager.AIManager;
import com.dy.model.entity.Chart;
import com.dy.service.ChartService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import java.io.IOException;

import static com.dy.constant.CommonConstant.BI_MODEL_ID;
import static com.dy.mq.BiMqConstant.BI_QUEUE_NAME;


/**
 * @Author: dy
 * @Date: 2024/4/29 11:54
 * @Description: 消息消费者
 */
@Component
@Slf4j
public class MyMessageConsumer {

    @Resource
    private ChartService chartService;

    @Resource
    private AIManager aiManager;

    @RabbitListener(queues = {BI_QUEUE_NAME}, ackMode = "MANUAL")
    public void receiveMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {

        if (StrUtil.isBlank(message)) {
            // TODO: 2024/4/30 这里还看不太懂
            channel.basicNack(deliveryTag, false, false);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "消息为空");
        }

        Long chartId = Long.valueOf(message);

        //  将图表状态更新为执行中
        Chart updateChart = new Chart();
        updateChart.setStatus(ChartStatus.RUNNING.getStatus());
        updateChart.setId(chartId);
        boolean updateResult = chartService.updateById(updateChart);
        if (!updateResult) {
            channel.basicNack(deliveryTag, false, false);
            handleChartUpdateError(chartId, "更新图表执行中状态失败");
        }

        //  调用 AI  服务
        String result = null;
        try {
            result = aiManager.doChat(BI_MODEL_ID, buildUserInput(chartId));
        } catch (Exception e) {
            channel.basicNack(deliveryTag, false, false);
            handleChartUpdateError(chartId, "AI 生成错误");
        }

        String[] split = result.split("@@@@@@");

        if (split.length < 3) {
            channel.basicNack(deliveryTag, false, false);
            handleChartUpdateError(chartId, "AI 生成错误");
        }


        String genChart = split[1].trim();
        String genResult = split[2].trim();

        log.info("genChart: {}", genChart);
        log.info("genResult: {}", genResult);

        //  将生成的图表保存到数据库
        Chart resultChart = new Chart();
        resultChart.setGenChart(genChart);
        resultChart.setGenResult(genResult);
        resultChart.setStatus(ChartStatus.SUCCEED.getStatus());
        resultChart.setId(chartId);

        boolean saveResult = chartService.updateById(resultChart);
        if (!saveResult) {
            channel.basicNack(deliveryTag, false, false);
            handleChartUpdateError(chartId, "更新图表成功状态失败!");
        }

        //  消息确认
        channel.basicAck(deliveryTag, false);


    }

    private void handleChartUpdateError(long chartId, String execMessage) {
        Chart updateChart = new Chart();
        updateChart.setId(chartId);
        updateChart.setStatus(ChartStatus.FAILED.getStatus());
        updateChart.setExecMessage(execMessage);
        boolean result = chartService.updateById(updateChart);
        if (!result) {
            log.error("更新图表状态失败 " + chartId + " : " + execMessage);
        }


    }


    private String buildUserInput(Long chartId) {
        StringBuilder userInput = new StringBuilder();

        Chart chart = chartService.getById(chartId);
        String goal = chart.getGoal();
        String csv = chart.getChartData();

        userInput.append("分析需求:").append(goal).append("\n");

        // TODO: 2024/4/22 补充图表类型

        //   将 Excel 文件转换为 csv
        userInput.append("原始数据: ").append(csv).append("\n");

        return userInput.toString();
    }



}
