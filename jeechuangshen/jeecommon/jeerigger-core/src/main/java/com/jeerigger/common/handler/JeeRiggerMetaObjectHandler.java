package com.jeerigger.common.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.jeerigger.common.shiro.ShiroUtil;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * mybatisplus自定义填充公共字段
 */
@Component
public class JeeRiggerMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        Object createUser = null;
        if (metaObject.hasGetter("createUser")) {
            createUser = metaObject.getValue("createUser");
        }
        Object createDate = null;
        if (metaObject.hasGetter("createDate")) {
            createDate = metaObject.getValue("createDate");
        }
        if (createUser == null) {
            setFieldValByName("createUser", ShiroUtil.getUserUuid(), metaObject);
        }
        if (createDate == null) {
            setFieldValByName("createDate", new Date(), metaObject);
        }
        Object createUserName = null;
        if (metaObject.hasGetter("createUserName")) {
            createUserName = metaObject.getValue("createUserName");
            if (createUserName == null) {
                setFieldValByName("createUserName", ShiroUtil.getUserData().getUserName(), metaObject);
            }
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Object updateUser = null;
        if (metaObject.hasGetter("updateUser")) {
            updateUser = metaObject.getValue("updateUser");
        }
        Object updateDate = null;
        if (metaObject.hasGetter("updateDate")) {
            updateDate = metaObject.getValue("updateDate");
        }
        if (updateUser == null) {
            setFieldValByName("updateUser", ShiroUtil.getUserUuid(), metaObject);
        }
        if (updateDate == null) {
            setFieldValByName("updateDate", new Date(), metaObject);
        }
        Object updateUserName = null;
        if (metaObject.hasGetter("updateUserName")) {
            updateUserName = metaObject.getValue("updateUserName");
            if (updateUserName == null) {
                setFieldValByName("updateUserName", ShiroUtil.getUserData().getUserName(), metaObject);
            }
        }
    }
}
