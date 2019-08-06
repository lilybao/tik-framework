package com.jeeadmin.api;

import com.jeeadmin.entity.SysDictData;
import com.jeerigger.frame.base.service.BaseService;

import java.util.List;

/**
 * <p>
 * 字典数据表 服务类
 * </p>
 *
 * @author wangcy
 * @since 2018-11-12
 */
public interface ISysDictDataService extends BaseService<SysDictData> {
    /**
     * 查询字典数据列表
     *
     * @param sysDictData 查询条件
     * @return
     */
    List<SysDictData> selectDictDataList(SysDictData sysDictData);

    /**
     * 更新字典数据
     *
     * @param dictDataUuid   字典数据主键
     * @param dictStatus 状态 0:正常 1:删除 2:停用
     * @return
     */
    boolean updateStatus(String dictDataUuid, String dictStatus);

    /**
     * 新增字典数据
     *
     * @param sysDictData
     * @return
     */
    boolean saveDictData(SysDictData sysDictData);

    /**
     * 更新字典数据
     *
     * @param sysDictData
     * @return
     */
    boolean updateDictData(SysDictData sysDictData);

    /**
     * 更新字典数据
     *
     * @param dictDataUuid 字典数据唯一标识
     * @return
     */
    boolean deleteDictData(String dictDataUuid);


}
