package com.atong.atojbackendjudgeservice.judge;


import com.atong.atojbackendjudgeservice.judge.strategy.DefaultJudgeStrategy;
import com.atong.atojbackendjudgeservice.judge.strategy.JavaLanguageJudgeStrategy;
import com.atong.atojbackendjudgeservice.judge.strategy.JudgeContext;
import com.atong.atojbackendjudgeservice.judge.strategy.JudgeStrategy;
import com.atong.atojbackendmodel.model.codesandbox.JudgeInfo;
import com.atong.atojbackendmodel.model.entity.QuestionSubmit;
import org.springframework.stereotype.Service;

/**
 * 判题管理（简化调用）
 */
@Service
public class JudgeManager {

    JudgeInfo doJudge(JudgeContext judgeContext){
        QuestionSubmit questionSubmit = judgeContext.getQuestionSubmit();
        String language = questionSubmit.getLanguage();
        JudgeStrategy judgeStrategy = new DefaultJudgeStrategy();
        if (language.equals("java")){
            judgeStrategy = new JavaLanguageJudgeStrategy();
        }
        return judgeStrategy.doJudge(judgeContext);
    }

}


