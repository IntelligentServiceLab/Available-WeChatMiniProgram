package com.sucker.shiroservice.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户表
 *
 * @author sucker
 * @since 2022-02-12
 */
@Getter
@Setter
// @TableName("user")
@Schema(hidden = true)
// @ApiModel(value = "EduUser对象", description = "用户表")
public class User implements Serializable {

  private static final long serialVersionUID = 1L;

  // @ApiModelProperty("用户id")
  @TableId(value = "user_id", type = IdType.ASSIGN_ID)
  private String userId;

  // @ApiModelProperty("用户姓名")
  @TableField("username")
  private String username;

  // @ApiModelProperty("手机号")
  @TableField("mobile")
  private String mobile;

  // @ApiModelProperty("密码")
  @TableField("password")
  private String password;

  // @ApiModelProperty("创建时间")
  @TableField(value = "create_time", fill = FieldFill.INSERT)
  private Date createTime;

  // @ApiModelProperty("更新时间")
  @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
  private Date updateTime;

  // @ApiModelProperty("角色Id")
  @TableField("role_id")
  private Integer roleId;
}
