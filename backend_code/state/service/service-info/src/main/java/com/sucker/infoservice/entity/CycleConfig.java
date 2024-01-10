package com.sucker.infoservice.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
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
@TableName("cycle_config")
@Schema(description = "CycleConfig对象")
public class CycleConfig implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "周期设置id")
  @TableId(value = "cycle_id", type = IdType.ASSIGN_ID)
  private String cycleId;

  @Schema(description = "用户id")
  @TableField("user_id")
  private String userId;

  @Schema(description = "状态名")
  @TableField("name")
  private String name;

  @Schema(description = "位置")
  @TableField("location")
  private String location;

  @Schema(description = "开始时间")
  @TableField("start_time")
  private Date startTime;

  @Schema(description = "结束时间")
  @TableField("end_time")
  private Date endTime;

  @Schema(description = "创建时间")
  @TableField(value = "create_time", fill = FieldFill.INSERT)
  private Date createTime;

  @Schema(description = "更新时间")
  @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
  private Date updateTime;

  @Schema(description = "周期(周一、周二...)")
  @TableField("week")
  private String week;
}
