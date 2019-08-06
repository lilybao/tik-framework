package com.jeerigger.common.properties;

import com.jeerigger.common.enums.SysCodeEnum;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.shiro.realm.AuthorizingRealm;

import java.util.Arrays;
import java.util.List;

@Data
@Accessors(chain = true)
public class ShiroProperty {
    /**
     * 用户认证realm 需继承AuthorizingRealm
     */
    private Class<? extends AuthorizingRealm> realm;
    /**
     * 获取菜单标识，多个逗号隔开
     */
    private List<String> menu_sys_code = Arrays.asList(SysCodeEnum.JEE_RIGGER_SYSTEM.getCode());
    /**
     * 无需认证可访问url
     */
    private List<String> anonUrl;

}
