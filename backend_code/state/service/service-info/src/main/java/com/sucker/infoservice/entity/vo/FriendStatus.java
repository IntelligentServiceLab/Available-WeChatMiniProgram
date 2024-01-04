package com.sucker.infoservice.entity.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.sucker.infoservice.entity.Friend;
import com.sucker.infoservice.entity.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "FriendStatus对象")
public class FriendStatus {

  @Schema(description = "用户好友")
  @TableField("friend")
  private Friend friend;

  @Schema(description = "用户状态")
  @TableField("status")
  private Status status;
}
