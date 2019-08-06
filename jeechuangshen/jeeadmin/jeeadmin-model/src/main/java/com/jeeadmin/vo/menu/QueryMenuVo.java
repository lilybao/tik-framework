package com.jeeadmin.vo.menu;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class QueryMenuVo {
    /**
     * 菜单UUID
     */
    private String menuUuid;
    /**
     * 菜单名称
     */
    private String menuName;
    /**
     * 菜单归属系统
     */
    private String sysCode;

}
