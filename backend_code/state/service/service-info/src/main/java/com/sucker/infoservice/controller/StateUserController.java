package com.sucker.infoservice.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sucker.commonutils.R;
import com.sucker.infoservice.entity.StateUser;
import com.sucker.infoservice.service.StateUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author sucker
 * @since 2023-11-30
 */
@Tag(name = "StateUserController", description = "用户管理")
@RestController
@CrossOrigin
@RequestMapping("/infoservice/stateUser")
public class StateUserController {

  @Autowired private StateUserService stateUserService;

  @Operation(summary = "用户登录注册", description = "参数:用户openid")
  @PostMapping("/login")
  public R login(@Parameter(description = "用户openid") @RequestParam String openId) {
    StateUser user = stateUserService.getOne(new QueryWrapper<StateUser>().eq("open_id", openId));
    if (ObjectUtils.isEmpty(user)) {
      StateUser stateUser = new StateUser();
      stateUser.setUserName("默认名称");
      stateUser.setPhoto("默认头像");
      stateUser.setOpenId(openId);
      stateUserService.save(stateUser);
      StateUser newUser =
          stateUserService.getOne(new QueryWrapper<StateUser>().eq("open_id", openId));
      return R.ok().data("user", newUser).message("注册成功！");
    }
    return R.ok().data("user", user).message("登录成功！");
  }

  @Operation(summary = "修改用户信息", description = "参数:stateUser类")
  @PostMapping("/updateUser")
  public R updateUser(@Parameter(description = "stateUser类") @RequestBody StateUser stateUser) {
    stateUserService.updateById(stateUser);
    return R.ok().message("修改成功！");
  }

  @Operation(summary = "获取用户信息", description = "参数:user_id")
  @GetMapping("/getUserInfo/{userId}")
  public R getUserInfo(@Parameter(description = "user_id") @PathVariable String userId) {
    StateUser user = stateUserService.getById(userId);
    return R.ok().data("user", user).message("获取成功！");
  }
}
