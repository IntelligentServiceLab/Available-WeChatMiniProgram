package com.sucker.infoservice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author sucker
 * @since 2023-11-24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("status")
@Schema(description = "Status对象")
public class Status implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "状态id")
  @TableId(value = "status_id", type = IdType.ASSIGN_ID)
  private String statusId;

  @Schema(description = "状态名")
  @TableField("status_name")
  private String statusName;

  @Schema(description = "位置")
  @TableField("location")
  private String location;

  @Schema(description = "用户id")
  @TableField("user_id")
  private String userId;

  @Schema(description = "开始时间")
  @TableField("start_time")
  private Date startTime;

  @Schema(description = "结束时间")
  @TableField("end_time")
  private Date endTime;

  @Schema(description = "状态模式(0通用，1私人，2周期)")
  @TableField("type")
  private Integer type;

  @Schema(description = "是否为当前状态(0否，1是)")
  @TableField("top")
  private Boolean top;
}
