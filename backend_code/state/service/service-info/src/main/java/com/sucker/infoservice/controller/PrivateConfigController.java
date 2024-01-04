package com.sucker.infoservice.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sucker.commonutils.R;
import com.sucker.infoservice.entity.PrivateConfig;
import com.sucker.infoservice.service.PrivateConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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
 * @since 2023-11-24
 */
@Tag(name = "PrivateConfigController", description = "私人模式状态管理")
@RestController
@CrossOrigin
@RequestMapping("/infoservice/privateConfig")
public class PrivateConfigController {

  @Autowired private PrivateConfigService privateConfigService;

  @Operation(summary = "获取用户当前私人模式", description = "参数:用户id")
  @GetMapping("/{userId}")
  public R getPrivateConfig(@Parameter(description = "用户id") @PathVariable String userId) {
    List<PrivateConfig> privateConfigList = null;
    try {
      privateConfigList =
          privateConfigService.list(new QueryWrapper<PrivateConfig>().eq("user_id", userId));
    } catch (Exception e) {
      e.printStackTrace();
      return R.ok().message("未设置私人状态！");
    }
    return R.ok().data("privateConfigList", privateConfigList).message("查询成功！");
  }

  @Operation(summary = "创建私人状态", description = "参数:privateConfig类")
  @PostMapping("/createPrivateConfig")
  public R createPrivateConfig(
      @Parameter(description = "privateConfig类") @RequestBody PrivateConfig privateConfig) {
    privateConfigService.save(privateConfig);
    return R.ok().message("创建成功！");
  }

  @Operation(summary = "修改私人状态", description = "参数:privateConfig类")
  @PostMapping("/updatePrivateConfig")
  public R updatePrivateConfig(
      @Parameter(description = "privateConfig类") @RequestBody PrivateConfig privateConfig) {
    privateConfigService.updateById(privateConfig);
    return R.ok().message("修改成功！");
  }

  @Operation(summary = "删除私人状态", description = "参数:privateId")
  @PostMapping("/deletePrivateConfig")
  public R deletePrivateConfig(
      @Parameter(description = "privateId") @RequestParam String privateId) {
    privateConfigService.removeById(privateId);
    return R.ok().message("删除成功！");
  }
}
