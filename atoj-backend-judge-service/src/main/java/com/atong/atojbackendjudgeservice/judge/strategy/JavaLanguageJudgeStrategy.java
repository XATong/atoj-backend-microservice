package com.atong.atojbackendjudgeservice.judge.strategy;

import cn.hutool.json.JSONUtil;
import com.atong.atojbackendmodel.model.codesandbox.JudgeInfo;
import com.atong.atojbackendmodel.model.dto.question.JudgeCase;
import com.atong.atojbackendmodel.model.dto.question.JudgeConfig;
import com.atong.atojbackendmodel.model.enums.JudgeInfoMessageEnum;


import java.util.List;
import java.util.Optional;

/**
 * Java 程序判题策略
 */
public class JavaLanguageJudgeStrategy implements JudgeStrategy {


    /**
     * 执行判题
     * @param judgeContext
     * @return
     */
    @Override
    public JudgeInfo doJudge(JudgeContext judgeContext) {
        JudgeInfo judgeInfoResponse = new JudgeInfo();
        JudgeInfo judgeInfo = judgeContext.getJudgeInfo();
        Long memory = Optional.ofNullable(judgeInfo.getMemory()).orElse(0L);
        Long time = Optional.ofNullable(judgeInfo.getTime()).orElse(0L);
        judgeInfoResponse.setMemory(memory);
        judgeInfoResponse.setTime(time);
        JudgeInfoMessageEnum judgeInfoMessageEnum = JudgeInfoMessageEnum.ACCEPTED;
        // 先判断沙箱执行的结果输出数量是否和预期输出数量相等
        List<String> outputList = judgeContext.getOutputList();
        List<String> inputList = judgeContext.getInputList();
        if (inputList.size() != outputList.size() && outputList.isEmpty()) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.WRONG_ANSWER;
            judgeInfoResponse.setMessage(judgeInfoMessageEnum.getText());
            return judgeInfoResponse;
        }
        // 依次判断每一项输出和预期输出是否相等
        List<JudgeCase> judgeCasesList = judgeContext.getJudgeCaseList();
        for (int i = 0; i < judgeCasesList.size(); i++) {
            JudgeCase judgeCase = judgeCasesList.get(i);
            if (!judgeCase.getOutput().equals(outputList.get(i))) {
                judgeInfoMessageEnum = JudgeInfoMessageEnum.WRONG_ANSWER;
                judgeInfoResponse.setMessage(judgeInfoMessageEnum.getText());
                return judgeInfoResponse;
            }
        }
        // 判题题目的限制是否符合要求
        String judgeConfigStr = judgeContext.getQuestion().getJudgeConfig();
        JudgeConfig judgeConfig = JSONUtil.toBean(judgeConfigStr, JudgeConfig.class);
        Long needTimeLimit = judgeConfig.getTimeLimit();
        Long needMemoryLimit = judgeConfig.getMemoryLimit();
        if (memory > needMemoryLimit) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.MEMORY_LIMIT_EXCEEDED;
            judgeInfoResponse.setMessage(judgeInfoMessageEnum.getText());
            return judgeInfoResponse;
        }
        // todo 假设 Java程序本身需要额外执行 10 毫秒钟
        long JAVA_PROGRAM_TIME_COST = 10L;
        if ((time - JAVA_PROGRAM_TIME_COST) > needTimeLimit) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.TIME_LIMIT_EXCEEDED;
            judgeInfoResponse.setMessage(judgeInfoMessageEnum.getText());
            return judgeInfoResponse;
        }
        judgeInfoResponse.setMessage(judgeInfoMessageEnum.getText());
        return judgeInfoResponse;
    }
}
