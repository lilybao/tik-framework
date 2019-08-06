package com.jeerigger.frame.base.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.jeerigger.frame.enums.FlagEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public abstract class BaseTreeModel<T extends BaseTreeModel> extends BaseModel<T> {
    /**
     * 父级UUID
     */
    private String parentUuid;
    /**
     * 是否为叶子节点
     */
    @ApiModelProperty(
            hidden = true
    )
    private String leafFlag;


    @ApiModelProperty(hidden = true)
    @TableField(exist = false)
    @JSONField(name = "isLeaf")
    private boolean isLeaf;
    public boolean isLeaf() {
        if (leafFlag.equals(FlagEnum.YES.getCode())) {
            isLeaf = true;
        } else {
            isLeaf = false;
        }
        return isLeaf;
    }
}
