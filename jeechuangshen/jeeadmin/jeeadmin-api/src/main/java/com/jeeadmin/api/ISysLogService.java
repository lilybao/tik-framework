package com.jeeadmin.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jeeadmin.entity.SysLog;
import com.jeerigger.frame.base.service.BaseService;
import com.jeerigger.frame.page.PageHelper;

/**
 * <p>
 * 操作日志表 服务类
 * </p>
 *
 * @author wangcy
 * @since 2018-11-12
 */
public interface ISysLogService extends BaseService<SysLog> {
    /**
     * 获取日志列表
     *
     * @param pageHelper
     * @return
     */
    Page<SysLog> selectPage(PageHelper<SysLog> pageHelper);
}
