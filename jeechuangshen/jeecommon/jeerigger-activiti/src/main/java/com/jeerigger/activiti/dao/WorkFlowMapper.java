package com.jeerigger.activiti.dao;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.annotation.SqlParser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * 执行sql语句
 * @param sql
 * @return
 */
@Mapper
public interface WorkFlowMapper {

	/**
	 * 执行sql语句
	 * @param sql
	 * @return
	 */
	List<Map<String,Object>> executeSql1(@Param("sql") String sql);
}
