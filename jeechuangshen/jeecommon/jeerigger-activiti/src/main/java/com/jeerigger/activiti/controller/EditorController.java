package com.jeerigger.activiti.controller;

import java.io.IOException;

import com.jeerigger.activiti.service.IEditorService;
import com.jeerigger.frame.base.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wf")
public class EditorController extends BaseController {

    @Autowired
    IEditorService editorService;


    @GetMapping("/editor/stencilset")
    public Object getStencilset() throws IOException {
       return editorService.getStencilset();
    }


    @GetMapping(value = "/model/{modelId}/json")
    public Object getEditorJson(@PathVariable(value ="modelId" )  String modelId)  throws IOException {
        return editorService.getEditorJson(modelId);
    }


    @PutMapping("/model/{modelId}/save")
    public void saveModel(@PathVariable(value ="modelId" ) String modelId, String name, String description,
                          String json_xml, String svg_xml)  throws IOException {
        editorService.saveModel(modelId, name, description, json_xml, svg_xml);
    }
}
