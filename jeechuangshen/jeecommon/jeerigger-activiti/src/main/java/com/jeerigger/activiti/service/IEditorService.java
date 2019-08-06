package com.jeerigger.activiti.service;/*
 * @项目名称: jeechuangshen
 * @日期: 2019/7/5 0005 上午 11:34
 * @版权: 2019 河南中审科技有限公司 Inc. All rights reserved.
 * @开发公司或单位：河南中审科技有限公司研发中心
 */

import java.io.IOException;

/**
 * @ClassName: IEditorService
 * @Description: TODO
 * @author: libaojian
 * @date: 2019/7/5 0005 上午 11:34
 * @version: V1.0
 */
public interface IEditorService {
     Object getStencilset() throws IOException;
     Object getEditorJson(String modelId);
     void saveModel(String modelId, String name, String description,String json_xml, String svg_xml);
}
