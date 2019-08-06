package com.jeeadmin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jeeadmin.api.ISysDictDataService;
import com.jeeadmin.api.ISysDictTypeService;
import com.jeeadmin.entity.SysDictData;
import com.jeeadmin.entity.SysDictType;
import com.jeeadmin.mapper.SysDictDataMapper;
import com.jeerigger.common.enums.StatusEnum;
import com.jeerigger.common.shiro.ShiroUtil;
import com.jeerigger.common.user.UserTypeEnum;
import com.jeerigger.frame.base.service.impl.BaseServiceImpl;
import com.jeerigger.frame.enums.FlagEnum;
import com.jeerigger.frame.exception.ValidateException;
import com.jeerigger.frame.support.validate.ValidateUtil;
import com.jeerigger.frame.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 字典数据表 服务实现类
 * </p>
 *
 * @author wangcy
 * @since 2018-11-12
 */
@Service
public class SysDictDataServiceImpl extends BaseServiceImpl<SysDictDataMapper, SysDictData> implements ISysDictDataService {
    @Autowired
    private ISysDictTypeService sysDictTypeService;

    @Override
    public List<SysDictData> selectDictDataList(SysDictData sysDictData) {
        if (StringUtil.isEmpty(sysDictData.getDictType())) {
            throw new ValidateException("字典类型不能为空！");
        }
        QueryWrapper<SysDictData> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysDictData::getDictType, sysDictData.getDictType());
        //添加字典标签查询条件
        if (StringUtil.isNotEmpty(sysDictData.getDictLabel())) {
            queryWrapper.lambda().like(SysDictData::getDictLabel, sysDictData.getDictLabel());
        }
        //添加字典键值查询条件
        if (StringUtil.isNotEmpty(sysDictData.getDictValue())) {
            queryWrapper.lambda().like(SysDictData::getDictValue, sysDictData.getDictValue());
        }
        //添加系统内置查询条件
        if (StringUtil.isNotEmpty(sysDictData.getSysFlag())) {
            queryWrapper.lambda().like(SysDictData::getSysFlag, sysDictData.getSysFlag());
        }
        //添加状态查询条件
        if (StringUtil.isNotEmpty(sysDictData.getDictStatus())) {
            queryWrapper.lambda().like(SysDictData::getDictStatus, sysDictData.getDictStatus());
        }
        queryWrapper.lambda().orderByAsc(SysDictData::getDictSort);
        return this.list(queryWrapper);
    }


    @Override
    public boolean updateStatus(String dictUuid, String dictStatus) {
        SysDictData oldDictData = this.getById(dictUuid);
        if (oldDictData == null) {
            throw new ValidateException("更新的字典数据不存在！");
        }

        if (oldDictData.getSysFlag().equals(FlagEnum.YES.getCode())) {
            if (!ShiroUtil.getUserData().getUserType().equals(UserTypeEnum.SUPER_ADMIN_USER)) {
                throw new ValidateException("系统内置字典不能进行状态更新！");
            }
        }

        SysDictData sysDictData = new SysDictData();
        sysDictData.setUuid(dictUuid);
        sysDictData.setDictStatus(dictStatus);
        return this.updateById(sysDictData);
    }

    @Override
    public boolean saveDictData(SysDictData sysDictData) {
        if (StringUtil.isEmpty(sysDictData.getParentUuid())) {
            sysDictData.setParentUuid("0");
        }
        sysDictData.setDictStatus(StatusEnum.NORMAL.getCode());
        sysDictData.setSysFlag(FlagEnum.NO.getCode());

        //校验字典类型是否存在
        validateDictType(sysDictData.getDictType());

        //校验业务数据
        ValidateUtil.validateObject(sysDictData);
        //校验字典数据代码是否已在当前字典类型下存在
        validateDictValue(sysDictData);

        sysDictData.setCreateDate(new Date());
        sysDictData.setUpdateDate(new Date());
        return this.save(sysDictData);
    }

    @Override
    public boolean updateDictData(SysDictData sysDictData) {
        if (StringUtil.isEmpty(sysDictData.getParentUuid())) {
            sysDictData.setParentUuid("0");
        }
        SysDictData oldDictData = this.getById(sysDictData.getUuid());
        if (oldDictData == null) {
            throw new ValidateException("更新的字典数据不存在！");
        }

        if (oldDictData.getSysFlag().equals(FlagEnum.YES.getCode())) {
            if (!ShiroUtil.getUserData().getUserType().equals(UserTypeEnum.SUPER_ADMIN_USER)) {
                throw new ValidateException("系统内置字典不能进行更新！");
            }
        }

        //校验字典类型是否存在
        validateDictType(sysDictData.getDictType());

        //校验数据
        ValidateUtil.validateObject(sysDictData);
        //校验字典数据代码是否已在当前字典类型下存在
        validateDictValue(sysDictData);

        sysDictData.setUpdateDate(new Date());
        return this.updateById(sysDictData);
    }

    /**
     * 校验字典类型是否存在
     *
     * @param dictType
     */
    private void validateDictType(String dictType) {
        QueryWrapper<SysDictType> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysDictType::getDictType, dictType);
        if (sysDictTypeService.count(queryWrapper) < 1) {
            throw new ValidateException("添加的字典数据类型不存在！");
        }
    }

    /**
     * 校验字典数据代码是否已在当前字典类型下存在
     *
     * @param sysDictData
     */
    private void validateDictValue(SysDictData sysDictData) {
        QueryWrapper<SysDictData> queryWrapper = new QueryWrapper();
        if (StringUtil.isNotEmpty(sysDictData.getUuid())) {
            queryWrapper.lambda().ne(SysDictData::getUuid, sysDictData.getUuid());
        }
        queryWrapper.lambda().eq(SysDictData::getDictType, sysDictData.getDictType());
        queryWrapper.lambda().eq(SysDictData::getDictValue, sysDictData.getDictValue());
        if (this.count(queryWrapper) > 0) {
            throw new ValidateException("字典键值已在该字典类型（" + sysDictData.getDictType() + "）下存在！");
        }
    }

    @Override
    public boolean deleteDictData(String dictUuid) {
        SysDictData sysDictData = this.getById(dictUuid);
        if (sysDictData == null) {
            return true;
        } else {
            if (sysDictData.getSysFlag().equals(FlagEnum.YES.getCode())) {
                if (!ShiroUtil.getUserData().getUserType().equals(UserTypeEnum.SUPER_ADMIN_USER)) {
                    throw new ValidateException("系统内置字典不能进行更新！");
                }
            }
        }
        return this.removeById(dictUuid);
    }
}
