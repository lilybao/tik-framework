package com.jeeadmin.vo.role;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 批量取消角色已分配的用户
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CancelUserVo {
    /**
     * 角色uuid
     */
    String roleUuid;
    /**
     * 用户uuid
     */
    List<String> userUuidList;
}
