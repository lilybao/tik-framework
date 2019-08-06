package com.jeerigger.activiti.service.impl;/*
 * @项目名称: jeechuangshen
 * @日期: 2019/7/5 0005 上午 11:42
 * @版权: 2019 河南中审科技有限公司 Inc. All rights reserved.
 * @开发公司或单位：河南中审科技有限公司研发中心
 */

import java.io.IOException;
import java.io.InputStream;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeerigger.activiti.service.IEditorService;
import org.activiti.engine.RepositoryService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName: EditorServiceImpl
 * @Description: TODO
 * @author: libaojian
 * @date: 2019/7/5 0005 上午 11:42
 * @version: V1.0
 */
@Service
public class EditorServiceImpl implements IEditorService {

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private ObjectMapper objectMapper;
    @Override public Object getStencilset() throws IOException {
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("stencilset.json");
        String s = IOUtils.toString(resourceAsStream, "utf-8");
        Object parse = JSON.parse(s);
        return parse;
    }

    @Override public Object getEditorJson(String modelId) {
        return null;
    }

    @Override public void saveModel(String modelId, String name, String description, String json_xml, String svg_xml) {

    }
}
