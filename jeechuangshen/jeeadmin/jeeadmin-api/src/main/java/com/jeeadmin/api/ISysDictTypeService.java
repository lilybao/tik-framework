package com.jeeadmin.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jeeadmin.entity.SysDictType;
import com.jeerigger.frame.page.PageHelper;

/**
 * <p>
 * 字典类型表 服务类
 * </p>
 *
 * @author wangcy
 * @since 2018-11-12
 */
public interface ISysDictTypeService extends IService<SysDictType> {
    /**
     * 查询字典类型数据
     *
     * @param pageHelper
     * @return
     */
    Page<SysDictType> selectPage(PageHelper<SysDictType> pageHelper);

    /**
     * 更新字典类型状态
     *
     * @param sysDictType
     * @return
     */
    boolean updateStatus(SysDictType sysDictType);

    /**
     * 新增字典类型
     *
     * @param sysDictType
     * @return
     */
    boolean saveDictType(SysDictType sysDictType);

    /**
     * 更新字典类型
     *
     * @param sysDictType
     * @return
     */
    boolean updateDictType(SysDictType sysDictType);

    /**
     * 删除字典类型
     *
     * @param dictUuid 字典类型UUID
     * @return
     */
    boolean deleteDictType(String dictUuid);

}
