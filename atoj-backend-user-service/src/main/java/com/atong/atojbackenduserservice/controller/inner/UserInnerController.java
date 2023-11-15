package com.atong.atojbackenduserservice.controller.inner;

import com.atong.atojbackendmodel.model.entity.User;
import com.atong.atojbackendserviceclient.service.UserFeignClient;
import com.atong.atojbackenduserservice.service.UserService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * 用户服务内部接口
 *
 * @author <a href="https://gitee.com/x-2022-k">阿通</a>
 * @CreateDate: 2023/11/14 16:19
 */
@RestController
@RequestMapping("/inner")
public class UserInnerController implements UserFeignClient {

    @Resource
    private UserService userService;

    /**
     * 根据id 获取用户
     * @param userId
     * @return
     */
    @Override
    @GetMapping("/get/id")
    public User getById(@RequestParam("userId") long userId){
        return userService.getById(userId);
    }

    /**
     * @param idList
     * @return
     */
    @Override
    @GetMapping("/get/ids")
    public List<User> listByIds(@RequestParam("idList") Collection<Long> idList){
        return userService.listByIds(idList);
    }


    /**
     * @param userIdList
     * @return
     */
    @Override
    @GetMapping("/get/list")
    public List<User> list(@RequestParam("userIdList") Collection<Long> userIdList){
        return userService.list(Wrappers.lambdaQuery(User.class).select(User::getUserName, User::getId).in(User::getId, userIdList));
    }
}
