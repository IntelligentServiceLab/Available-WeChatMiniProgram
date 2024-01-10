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
 * @since 2023-11-25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("friend")
@Schema(description = "Friend对象")
public class Friend implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "关系id")
  @TableId(value = "friendship_id", type = IdType.ASSIGN_ID)
  private String friendshipId;

  @Schema(description = "用户id")
  @TableField("user_id")
  private String userId;

  @Schema(description = "被订阅者id")
  @TableField("friend_id")
  private String friendId;

  @Schema(description = "是否相互订阅(0未，1已)")
  @TableField("subscription_status")
  private Boolean subscriptionStatus;

  @Schema(description = "订阅时间")
  @TableField(value = "create_time", fill = FieldFill.INSERT)
  private Date createTime;

  @Schema(description = "是否置顶(0否，1是)")
  @TableField("top")
  private Boolean top;

  @Schema(description = "是否屏蔽(0否，1是)")
  @TableField("shield")
  private Boolean shield;
}
