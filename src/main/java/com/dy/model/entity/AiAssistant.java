package com.dy.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * AI 问答助手信息表
 * @TableName ai_assistant
 */
@TableName(value ="ai_assistant")
@Data
public class AiAssistant implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 问题名称
     */
    private String questionName;

    /**
     * 问题概述
     */
    private String questionGoal;

    /**
     * 问题结果
     */
    private String questionResult;

    /**
     * 问题类型
     */
    private String questionType;

    /**
     * wait, running, succeed, fail
     */
    private String questionStatus;

    /**
     * 执行信息
     */
    private String execMessage;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}