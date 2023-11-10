package com.atong.atojbackendquestionservice.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;

import com.atong.atojbackendcommon.common.ErrorCode;
import com.atong.atojbackendcommon.constant.CommonConstant;
import com.atong.atojbackendcommon.exception.BusinessException;
import com.atong.atojbackendcommon.utils.SqlUtils;
import com.atong.atojbackendmodel.model.dto.questionsubmit.JudgeInfo;
import com.atong.atojbackendmodel.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.atong.atojbackendmodel.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.atong.atojbackendmodel.model.entity.Question;
import com.atong.atojbackendmodel.model.entity.QuestionSubmit;
import com.atong.atojbackendmodel.model.entity.User;
import com.atong.atojbackendmodel.model.enums.QuestionSubmitLanguageEnum;
import com.atong.atojbackendmodel.model.enums.QuestionSubmitStatusEnum;
import com.atong.atojbackendmodel.model.vo.QuestionSubmitVO;
import com.atong.atojbackendmodel.model.vo.QuestionSubmitViewVO;
import com.atong.atojbackendmodel.model.vo.QuestionVO;
import com.atong.atojbackendmodel.model.vo.UserVO;
import com.atong.atojbackendquestionservice.mapper.QuestionSubmitMapper;
import com.atong.atojbackendquestionservice.service.QuestionService;
import com.atong.atojbackendquestionservice.service.QuestionSubmitService;
import com.atong.atojbackendserviceclient.service.JudgeService;
import com.atong.atojbackendserviceclient.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author jojiboy
 * @description 针对表【question_submit(题目提交)】的数据库操作Service实现
 * @createDate 2023-10-11 09:27:42
 */
@Service
public class QuestionSubmitServiceImpl extends ServiceImpl<QuestionSubmitMapper, QuestionSubmit>
        implements QuestionSubmitService {

    @Resource
    private QuestionService questionService;

    @Resource
    private UserService userService;

    @Resource
    @Lazy // 循环调用导致异常, 开启懒加载注解
    private JudgeService judgeService;

    /**
     * 提交题目
     *
     * @param questionSubmitAddRequest
     * @param loginUser
     * @return
     */
    @Override
    public long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser) {
        String language = questionSubmitAddRequest.getLanguage();
        String code = questionSubmitAddRequest.getCode();
        long questionId = questionSubmitAddRequest.getQuestionId();
        // 编程语言是否合法
        QuestionSubmitLanguageEnum languageEnum = QuestionSubmitLanguageEnum.getEnumByValue(language);
        if (languageEnum == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "编程语言错误");
        }
        // 判断实体是否存在，根据类别获取实体
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 是否已提交题目
        long userId = loginUser.getId();
        // 每个用户串行提交题目
        // 锁必须要包裹住事务方法
        QuestionSubmit questionSubmit = new QuestionSubmit();
        questionSubmit.setCode(code);
        questionSubmit.setLanguage(language);
        questionSubmit.setUserId(userId);
        questionSubmit.setQuestionId(questionId);
        // 设置初始状态
        questionSubmit.setStatus(QuestionSubmitStatusEnum.WAITING.getValue());
