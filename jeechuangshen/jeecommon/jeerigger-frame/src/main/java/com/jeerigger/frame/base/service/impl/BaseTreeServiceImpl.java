package com.jeerigger.frame.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jeerigger.frame.base.mapper.BaseTreeMapper;
import com.jeerigger.frame.base.model.BaseTreeModel;
import com.jeerigger.frame.base.service.BaseTreeService;
import com.jeerigger.frame.enums.FlagEnum;
import com.jeerigger.frame.exception.ValidateException;
import com.jeerigger.frame.util.StringUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * tree接口实现基类
 *
 * @param <M>
 * @param <T>
 */
public class BaseTreeServiceImpl<M extends BaseTreeMapper<T>, T extends BaseTreeModel> extends BaseServiceImpl<M, T> implements BaseTreeService<T> {


    /**
     * <p>
     * 插入一条记录 需更新父节点信息
     * </p>
     */
    @Override
    public synchronized boolean save(T entity) {
        saveParentLeafFlag(entity);
        entity.setLeafFlag(FlagEnum.YES.getCode());
        return super.save(entity);
    }

    /**
     * 设置父节点ID
     *
     * @param entity
     */
    private void setParentUuid(T entity) {
        String parentUuid = entity.getParentUuid();
        if (StringUtil.isEmpty(parentUuid)) {
            parentUuid = "0";
        }
        entity.setParentUuid(parentUuid);
    }

    /**
     * 新增修改父节点
     *
     * @param entity
     */
    private synchronized void saveParentLeafFlag(T entity) {
        this.setParentUuid(entity);
        if (!"0".equals(entity.getParentUuid())) {
            T parentEntity = this.getById(entity.getParentUuid());
            parentEntity.setLeafFlag(FlagEnum.NO.getCode());
            super.updateById(parentEntity);
        }
    }

    /**
     * 更新叶子节点
     *
     * @param entity
     */
    private synchronized void updateParentLeafFlag(T entity) {
        setParentUuid(entity);
        //如果父节点不为空 更新父节点叶子节点标识
        if (!"0".equals(entity.getParentUuid())) {
            T parentEntity = this.getById(entity.getParentUuid());
            QueryWrapper<T> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("parent_uuid", parentEntity.getUuid());
            if (this.count(queryWrapper) > 0) {
                parentEntity.setLeafFlag(FlagEnum.NO.getCode());
            } else {
                parentEntity.setLeafFlag(FlagEnum.YES.getCode());
            }
            super.updateById(parentEntity);
        }
    }

    /**
     * <p>
     * 更新记录 需判断父节点是否改变
     * </p>
     */
    @Override
    public synchronized boolean updateById(T entity) {
        boolean flag = true;
        if (entity.getUuid().equals(entity.getParentUuid())) {
            throw new ValidateException("该节点的上级节点不能是其本身！");
        }
        //判断上级节点是否为当前节点的子节点
        if (StringUtil.isNotEmpty(entity.getParentUuid()) && !entity.getParentUuid().equals("0")) {
            List<String> childrenPkList = this.getChildrenPk(entity.getUuid());
            if (childrenPkList.contains(entity.getParentUuid())) {
                throw new ValidateException("该节点的上级节点不能是该节点的子节点！");
            }
        }
        T oldEntity = this.getById(entity.getUuid());
        if (oldEntity != null) {
            if (!oldEntity.getParentUuid().equals(entity.getParentUuid())) {
                saveParentLeafFlag(entity);
            }
            if (flag = super.updateById(entity)) {
                updateParentLeafFlag(oldEntity);
            }
        }
        return flag;
    }


    /**
     * <p>
     * 根据 ID 删除 需更新父节点信息
     * </p>
     */
    @Override
    public synchronized boolean removeById(Serializable id) {
        T entity = this.getById(id);
        boolean flag = true;
        if (entity != null) {
            if (flag = super.removeById(id)) {
                updateParentLeafFlag(entity);
            }
        }
        return flag;
    }

    @Override
    public List<String> getChildrenPk(Serializable uuid) {
        List<String> childAllList = new ArrayList<String>();
        List<BaseTreeModel> childList = queryList(uuid);
        getListChild(childAllList, childList);
        return childAllList;
    }

    @Override
    public List<String> getParentPk(Serializable uuid) {
        List<String> parentAllList = new ArrayList<String>();
        T entity = this.getById(uuid);
        if (entity != null && !entity.getParentUuid().equals("0")) {
            getListParent(parentAllList, entity.getParentUuid());
        }
        return parentAllList;
    }

    /**
     * 递归获取所有上级UUID
     *
     * @param parentAllList
     * @param parentAllList
     */
    private void getListParent(List<String> parentAllList, Serializable parentUuid) {
        T parentEntity = this.getById(parentUuid);
        if (parentEntity != null && StringUtil.isNotEmpty(parentEntity.getParentUuid())) {
            parentAllList.add(parentEntity.getUuid());
            if (!parentEntity.getParentUuid().equals("0")) {
                getListParent(parentAllList, parentEntity.getParentUuid());
            } else {
                return;
            }
        } else {
            return;
        }
    }

    /**
     * 递归获取所有下级UUID
     *
     * @param childAllList
     * @param childList
     */
    private void getListChild(List<String> childAllList, List<BaseTreeModel> childList) {
        for (BaseTreeModel model : childList) {
            List<BaseTreeModel> list = queryList(model.getUuid());
            if (list != null && list.size() > 0) {
                getListChild(childAllList, list);
            }
            childAllList.add(model.getUuid());
        }
    }

    private List<BaseTreeModel> queryList(Serializable uuid) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.in("parent_uuid", uuid);
        wrapper.select("uuid");
        return (List<BaseTreeModel>) this.list(wrapper);
    }
}
