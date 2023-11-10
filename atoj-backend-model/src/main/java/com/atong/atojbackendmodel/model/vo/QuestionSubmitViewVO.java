package com.atong.atojbackendmodel.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 题目提交封装类
 */
@Data
public class QuestionSubmitViewVO implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 编程语言
     */
    private String language;

    /**
     * 判题信息
     */
    private String message;

    /**
     * 消耗时间 ms
     */
    private String time;


    /**
     * 题目 id
     */
    private Long questionId;

    /**
     * 提交代码用户 id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date createTime;

}