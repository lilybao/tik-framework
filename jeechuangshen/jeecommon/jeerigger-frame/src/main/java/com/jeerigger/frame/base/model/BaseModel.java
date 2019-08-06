package com.jeerigger.frame.base.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public abstract class BaseModel<T extends BaseModel> extends Model<T> {
    @TableId(value = "uuid", type = IdType.UUID)
    private String uuid;
    /**
     * 创建人
     */
    @ApiModelProperty(
            hidden = true
    )
    @TableField(fill = FieldFill.INSERT)
    @JSONField(serialize = false)
    private String createUser;

    /**
     * 创建时间
     */
    @ApiModelProperty(
            hidden = true
    )
    @TableField(fill = FieldFill.INSERT)
    @JSONField(serialize = false)
    private Date createDate;

    /**
     * 更新人
     */
    @ApiModelProperty(
            hidden = true
    )
    @TableField(fill = FieldFill.UPDATE)
    @JSONField(serialize = false)
    private String updateUser;

    /**
     * 更新时间
     */
    @ApiModelProperty(
            hidden = true
    )
    @TableField(fill = FieldFill.UPDATE)
    @JSONField(serialize = false)
    private Date updateDate;

    @Override
    protected Serializable pkVal() {
        return this.uuid;
    }
}
