package com.atong.atojbackendserviceclient.service;

import com.atong.atojbackendmodel.model.entity.QuestionSubmit;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 判题服务
 *
 * @author <a href="https://gitee.com/x-2022-k">阿通</a>
 */
@FeignClient(name = "atoj-backend-judge-service", path = "/api/judge/inner")
public interface JudgeFeignClient {

    /**
     * 判题
     * @param questionSubmitId
     * @return
     */
    @PostMapping("/do")
    QuestionSubmit doJudge(@RequestParam("questionSubmitId") long questionSubmitId);
}
