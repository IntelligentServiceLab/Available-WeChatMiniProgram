package com.sucker.infoservice.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sucker.commonutils.R;
import com.sucker.infoservice.entity.Friend;
import com.sucker.infoservice.entity.Status;
import com.sucker.infoservice.entity.vo.FriendStatus;
import com.sucker.infoservice.service.FriendService;
import com.sucker.infoservice.service.StatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
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
 * 前端控制器
 *
 * @author sucker
 * @since 2023-11-25
 */
@Tag(name = "FriendController", description = "朋友订阅管理")
@RestController
@CrossOrigin
@RequestMapping("/infoservice/friend")
public class FriendController {

  @Autowired private FriendService friendService;
  @Autowired private StatusService statusService;

  @Operation(summary = "当前用户订阅一个用户", description = "参数:Friend类")
  @PostMapping("/createFriend")
  public R createFriend(@Parameter(description = "Friend类") @RequestBody Friend friend) {
    Friend friendServiceOne =
        friendService.getOne(
            new QueryWrapper<Friend>()
                .eq("user_id", friend.getFriendId())
                .eq("friend_id", friend.getUserId()));
    if (!ObjectUtils.isEmpty(friendServiceOne)) {
      friend.setSubscriptionStatus(Boolean.TRUE);
      friendServiceOne.setSubscriptionStatus(Boolean.TRUE);
      friendService.updateById(friendServiceOne);
    }
    friendService.save(friend);
    return R.ok().message("订阅成功！");
  }

  @Operation(summary = "当前用户取消订阅一个用户", description = "参数:用户id，被取消订阅用户id")
  @PostMapping("/deleteFriend")
  public R deleteFriend(
      @Parameter(description = "用户id") @RequestParam String userId,
      @Parameter(description = "被取消订阅用户id") @RequestParam String friendUserId) {
    boolean remove =
        friendService.remove(
            new QueryWrapper<Friend>().eq("user_id", userId).eq("friend_id", friendUserId));
    Friend friendServiceOne =
        friendService.getOne(
            new QueryWrapper<Friend>().eq("user_id", friendUserId).eq("friend_id", userId));
    if (!ObjectUtils.isEmpty(friendServiceOne)) {
      friendServiceOne.setSubscriptionStatus(Boolean.FALSE);
      friendService.updateById(friendServiceOne);
    }
    return R.ok().message("取消订阅成功！");
  }

  @Operation(summary = "当前用户对一个用户屏蔽位置", description = "参数:用户id，被屏蔽用户id")
  @PostMapping("/shieldFriend")
  public R shieldFriend(
      @Parameter(description = "用户id") @RequestParam String userId,
      @Parameter(description = "被取消订阅用户id") @RequestParam String friendUserId) {
    Friend friend =
        friendService.getOne(
            new QueryWrapper<Friend>().eq("user_id", userId).eq("friend_id", friendUserId));
    friend.setShield(Boolean.FALSE);
    friendService.updateById(friend);
    return R.ok().message("屏蔽成功！");
  }

  @Operation(summary = "当前用户对一个用户取消屏蔽位置", description = "参数:用户id，被屏蔽用户id")
  @PostMapping("/NonShieldFriend")
  public R NonShieldFriend(
      @Parameter(description = "用户id") @RequestParam String userId,
      @Parameter(description = "被取消订阅用户id") @RequestParam String friendUserId) {
    Friend friend =
        friendService.getOne(
            new QueryWrapper<Friend>().eq("user_id", userId).eq("friend_id", friendUserId));
    friend.setShield(Boolean.TRUE);
    friendService.updateById(friend);
    return R.ok().message("取消 屏蔽成功！");
  }

  @Operation(summary = "获取当前用户订阅用户（对方未订阅我）及相互订阅用户信息及状态", description = "参数:用户id")
  @GetMapping("/{userId}")
  public R getFriendList(@Parameter(description = "用户id") @PathVariable String userId) {
    List<FriendStatus> friendStatusList = new ArrayList<>();
    List<Friend> friendList = new ArrayList<>();
    try {
      friendList = friendService.list(new QueryWrapper<Friend>().eq("user_id", userId));
    } catch (Exception e) {
      e.printStackTrace();
      if (friendList.isEmpty()) return R.ok().message("未订阅好友！");
    }

    if (!friendList.isEmpty()) {
      // 获取friendList中每个Friend对象的friendId
      List<String> friendIds =
          friendList.stream().map(Friend::getFriendId).collect(Collectors.toList());

      // 查询Status表中对应的记录
      List<Status> statusList =
          statusService.list(new QueryWrapper<Status>().in("user_id", friendIds));

      // 将friendList和statusList中的记录进行匹配
      for (Friend friend : friendList) {
        // 找到friendId对应的Status记录
        Status status =
            statusList.stream()
                .filter(s -> s.getUserId().equals(friend.getFriendId()))
                .findFirst()
                .orElse(null);

        // 如果Status记录存在，且friend的shield字段为true，将Status的location字段设置为空
        if (status != null && friend.getShield()) {
          status.setLocation(null);
        }
        // 将Friend和对应的Status放入List
        FriendStatus friendStatus = new FriendStatus();
        friendStatus.setFriend(friend);
        friendStatus.setStatus(status);
        friendStatusList.add(friendStatus);
      }
    }
    // 处理 friendStatusMap，你可以根据业务需求进行进一步操作
    return R.ok().data("friendStatusList", friendStatusList);
  }

  @Operation(summary = "获取订阅当前用户的用户信息及状态", description = "参数:用户id")
  @GetMapping("/getFansList/{userId}")
  public R getFansList(@Parameter(description = "用户id") @PathVariable String userId) {
    List<FriendStatus> fansStatusList = new ArrayList<>();
    List<Friend> fansList = new ArrayList<>();
    try {
      fansList =
          friendService.list(
              new QueryWrapper<Friend>()
                  .eq("friend_id", userId)
                  .eq("subscription_status", Boolean.FALSE));
    } catch (Exception e) {
      e.printStackTrace();
      if (fansList.isEmpty()) return R.ok().message("未被好友订阅！");
    }

    if (!fansList.isEmpty()) {
      // 获取fansList中每个Friend对象的UserId
      List<String> fansIds = fansList.stream().map(Friend::getUserId).collect(Collectors.toList());

      // 查询Status表中对应的记录
      List<Status> statusList =
          statusService.list(new QueryWrapper<Status>().in("user_id", fansIds));

      // 将fansList和statusList中的记录进行匹配
      for (Friend fan : fansList) {
        // 找到friendId对应的Status记录
        Status status =
            statusList.stream()
                .filter(s -> s.getUserId().equals(fan.getUserId()))
                .findFirst()
                .orElse(null);

        // 如果Status记录存在，且fan的shield字段为true，将Status的location字段设置为空
        if (status != null && fan.getShield()) {
          status.setLocation(null);
        }
        // 将Fan和对应的Status放入Map
        FriendStatus friendStatus = new FriendStatus();
        friendStatus.setFriend(fan);
        friendStatus.setStatus(status);
        fansStatusList.add(friendStatus);
      }
    }
    // 处理 friendStatusMap，你可以根据业务需求进行进一步操作
    return R.ok().data("fansStatusLisst", fansStatusList);
  }
}
