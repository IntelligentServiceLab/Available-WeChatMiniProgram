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
 * @since 2023-11-30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("state_user")
@Schema(description = "StateUser对象")
public class StateUser implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "用户id")
  @TableId(value = "user_id", type = IdType.ASSIGN_ID)
  private String userId;

  @Schema(description = "用户名")
  @TableField("user_name")
  private String userName;

  @Schema(description = "用户头像")
  @TableField("photo")
  private String photo;

  @Schema(description = "创建时间")
  @TableField(value = "create_time", fill = FieldFill.INSERT)
  private Date createTime;

  @Schema(description = "修改时间")
  @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
  private Date updateTime;

  @Schema(description = "openid")
  @TableField("open_id")
  private String openId;
}
