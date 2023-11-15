package com.atong.atojbackendjudgeservice.controller.inner;

import com.atong.atojbackendjudgeservice.judge.JudgeService;
import com.atong.atojbackendmodel.model.entity.QuestionSubmit;
import com.atong.atojbackendserviceclient.service.JudgeFeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 判题服务内部调用接口
 *
 * @author <a href="https://gitee.com/x-2022-k">阿通</a>
 * @CreateDate: 2023/11/14 16:33
 */
@RestController
@RequestMapping("/inner")
public class JudgeInnerController implements JudgeFeignClient {

    @Resource
    private JudgeService judgeService;

    /**
     * 判题
     * @param questionSubmitId
     * @return
     */
    @Override
    @PostMapping("/do")
    public QuestionSubmit doJudge(@RequestParam("questionSubmitId") long questionSubmitId){
        return judgeService.doJudge(questionSubmitId);
    }
}
