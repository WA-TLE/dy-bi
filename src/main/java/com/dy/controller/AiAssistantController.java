package com.dy.controller;

import cn.hutool.core.util.StrUtil;
import com.dy.common.BaseResponse;
import com.dy.common.ErrorCode;
import com.dy.common.ResultUtils;
import com.dy.exception.BusinessException;
import com.dy.exception.ThrowUtils;
import com.dy.manager.AIManager;
import com.dy.manager.RedissonManager;
import com.dy.model.dto.ai_assistant.AiAssistantQueryRequest;
import com.dy.model.entity.AiAssistant;
import com.dy.model.entity.User;
import com.dy.service.AiAssistantService;
import com.dy.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.dy.constant.ChartConstant.ASSISTANT_BY_AI;

/**
 * @Author: dy
 * @Date: 2024/5/1 21:07
 * @Description:
 */
@RestController
@RequestMapping("/aiAssistant")
@Slf4j
public class AiAssistantController {

    @Resource
    private AIManager aiManager;

    @Resource
    private UserService userService;

    @Resource
    private AiAssistantService aiAssistantService;

    @Resource
    private RedissonManager redissonManager;


    @PostMapping("/chat")
    public BaseResponse<?> queryAiAssistant(@RequestBody AiAssistantQueryRequest aiAssistantQueryRequest, HttpServletRequest request) {
        String questionName = aiAssistantQueryRequest.getQuestionName();
        String questionGoal = aiAssistantQueryRequest.getQuestionGoal();
        String questionType = aiAssistantQueryRequest.getQuestionType();

        if (StrUtil.hasBlank(questionName, questionGoal, questionType)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }


        long modelId = 1785664780150362113L;


        String result = aiManager.doAssistant(modelId, questionGoal);

        //  获取当前用户
        User loginUser = userService.getLoginUser(request);

        //  对当前用户进行限流
        redissonManager.doRateLimit(ASSISTANT_BY_AI + loginUser.getId());



        AiAssistant aiAssistant = new AiAssistant();
        aiAssistant.setQuestionName(questionName);
        aiAssistant.setQuestionGoal(questionGoal);
        aiAssistant.setQuestionResult(result);
        aiAssistant.setQuestionType(questionType);
        aiAssistant.setQuestionStatus("succeed");
        aiAssistant.setUserId(loginUser.getId());

        //  保存结果到数据库中
        boolean save = aiAssistantService.save(aiAssistant);
        ThrowUtils.throwIf(!save, ErrorCode.SYSTEM_ERROR, "保存对话失败!");


        return ResultUtils.success(aiAssistant);
    }
}
