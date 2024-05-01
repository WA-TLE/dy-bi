package com.dy.manager;

import com.dy.common.ErrorCode;
import com.dy.exception.ThrowUtils;
import com.yupi.yucongming.dev.client.YuCongMingClient;
import com.yupi.yucongming.dev.common.BaseResponse;
import com.yupi.yucongming.dev.model.DevChatRequest;
import com.yupi.yucongming.dev.model.DevChatResponse;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author: dy
 * @Date: 2024/4/22 18:20
 * @Description:
 */
@Component
public class AIManager {

    @Resource
    private YuCongMingClient client;

    /**
     * 数据分析助手
     *
     * @param modelId
     * @param message
     * @return
     */
    public String doChat(Long modelId, String message) {

        DevChatRequest devChatRequest = new DevChatRequest();
        devChatRequest.setModelId(modelId);
        devChatRequest.setMessage(message);

        BaseResponse<DevChatResponse> response = client.doChat(devChatRequest);

        ThrowUtils.throwIf(response == null, ErrorCode.SYSTEM_ERROR, "AI 相应错误");


        return response.getData().getContent();
    }


    /**
     * AI 助手问答
     *
     * @param modelId
     * @param message
     * @return
     */
    public String doAssistant(Long modelId, String message) {

        DevChatRequest devChatRequest = new DevChatRequest();
        devChatRequest.setModelId(modelId);
        devChatRequest.setMessage(message);

        BaseResponse<DevChatResponse> response = client.doChat(devChatRequest);

        ThrowUtils.throwIf(response == null, ErrorCode.SYSTEM_ERROR, "AI 相应错误");


        return response.getData().getContent();
    }


}
