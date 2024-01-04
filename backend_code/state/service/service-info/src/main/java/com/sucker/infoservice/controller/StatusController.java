package com.sucker.infoservice.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sucker.commonutils.R;
import com.sucker.infoservice.entity.Status;
import com.sucker.infoservice.service.StatusService;
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
import org.springframework.web.bind.annotation.RestController;

/**
 * @author sucker
 * @since 2023-11-24
 */
@Tag(name = "StatusController", description = "状态管理")
@RestController
@CrossOrigin
@RequestMapping("/infoservice/status")
public class StatusController {

  @Autowired private StatusService statusService;

  @Operation(summary = "获取用户当前状态", description = "参数:用户id")
  @GetMapping("/{userId}")
  public R getTopStatus(@Parameter(description = "用户id") @PathVariable String userId) {
    Status top =
        statusService.getOne(new QueryWrapper<Status>().eq("user_id", userId).eq("top", 1));
    if (ObjectUtils.isEmpty(top)) {
      return R.error().message("当前未设置状态！");
    } else {
      return R.ok().data("status", top).message("查询成功！");
    }
  }

  @Operation(summary = "创建状态", description = "参数:status类")
  @PostMapping("/createStatus")
  public R createStatus(@Parameter(description = "status类") @RequestBody Status status) {
    Status top =
        statusService.getOne(
            new QueryWrapper<Status>().eq("user_id", status.getUserId()).eq("top", 1));
    if (!ObjectUtils.isEmpty(top)) {
      top.setTop(false);
      statusService.updateById(top);
    }
    statusService.save(status);
    return R.ok().message("设置成功！");
  }

  @Operation(summary = "修改当前状态", description = "参数:status类")
  @PostMapping("/updateStatus")
  public R updateStatus(@Parameter(description = "status类") @RequestBody Status status) {
    statusService.updateById(status);
    return R.ok().message("修改成功！");
  }
}
