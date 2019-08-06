package com.jeerigger.frame.base.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jeerigger.frame.base.mapper.BaseMapper;
import com.jeerigger.frame.base.model.BaseModel;
import com.jeerigger.frame.base.service.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 接口实现基类
 * @param <M>
 * @param <T>
 */
public abstract class BaseServiceImpl<M extends BaseMapper<T>,T extends BaseModel> extends ServiceImpl<M,T> implements BaseService<T> {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
}
