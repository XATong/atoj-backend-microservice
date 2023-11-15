package com.atong.atojbackendserviceclient.service;

import com.atong.atojbackendmodel.model.entity.Question;
import com.atong.atojbackendmodel.model.entity.QuestionSubmit;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 题目服务
 *
 * @author <a href="https://gitee.com/x-2022-k">阿通</a>
*/
@FeignClient(name = "atoj-backend-question-service", path = "/api/question/inner")
public interface QuestionFeignClient{

    @GetMapping("/get/id")
    Question getQuestionById(@RequestParam("questionId") long questionId);

    @GetMapping("/question_submit/get/id")
    QuestionSubmit getQuestionSubmitById(@RequestParam("questionSubmitId") long questionSubmitId);

    @PostMapping("/question_submit/update")
    boolean updateQuestionSubmitById(@RequestBody QuestionSubmit questionSubmit);

    @PostMapping("/question/update")
    boolean updateQuestionById(@RequestBody Question question);
}
