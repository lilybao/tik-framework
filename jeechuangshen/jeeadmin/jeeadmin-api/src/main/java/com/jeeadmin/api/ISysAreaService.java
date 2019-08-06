package com.jeeadmin.api;

import com.jeeadmin.entity.SysArea;
import com.jeerigger.frame.base.service.BaseTreeService;

import java.util.List;

/**
 * <p>
 * 行政区划表 服务类
 * </p>
 *
 * @author wangcy
 * @since 2018-11-12
 */
public interface ISysAreaService extends BaseTreeService<SysArea> {

    /**
     * 根据行政区划UUID获取下级行政区划列表
     *
     * @param areaUuid
     * @return
     */
    List<SysArea> selectChildArea(String areaUuid);

    /**
     * 查询所有行政区划
     *
     * @param sysArea
     * @return
     */
    List<SysArea> selectAreaList(SysArea sysArea);

    /**
     * 新增行政区划信息
     *
     * @param sysArea
     * @return
     */
    boolean saveSysArea(SysArea sysArea);

    /**
     * 更新行政区划信息
     *
     * @param sysArea
     * @return
     */
    boolean updateSysArea(SysArea sysArea);

    /**
     * 更新行政区划状态
     *
     * @param sysArea
     * @return
     */
    boolean updateStatus(SysArea sysArea);

    /**
     * 删除行政区划信息
     *
     * @param areaUuid
     * @return
     */
    boolean deleteSysArea(String areaUuid);

}