//        questionSubmit.setJudgeInfo();
        boolean save = this.save(questionSubmit);
        if (!save) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "数据插入失败");
        }
        // 设置提交数
        Integer submitNum = question.getSubmitNum();
        Question updateQuestion = new Question();
        synchronized (question.getSubmitNum()) {
            submitNum = submitNum + 1;
            updateQuestion.setId(questionId);
            updateQuestion.setSubmitNum(submitNum);
            boolean result = questionService.updateById(updateQuestion);
            if (!result) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "提交数更新失败");
            }
        }
        // 执行判题服务
        Long questionSubmitId = questionSubmit.getId();
        CompletableFuture.runAsync(() -> {
            judgeService.doJudge(questionSubmitId);
        });
        return questionSubmitId;
    }


    /**
     * 获取查询包装类（用户根据哪些字段查询，根据前端传来的请求对象，得到 mybatis 框架支持的查询 QueryWrapper 类）
     *
     * @param questionSubmitQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest) {
        QueryWrapper<QuestionSubmit> queryWrapper = new QueryWrapper<>();
        if (questionSubmitQueryRequest == null) {
            return queryWrapper;
        }
        String language = questionSubmitQueryRequest.getLanguage();
        Integer status = questionSubmitQueryRequest.getStatus();
        Long questionId = questionSubmitQueryRequest.getQuestionId();
        Long userId = questionSubmitQueryRequest.getUserId();
        String sortField = questionSubmitQueryRequest.getSortField();
        String sortOrder = questionSubmitQueryRequest.getSortOrder();

        // 拼接查询条件
        queryWrapper.like(StringUtils.isNotBlank(language), "language", language);
        queryWrapper.eq(ObjectUtils.isNotEmpty(questionId), "questionId", questionId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq(QuestionSubmitStatusEnum.getEnumByValue(status) != null, "status", status);
        queryWrapper.eq("isDelete", 0);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    @Override
    public QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser) {
        QuestionSubmitVO questionSubmitVO = QuestionSubmitVO.objToVo(questionSubmit);
        // 除管理员以及本人外，普通用户只能看到无答案和提交代码的公开信息
        // 提交题目用户id
        long userId = questionSubmit.getUserId();
        // 登录用户id
        long loginUserId = loginUser.getId();
        if (loginUserId != userId && !userService.isAdmin(loginUser)){
            questionSubmitVO.setCode(null);
        }
        // 关联题目和用户信息
        Long questionId = questionSubmit.getQuestionId();
        HttpServletRequest request = null;
        QuestionVO questionVO = questionService.getQuestionVO(questionService.getById(questionId), request);
        questionSubmitVO.setQuestionVO(questionVO);
        UserVO userVO = userService.getUserVO(userService.getById(userId));
        questionSubmitVO.setUserVO(userVO);
        return questionSubmitVO;
    }

    @Override
    public Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage, User loginUser) {
        List<QuestionSubmit> questionSubmitList = questionSubmitPage.getRecords();
        Page<QuestionSubmitVO> questionSubmitVOPage = new Page<>(questionSubmitPage.getCurrent(), questionSubmitPage.getSize(), questionSubmitPage.getTotal());
        if (CollectionUtils.isEmpty(questionSubmitList)) {
            return questionSubmitVOPage;
        }
        List<QuestionSubmitVO> questionSubmitVOList = questionSubmitList.stream()
                // 循环调用, 而questionSubmitVO 关联题目和提交者信息需多次查数据库, 耗时增加
                .map(questionSubmit -> getQuestionSubmitVO(questionSubmit, loginUser))
                .collect(Collectors.toList());
        questionSubmitVOPage.setRecords(questionSubmitVOList);
        return questionSubmitVOPage;
    }

    @Override
    public Page<QuestionSubmitViewVO> getQuestionSubmitVOViewByPage(Page<QuestionSubmit> questionSubmitPage) {
        List<QuestionSubmit> questionSubmitList = questionSubmitPage.getRecords();
        Page<QuestionSubmitViewVO> questionSubmitVOViewPage = new Page<>(questionSubmitPage.getCurrent(), questionSubmitPage.getSize(), questionSubmitPage.getTotal());
        if (CollectionUtils.isEmpty(questionSubmitList)) {
            return questionSubmitVOViewPage;
        }
        List<QuestionSubmitViewVO> QuestionSubmitViewVOList = questionSubmitList.stream().map(questionSubmit -> {
            QuestionSubmitViewVO questionSubmitViewVO = new QuestionSubmitViewVO();
            BeanUtils.copyProperties(questionSubmit, questionSubmitViewVO);
            JudgeInfo judgeInfo = JSONUtil.toBean(questionSubmit.getJudgeInfo(), JudgeInfo.class);
            questionSubmitViewVO.setMessage(judgeInfo.getMessage());
            questionSubmitViewVO.setTime((ObjectUtil.defaultIfNull(judgeInfo.getTime(), 0) + "ms"));
            return questionSubmitViewVO;
        }).collect(Collectors.toList());
        questionSubmitVOViewPage.setRecords(QuestionSubmitViewVOList);
        return questionSubmitVOViewPage;
    }


}




