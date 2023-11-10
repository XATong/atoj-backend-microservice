package com.atong.atojbackendjudgeservice.judge.codesandbox.impl;



import com.atong.atojbackendjudgeservice.judge.codesandbox.CodeSandbox;
import com.atong.atojbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.atong.atojbackendmodel.model.codesandbox.ExecuteCodeResponse;
import com.atong.atojbackendmodel.model.codesandbox.JudgeInfo;
import com.atong.atojbackendmodel.model.enums.JudgeInfoMessageEnum;
import com.atong.atojbackendmodel.model.enums.QuestionSubmitStatusEnum;

import java.util.List;

/**
 * 示例代码沙箱（跑通业务流程）
 */
public class ExampleCodeSandbox implements CodeSandbox {

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        List<String> inputList = executeCodeRequest.getInputList();
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setOutputList(inputList);
        executeCodeResponse.setMessage("测试执行成功");
        executeCodeResponse.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setMessage(JudgeInfoMessageEnum.ACCEPTED.getText());
        judgeInfo.setMemory(100L);
        judgeInfo.setTime(100L);
        executeCodeResponse.setJudgeInfo(judgeInfo);
        return executeCodeResponse;
    }
}
