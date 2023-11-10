package com.atong.atojbackendjudgeservice.judge.codesandbox;


import com.atong.atojbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.atong.atojbackendmodel.model.codesandbox.ExecuteCodeResponse;

/**
 * 代码沙箱接口定义
 */
public interface CodeSandbox {

    /**
     * 执行代码
     * @param executeCodeRequest
     * @return
     */
    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}
