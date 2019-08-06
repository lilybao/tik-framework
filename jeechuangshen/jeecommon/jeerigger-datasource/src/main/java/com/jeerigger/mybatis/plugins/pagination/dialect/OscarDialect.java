package com.jeerigger.mybatis.plugins.pagination.dialect;

import com.baomidou.mybatisplus.extension.plugins.pagination.DialectModel;
import com.baomidou.mybatisplus.extension.plugins.pagination.dialects.IDialect;

/**
 * 神通数据库分页辅助类
 */
public class OscarDialect implements IDialect {
    @Override
    public DialectModel buildPaginationSql(String originalSql, long offset, long limit) {
        limit = offset >= 1L ? offset + limit : limit;
        String sql = "SELECT * FROM ( SELECT TMP.*, ROWNUM ROW_ID FROM ( " + originalSql + " ) TMP WHERE ROWNUM <=" + "?" + ") WHERE ROW_ID > " + "?";
        return (new DialectModel(sql, limit, offset)).setConsumerChain();
    }
}
