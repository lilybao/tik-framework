package com.jeeadmin.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jeeadmin.entity.SysParam;
import com.jeerigger.frame.base.service.BaseService;
import com.jeerigger.frame.page.PageHelper;

/**
 * <p>
 * 系统参数配置表 服务类
 * </p>
 *
 * @author wangcy
 * @since 2018-11-12
 */
public interface ISysParamService extends BaseService<SysParam> {
    /**
     * 查询系统参数配置列表
     *
     * @param pageHelper
     * @return
     */
    Page<SysParam> selectPage(PageHelper<SysParam> pageHelper);

    /**
     * 保存系统参数
     *
     * @param sysParam
     * @return
     */
    boolean saveSysParam(SysParam sysParam);

    /**
     * 更新系统参数
     *
     * @param sysParam
     * @return
     */
    boolean updateSysParam(SysParam sysParam);

    /**
     * 删除系统参数
     *
     * @param paramUuid
     * @return
     */
    boolean deleteSysParam(String paramUuid);

}
