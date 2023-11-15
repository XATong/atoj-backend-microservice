package com.atong.atojbackendquestionservice.controller.inner;

import com.atong.atojbackendmodel.model.entity.Question;
import com.atong.atojbackendmodel.model.entity.QuestionSubmit;
import com.atong.atojbackendquestionservice.service.QuestionService;
import com.atong.atojbackendquestionservice.service.QuestionSubmitService;
import com.atong.atojbackendserviceclient.service.QuestionFeignClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 题目服务内部接口
 *
 * @author <a href="https://gitee.com/x-2022-k">阿通</a>
 * @CreateDate: 2023/11/14 16:27
 */
@RestController
@RequestMapping("/inner")
public class QuestionInnerController implements QuestionFeignClient {

    @Resource
    private QuestionService questionService;

    @Resource
    private QuestionSubmitService questionSubmitService;

    @Override
    @GetMapping("/get/id")
    public Question getQuestionById(@RequestParam("questionId") long questionId){
        return questionService.getById(questionId);
    }

    @Override
    @GetMapping("/question_submit/get/id")
    public QuestionSubmit getQuestionSubmitById(@RequestParam("questionSubmitId") long questionSubmitId){
        return questionSubmitService.getById(questionSubmitId);
    }

    @Override
    @PostMapping("/question_submit/update")
    public boolean updateQuestionSubmitById(@RequestBody QuestionSubmit questionSubmit){
        return questionSubmitService.updateById(questionSubmit);
    }

    @Override
    @PostMapping("/question/update")
    public boolean updateQuestionById(@RequestBody Question question){
        return questionService.updateById(question);
    }
}
