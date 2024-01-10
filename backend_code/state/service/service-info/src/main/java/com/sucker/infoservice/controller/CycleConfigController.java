package com.sucker.infoservice.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sucker.commonutils.R;
import com.sucker.infoservice.entity.CycleConfig;
import com.sucker.infoservice.entity.Status;
import com.sucker.infoservice.service.CycleConfigService;
import com.sucker.infoservice.service.StatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
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
 * @since 2023-11-24
 */
@Tag(name = "CycleConfigController", description = "周期模式状态管理")
@RestController
@CrossOrigin
@RequestMapping("/infoservice/cycleConfig")
public class CycleConfigController {

  @Autowired private CycleConfigService cycleConfigService;

  @Autowired private StatusService statusService;

  @Operation(summary = "获取用户所有周期模式", description = "参数:用户id")
  @GetMapping("/{userId}")
  public R getCycleConfig(@Parameter(description = "用户id") @PathVariable String userId) {
    List<CycleConfig> cycleConfigList = null;
    try {
      cycleConfigList =
          cycleConfigService.list(new QueryWrapper<CycleConfig>().eq("user_id", userId));
    } catch (Exception e) {
      e.printStackTrace();
      return R.ok().message("未设置周期状态！");
    }
    return R.ok().data("privateConfigList", cycleConfigList).message("查询成功！");
  }

  @Operation(summary = "创建周期状态", description = "参数:cycleConfig类")
  @PostMapping("/createCycleConfig")
  public R createCycleConfig(
      @Parameter(description = "cycleConfig类") @RequestBody CycleConfig cycleConfig) {
    cycleConfigService.save(cycleConfig);
    return R.ok().message("创建成功！");
  }

  @Operation(summary = "修改周期状态", description = "参数:cycleConfig类")
  @PostMapping("/updateCycleConfig")
  public R updateCycleConfig(
      @Parameter(description = "cycleConfig类") @RequestBody CycleConfig cycleConfig) {
    cycleConfigService.updateById(cycleConfig);
    return R.ok().message("修改成功！");
  }

  @Operation(summary = "删除周期状态", description = "参数:cycleId")
  @PostMapping("/deleteCycleConfig")
  public R deleteCycleConfig(@Parameter(description = "cycleId") @RequestParam String cycleId) {
    cycleConfigService.removeById(cycleId);
    return R.ok().message("删除成功！");
  }

  @Transactional
  @Scheduled(fixedRate = 60000)
  public void cycleSchedule() {
    Date currentTime = new Date();
    Date oneMinuteLater = new Date(currentTime.getTime() + 60 * 1000);
    int currentDayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
    List<CycleConfig> cycleConfigList = new ArrayList<>();
    try {
      cycleConfigList =
          cycleConfigService.list(
              new QueryWrapper<CycleConfig>()
                  .eq("week", String.valueOf(currentDayOfWeek))
                  .apply(
                      "DATE_FORMAT(start_time, '%H:%i:%s') BETWEEN DATE_FORMAT(?, '%H:%i:%s') AND DATE_FORMAT(?, '%H:%i:%s')",
                      currentTime, oneMinuteLater));
    } catch (Exception e) {
      // e.printStackTrace();
      System.out.println("周期任务未查询到！");
    }
    if (!ObjectUtils.isEmpty(cycleConfigList)) {
      List<String> distinctUserIds =
          cycleConfigList.stream()
              .map(CycleConfig::getUserId)
              .distinct()
              .collect(Collectors.toList());
      List<Status> statusList =
          statusService.list(
              new QueryWrapper<Status>().in("user_id", distinctUserIds).eq("top", 1));
      statusList.forEach(status -> status.setTop(false));
      statusService.updateBatchById(statusList);
      List<Status> newCycleStatus =
          cycleConfigList.stream()
              .map(
                  cycleConfig -> {
                    Status status = new Status();
                    status.setStatusName(cycleConfig.getName());
                    status.setLocation(cycleConfig.getLocation());
                    status.setStartTime(cycleConfig.getStartTime());
                    status.setEndTime(cycleConfig.getEndTime());
                    status.setUserId(cycleConfig.getUserId());
                    status.setType(2);
                    status.setTop(true);
                    return status;
                  })
              .collect(Collectors.toList());
      statusService.saveBatch(newCycleStatus);
    }
  }
}
