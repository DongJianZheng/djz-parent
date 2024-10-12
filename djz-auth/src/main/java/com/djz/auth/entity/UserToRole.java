package com.djz.auth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 用户角色关系表
 * </p>
 *
 * @author djz
 * @since 2022-10-21
 */
@Data
@TableName("tb_user_to_role")
public class UserToRole extends Model<UserToRole> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "user_to_role_id", type = IdType.AUTO)
    private Integer userToRoleId;
    /**
     * 用户编号
     */
    @TableField("user_no")
    private String userNo;
    /**
     * 角色代号
     */
    @TableField("role_code")
    private String roleCode;

    @Override
    protected Serializable pkVal() {
        return this.userToRoleId;
    }
}
