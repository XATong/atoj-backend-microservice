package com.atong.atojbackendserviceclient.service;

import com.atong.atojbackendcommon.common.ErrorCode;
import com.atong.atojbackendcommon.constant.UserConstant;
import com.atong.atojbackendcommon.exception.BusinessException;
import com.atong.atojbackendmodel.model.entity.User;
import com.atong.atojbackendmodel.model.enums.UserRoleEnum;
import com.atong.atojbackendmodel.model.vo.UserVO;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;

/**
 * 用户服务
 *
 * @author <a href="https://gitee.com/x-2022-k">阿通</a>
 */
@FeignClient(name = "atoj-backend-user-service", path = "/api/user/inner")
public interface UserFeignClient {

    /**
     * 根据id 获取用户
     * @param userId
     * @return
     */
    @GetMapping("/get/id")
    User getById(@RequestParam("userId") long userId);

    /**
     * @param idList
     * @return
     */
    @GetMapping("/get/ids")
    List<User> listByIds(@RequestParam("idList") Collection<Long> idList);


    /**
     * @param userIdList
     * @return
     */
    @GetMapping("/get/list")
    List<User> list(@RequestParam("userIdList") Collection<Long> userIdList);


    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    default User getLoginUser(HttpServletRequest request){
        // 先判断是否已登录
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return currentUser;
    };


    /**
     * 是否为管理员
     *
     * @param user
     * @return
     */
    default boolean isAdmin(User user){
        return user != null && UserRoleEnum.ADMIN.getValue().equals(user.getUserRole());
    };


    /**
     * 获取脱敏的用户信息
     *
     * @param user
     * @return
     */
    default UserVO getUserVO(User user){
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    };


}
