package com.jeerigger.module.sys.entity;

import com.jeerigger.frame.base.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysParam extends BaseModel<SysParam> {
    /**
     * 参数名称
     */
    private String paramName;

    /**
     * 参数键名
     */
    private String paramKey;

    /**
     * 参数键值
     */
    private String paramValue;
}
