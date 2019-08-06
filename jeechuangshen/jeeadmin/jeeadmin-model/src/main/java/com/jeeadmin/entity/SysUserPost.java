package com.jeeadmin.entity;

import com.jeerigger.frame.base.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author wangcy
 * @since 2019-01-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysUserPost extends BaseModel<SysUserPost> {

    private static final long serialVersionUID = 1L;

    /**
     * 岗位UUID
     */
    private String postUuid;

    /**
     * 用户UUID
     */
    private String userUuid;

}
