package com.atong.atojbackendquestionservice.service;


import com.atong.atojbackendmodel.model.dto.question.QuestionQueryRequest;
import com.atong.atojbackendmodel.model.entity.Question;
import com.atong.atojbackendmodel.model.vo.QuestionManageVO;
import com.atong.atojbackendmodel.model.vo.QuestionVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
* @author jojiboy
* @description 针对表【question(题目)】的数据库操作Service
* @createDate 2023-10-11 09:27:25
*/
public interface QuestionService extends IService<Question> {
    /**
     * 校验
     *
     * @param question
     * @param add
     */
    void validQuestion(Question question, boolean add);

    /**
     * 获取查询条件
     *
     * @param questionQueryRequest
     * @return
     */
    QueryWrapper<Question> getQueryWrapper(QuestionQueryRequest questionQueryRequest);

    /**
     * 获取题目封装
     *
     * @param question
     * @param request
     * @return
     */
    QuestionVO getQuestionVO(Question question, HttpServletRequest request);

    /**
     * 分页获取题目封装
     *
     * @param questionPage
     * @param request
     * @return
     */
    Page<QuestionVO> getQuestionVOPage(Page<Question> questionPage, HttpServletRequest request);


    /**
     * 管理题目列表页面
     *
     * @param questionPage   题目分页
     * @param queryWrapper 查询条件
     * @return {@link Page}<{@link QuestionManageVO}>
     */
    Page<QuestionManageVO> listManageQuestionByPage(Page<Question> questionPage, QueryWrapper<Question> queryWrapper);
}
