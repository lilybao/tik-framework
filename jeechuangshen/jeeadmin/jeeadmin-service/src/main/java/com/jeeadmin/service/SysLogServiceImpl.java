package com.jeeadmin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jeeadmin.api.ISysLogService;
import com.jeeadmin.entity.SysLog;
import com.jeeadmin.mapper.SysLogMapper;
import com.jeerigger.frame.base.service.impl.BaseServiceImpl;
import com.jeerigger.frame.page.PageHelper;
import com.jeerigger.frame.util.StringUtil;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 操作日志表 服务实现类
 * </p>
 *
 * @author wangcy
 * @since 2018-11-12
 */
@Service
public class SysLogServiceImpl extends BaseServiceImpl<SysLogMapper, SysLog> implements ISysLogService {

    @Override
    public Page<SysLog> selectPage(PageHelper<SysLog> pageHelper) {
        Page<SysLog> page = new Page<>(pageHelper.getCurrent(), pageHelper.getSize());
        QueryWrapper<SysLog> queryWrapper = new QueryWrapper<>();
        if (pageHelper.getData() != null) {
            SysLog sysLog = pageHelper.getData();
            if (StringUtil.isNotEmpty(sysLog.getLogTitle())) {
                queryWrapper.lambda().like(SysLog::getLogTitle, sysLog.getLogTitle());
            }
            if (StringUtil.isNotEmpty(sysLog.getLogType())) {
                queryWrapper.lambda().like(SysLog::getLogType, sysLog.getLogType());
            }
            if (StringUtil.isNotEmpty(sysLog.getRequestUri())) {
                queryWrapper.lambda().like(SysLog::getRequestUri, sysLog.getRequestUri());
            }
            if (StringUtil.isNotEmpty(sysLog.getUserName())) {
                queryWrapper.lambda().like(SysLog::getUserName, sysLog.getUserName());
            }
            if (StringUtil.isNotEmpty(sysLog.getExceptionFlag())) {
                queryWrapper.lambda().eq(SysLog::getExceptionFlag, sysLog.getExceptionFlag());
            }
            if (StringUtil.isNotEmpty(sysLog.getRemoteAddr())) {
                queryWrapper.lambda().eq(SysLog::getRemoteAddr, sysLog.getRemoteAddr());
            }

            if (sysLog.getExecuteDateStart() != null && sysLog.getExecuteDateEnd() != null) {
                queryWrapper.lambda().between(SysLog::getCreateDate, sysLog.getExecuteDateStart(), sysLog.getExecuteDateEnd());
            } else {
                if (sysLog.getExecuteDateStart() != null) {
                    queryWrapper.lambda().ge(SysLog::getCreateDate, sysLog.getExecuteDateStart());
                }

                if (sysLog.getExecuteDateEnd() != null) {
                    queryWrapper.lambda().le(SysLog::getCreateDate, sysLog.getExecuteDateEnd());
                }
            }
        }
        queryWrapper.lambda().orderByDesc(SysLog::getCreateDate);
        return (Page<SysLog>) this.page(page, queryWrapper);
    }
}
