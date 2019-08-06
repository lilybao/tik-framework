package com.jeeadmin.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jeeadmin.entity.SysPost;
import com.jeerigger.frame.base.service.BaseService;
import com.jeerigger.frame.page.PageHelper;

import java.util.List;

/**
 * <p>
 * 岗位表 服务类
 * </p>
 *
 * @author wangcy
 * @since 2019-01-22
 */
public interface ISysPostService extends BaseService<SysPost> {
    /**
     * 查询系统角色列表
     *
     * @param pageHelper
     * @return
     */
    Page<SysPost> selectPage(PageHelper<SysPost> pageHelper);

    /**
     * 获取岗位列表
     * @return
     */
    List<SysPost> selectPostList();

    /**
     * 更新角色状态
     *
     * @param postUuid   角色唯一标识
     * @param postStatus 状态 0:正常 2:停用
     * @return
     */
    boolean updateStatus(String postUuid, String postStatus);

    /**
     * 保存保存
     *
     * @param sysPost
     * @return
     */
    boolean saveSysPost(SysPost sysPost);

    /**
     * 更新岗位
     *
     * @param sysPost
     * @return
     */
    boolean updateSysPost(SysPost sysPost);

    /**
     * 删除岗位
     *
     * @param postUuid
     * @return
     */
    boolean deleteSysPost(String postUuid);


}
