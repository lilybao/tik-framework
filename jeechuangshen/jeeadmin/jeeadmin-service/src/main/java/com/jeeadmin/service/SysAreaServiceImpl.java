package com.jeeadmin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jeeadmin.api.ISysAreaService;
import com.jeeadmin.entity.SysArea;
import com.jeeadmin.mapper.SysAreaMapper;
import com.jeerigger.common.enums.StatusEnum;
import com.jeerigger.frame.base.service.impl.BaseTreeServiceImpl;
import com.jeerigger.frame.exception.ValidateException;
import com.jeerigger.frame.support.validate.ValidateUtil;
import com.jeerigger.frame.util.StringUtil;
import com.jeerigger.module.sys.SysConstant;
import com.jeerigger.module.sys.util.SysDictUtil;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 行政区划表 服务实现类
 * </p>
 *
 * @author wangcy
 * @since 2018-11-12
 */
@Service
public class SysAreaServiceImpl extends BaseTreeServiceImpl<SysAreaMapper, SysArea> implements ISysAreaService {

    @Override
    public List<SysArea> selectChildArea(String areaUuid) {
        QueryWrapper<SysArea> wrapper = new QueryWrapper<>();
        if (StringUtil.isEmpty(areaUuid)) {
            areaUuid = "0";
        }
        wrapper.lambda().eq(SysArea::getParentUuid, areaUuid);

        wrapper.lambda().orderByAsc(SysArea::getParentUuid, SysArea::getAreaSort);
        return this.getListArea(wrapper);
    }

    @Override
    public List<SysArea> selectAreaList(SysArea sysArea) {
        QueryWrapper<SysArea> wrapper = new QueryWrapper<>();
        if (StringUtil.isNotEmpty(sysArea.getAreaCode())) {
            wrapper.lambda().eq(SysArea::getAreaCode, sysArea.getAreaCode());
        }
        if (StringUtil.isNotEmpty(sysArea.getAreaName())) {
            wrapper.lambda().like(SysArea::getAreaName, sysArea.getAreaName());
        }
        if (StringUtil.isNotEmpty(sysArea.getAreaStatus())) {
            wrapper.lambda().eq(SysArea::getAreaStatus, sysArea.getAreaStatus());
        }
        wrapper.lambda().orderByAsc(SysArea::getParentUuid, SysArea::getAreaSort);
        return this.getListArea(wrapper);
    }

    /**
     * 根据查询条件获取区划信息获取区域类型名称
     *
     * @param wrapper
     * @return
     */
    private List<SysArea> getListArea(QueryWrapper<SysArea> wrapper) {
        List<SysArea> areaList = this.list(wrapper);
        for (SysArea sysArea : areaList) {
            if (StringUtil.isNotEmpty(sysArea.getAreaType())) {
                String areaTypeName = SysDictUtil.getDictLable(SysConstant.SYS_AREA_TYPE, sysArea.getAreaType());
                sysArea.setAreaTypeName(areaTypeName);
            }
        }
        return areaList;
    }

    @Override
    public boolean saveSysArea(SysArea sysArea) {
        if (StringUtil.isEmpty(sysArea.getParentUuid())) {
            sysArea.setParentUuid("0");
        }
        //设置状态值为正常
        sysArea.setAreaStatus(StatusEnum.NORMAL.getCode());
        //校验业务数据
        ValidateUtil.validateObject(sysArea);
        //校验行政区划代码是否已存在
        validateAreaCode(sysArea);
        //验证上级行政区划是否存在
        validateParentUuid(sysArea.getParentUuid());
        //验证行政区划名称是否存在
        validateAreaName(sysArea);

        return this.save(sysArea);
    }

    /**
     * 验证上级上级行政区划是否存在
     *
     * @param areaUuid
     */
    private void validateParentUuid(String areaUuid) {
        if (StringUtil.isNotEmpty(areaUuid) && !areaUuid.equals("0")) {
            if (this.getById(areaUuid) == null) {
                throw new ValidateException("选择的上级行政区划不存在！");
            }
        }
    }

