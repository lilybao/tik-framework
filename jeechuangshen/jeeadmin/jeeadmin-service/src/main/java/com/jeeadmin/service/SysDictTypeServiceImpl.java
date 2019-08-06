package com.jeeadmin.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jeeadmin.api.ISysDictDataService;
import com.jeeadmin.api.ISysDictTypeService;
import com.jeeadmin.entity.SysDictData;
import com.jeeadmin.entity.SysDictType;
import com.jeeadmin.mapper.SysDictTypeMapper;
import com.jeerigger.common.shiro.ShiroUtil;
import com.jeerigger.common.user.UserTypeEnum;
import com.jeerigger.frame.base.service.impl.BaseServiceImpl;
import com.jeerigger.frame.enums.FlagEnum;
import com.jeerigger.frame.exception.ValidateException;
import com.jeerigger.frame.page.PageHelper;
import com.jeerigger.frame.support.validate.ValidateUtil;
import com.jeerigger.frame.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 字典类型表 服务实现类
 * </p>
 *
 * @author wangcy
 * @since 2018-11-12
 */
@Service
public class SysDictTypeServiceImpl extends BaseServiceImpl<SysDictTypeMapper, SysDictType> implements ISysDictTypeService {
    @Autowired
    private ISysDictDataService sysDictDataService;

    @Override
    public Page<SysDictType> selectPage(PageHelper<SysDictType> pageHelper) {
        Page page = new Page<SysDictType>(pageHelper.getCurrent(), pageHelper.getSize());
        QueryWrapper<SysDictType> queryWrapper = new QueryWrapper<>();
        if (pageHelper.getData() != null) {
            SysDictType sysDictType = pageHelper.getData();
            //字典名称模糊查询
            if (StringUtil.isNotEmpty(sysDictType.getDictName())) {
                queryWrapper.lambda().like(SysDictType::getDictName, sysDictType.getDictName());
            }
            //字典类型模糊查询
            if (StringUtil.isNotEmpty(sysDictType.getDictType())) {
                queryWrapper.lambda().like(SysDictType::getDictType, sysDictType.getDictType());
            }
            //是否为系统默认
            if (StringUtil.isNotEmpty(sysDictType.getSysFlag())) {
                queryWrapper.lambda().eq(SysDictType::getSysFlag, sysDictType.getSysFlag());
            }
            //状态
            if (StringUtil.isNotEmpty(sysDictType.getDictStatus())) {
                queryWrapper.lambda().eq(SysDictType::getDictStatus, sysDictType.getDictStatus());
            }
        }
        return (Page<SysDictType>) this.page(page, queryWrapper);
    }

    @Override
    public boolean updateStatus(SysDictType sysDictType) {
        SysDictType oldDictType = this.getById(sysDictType.getUuid());
        //验证字典类型是否存在
        if (oldDictType == null) {
            throw new ValidateException("该字典类型不存在不能进行更新！");
        }
        if (oldDictType.getSysFlag().equals(FlagEnum.YES.getCode())) {
            if (!ShiroUtil.getUserData().getUserType().equals(UserTypeEnum.SUPER_ADMIN_USER)) {
                throw new ValidateException("系统内置字典不能进行状态更新！");
            }
        }
        //执行字典类型状态更新
        UpdateWrapper<SysDictType> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().set(SysDictType::getDictStatus, sysDictType.getDictStatus());
        updateWrapper.lambda().eq(SysDictType::getUuid, sysDictType.getUuid());

        boolean flag = this.update(new SysDictType(), updateWrapper);
        if (flag) {
            UpdateWrapper<SysDictData> updateDataWrapper = new UpdateWrapper<>();
            updateDataWrapper.lambda().set(SysDictData::getDictStatus, sysDictType.getDictStatus());
            updateDataWrapper.lambda().eq(SysDictData::getDictType, oldDictType.getDictType());
            sysDictDataService.update(new SysDictData(), updateDataWrapper);
        }
        return flag;
    }


    @Override
    public boolean saveDictType(SysDictType sysDictType) {
        ValidateUtil.validateObject(sysDictType);
        validateDictType(sysDictType);
        return this.save(sysDictType);
    }

    @Override
    public boolean updateDictType(SysDictType sysDictType) {
        SysDictType oldDictType = this.getById(sysDictType.getUuid());
        if (oldDictType == null) {
            throw new ValidateException("该字典类型（" + sysDictType.getDictType() + "）不存在不能进行更新！");
        }
        if (oldDictType.getSysFlag().equals(FlagEnum.YES.getCode())) {
            if (!ShiroUtil.getUserData().getUserType().equals(UserTypeEnum.SUPER_ADMIN_USER)) {
                throw new ValidateException("系统内置字典不能进行更新！");
            }
        }

        ValidateUtil.validateObject(sysDictType);
        validateDictType(sysDictType);
        boolean flag = this.updateById(sysDictType);
        if (flag && !oldDictType.getDictType().equals(sysDictType.getDictType())) {
            UpdateWrapper<SysDictData> updateWrapper = new UpdateWrapper();
            updateWrapper.lambda().set(SysDictData::getDictType, sysDictType.getDictType());
            updateWrapper.lambda().eq(SysDictData::getDictType, oldDictType.getDictType());
            sysDictDataService.update(new SysDictData(), updateWrapper);
        }
        return flag;
    }

    /**
     * 添加或更新校验字典类型是否已存在
     *
     * @param sysDictType
     */
    private void validateDictType(SysDictType sysDictType) {
        QueryWrapper<SysDictType> queryWrapper = new QueryWrapper<>();
        if (StringUtil.isNotEmpty(sysDictType.getUuid())) {
            queryWrapper.lambda().ne(SysDictType::getUuid, sysDictType.getUuid());
        }
        queryWrapper.lambda().eq(SysDictType::getDictType, sysDictType.getDictType());
        if (this.count(queryWrapper) > 0) {
            throw new ValidateException("字典类型（" + sysDictType.getDictType() + "）已存在！");
        }
    }

    @Override
    public boolean deleteDictType(String dictUuid) {
        SysDictType sysDictType = this.getById(dictUuid);
        if (sysDictType == null) {
            return true;
        } else {
            if (sysDictType.getSysFlag().equals(FlagEnum.YES.getCode())) {
                if (!ShiroUtil.getUserData().getUserType().equals(UserTypeEnum.SUPER_ADMIN_USER)) {
                    throw new ValidateException("系统内置字典不能进行删除！");
                }
            }
        }
        QueryWrapper<SysDictData> whereWrapper = new QueryWrapper<>();
        whereWrapper.lambda().eq(SysDictData::getDictType, sysDictType.getDictType());
        //删除字典数据
        if (sysDictDataService.remove(whereWrapper)) {
            //删除字典类型
            if (this.removeById(dictUuid)) {
                return true;
            }
        }
        return false;
    }
}
