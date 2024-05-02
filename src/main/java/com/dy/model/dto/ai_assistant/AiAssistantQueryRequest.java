package com.dy.model.dto.ai_assistant;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.dy.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 查询请求
 *

 */

@Data
public class AiAssistantQueryRequest  implements Serializable {


    /**
     * 问题名称
     */
    private String questionName;

    /**
     * 问题概述
     */
    private String questionGoal;


    /**
     * 问题类型
     */
    private String questionType;



    private static final long serialVersionUID = 1L;
}