package com.jeerigger.module.sys.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jeerigger.frame.base.service.impl.BaseServiceImpl;
import com.jeerigger.frame.util.StringUtil;
import com.jeerigger.module.sys.api.ISysUserPostService;
import com.jeerigger.module.sys.entity.SysUserPost;
import com.jeerigger.module.sys.mapper.SysUserPostMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wangcy
 * @since 2019-01-22
 */
@Service
public class SysUserPostServiceImpl extends BaseServiceImpl<SysUserPostMapper, SysUserPost> implements ISysUserPostService {

    @Override
    public boolean saveUserPost(List<SysUserPost> sysUserPostList) {
        return this.saveBatch(sysUserPostList);
    }

    @Override
    public boolean deleteUserPost(String userUuid) {
        if (StringUtil.isNotEmpty(userUuid)) {
            QueryWrapper<SysUserPost> whereWrapper = new QueryWrapper<>();
            whereWrapper.lambda().eq(SysUserPost::getUserUuid, userUuid);
            return this.remove(whereWrapper);
        } else {
            return true;
        }
    }

    @Override
    public List<SysUserPost> detailPostList(String userUuid) {
        QueryWrapper<SysUserPost> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysUserPost::getUserUuid, userUuid);
        return this.list(queryWrapper);
    }
}
