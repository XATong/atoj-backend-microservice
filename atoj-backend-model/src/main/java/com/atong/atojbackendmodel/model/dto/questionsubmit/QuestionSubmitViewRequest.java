package com.atong.atojbackendmodel.model.dto.questionsubmit;

import com.atong.atojbackendcommon.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 查询请求
 *
 * @author <a href="https://gitee.com/x-2022-k">阿通</a>
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QuestionSubmitViewRequest extends PageRequest implements Serializable {

    /**
     * 编程语言
     */
    private String language;


    /**
     * 题目 id
     */
    private Long questionId;


    private static final long serialVersionUID = 1L;
}