    @Override
    public boolean updateSysArea(SysArea sysArea) {
        if (StringUtil.isEmpty(sysArea.getParentUuid())) {
            sysArea.setParentUuid("0");
        }
        if (this.getById(sysArea.getUuid()) == null) {
            throw new ValidateException("该行政区划信息已不存在，不能进行编辑！");
        }
        //校验数据
        ValidateUtil.validateObject(sysArea);

        //校验行政区划代码是否已存在
        validateAreaCode(sysArea);
        //验证上级行政区划是否存在
        validateParentUuid(sysArea.getParentUuid());
        //验证行政区划名称是否存在
        validateAreaName(sysArea);
        return this.updateById(sysArea);
    }

    @Override
    public boolean updateStatus(SysArea sysArea) {
        if (this.getById(sysArea.getUuid()) == null) {
            throw new ValidateException("更新的行政区划不存在！");
        }
        List<String> pkList = this.getChildrenPk(sysArea.getUuid());
        pkList.add(sysArea.getUuid());
        UpdateWrapper<SysArea> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().set(SysArea::getAreaStatus, sysArea.getAreaStatus());
        updateWrapper.lambda().in(SysArea::getUuid, pkList);
        boolean updateFlag = this.update(new SysArea(), updateWrapper);
        if (updateFlag) {
            //如果将当前节点启用则将所有父级节点也进行启用
            if (sysArea.getAreaStatus().equals(StatusEnum.NORMAL.getCode())) {
                List parentPkList = this.getParentPk(sysArea.getUuid());
                if (parentPkList != null && parentPkList.size() > 0) {
                    UpdateWrapper<SysArea> updateParentWrapper = new UpdateWrapper<>();
                    updateParentWrapper.lambda().set(SysArea::getAreaStatus, StatusEnum.NORMAL.getCode());
                    updateParentWrapper.lambda().in(SysArea::getUuid, parentPkList);
                    updateFlag = this.update(new SysArea(), updateParentWrapper);
                }
            }
        }
        return updateFlag;
    }

    /**
     * 添加或者更新的时候校验行政区划代码是否已存在
     *
     * @param sysArea
     */
    private void validateAreaCode(SysArea sysArea) {
        QueryWrapper<SysArea> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysArea::getAreaCode, sysArea.getAreaCode());
        if (StringUtil.isNotEmpty(sysArea.getUuid())) {
            queryWrapper.lambda().ne(SysArea::getUuid, sysArea.getUuid());
        }
        if (this.count(queryWrapper) > 0) {
            throw new ValidateException("行政区划代码（" + sysArea.getAreaCode() + "）已存在！");
        }
    }

    /**
     * 验证同一级下名称是否存在
     */
    private void validateAreaName(SysArea sysArea) {
        QueryWrapper<SysArea> queryWrapper = new QueryWrapper<>();
        if (StringUtil.isNotEmpty(sysArea.getUuid())) {
            queryWrapper.lambda().ne(SysArea::getUuid, sysArea.getUuid());
        }
        queryWrapper.lambda().eq(SysArea::getParentUuid, sysArea.getParentUuid());
        queryWrapper.lambda().and(wrapper -> wrapper.eq(SysArea::getAreaName, sysArea.getAreaName()).or().eq(SysArea::getAreaShortName, sysArea.getAreaShortName()));
        List list = this.list(queryWrapper);
        if (list != null && list.size() > 0) {
            throw new ValidateException("行政区划名称或行政区划简称已存在，请核实！");
        }
    }

    @Override
    public boolean deleteSysArea(String areaUuid) {
        List list = this.selectChildArea(areaUuid);
        if (list != null && list.size() > 0) {
            throw new ValidateException("请先删除下级行政区划！");
        }
        return this.removeById(areaUuid);
    }

}
