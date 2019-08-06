webpackJsonp([26],{Fq6D:function(e,t){},Ob5x:function(e,t,r){"use strict";var a=r("woOf"),s=r.n(a),l=r("Xxa5"),o=r.n(l),n=r("exGp"),i=r.n(n),u=r("Dd8w"),c=r.n(u),m=r("t5DY"),p=r("4w6b"),d=r("RbaZ"),f=r("E4LH"),g={loginName:[{required:!0,message:"请输入",trigger:"blur"},{min:6,max:20,message:"请输入最少要输入6个字符，不超过20个字符",trigger:"blur"},{pattern:/^\w+$/,message:"只能包括英文字母、数字和下划线",trigger:"blur"}],userEmail:[{pattern:f.a,message:"请输入正确的邮箱",trigger:"blur"}],userMobile:[{pattern:f.c,message:"请输入正确的手机号",trigger:"blur"}],userPhone:[{validator:f.d,trigger:"blur"}],userSort:[{validator:Object(f.b)(0,10),trigger:"blur"},{pattern:/^\d+$/,message:"只能包括数字",trigger:"blur"}],userNumber:[{required:!0,message:"请输入",trigger:"blur"},{min:4,max:32,message:"请输入最少要输入4个字符，不超过32个字符",trigger:"blur"},{pattern:/^[A-Za-z\d]+$/,message:"只能包括英文字母和数字",trigger:"blur"}],userName:[{required:!0,message:"请输入",trigger:"blur"},{min:1,max:10,message:"请输入不超过10个字符",trigger:"blur"}],remarks:[{max:150,message:"请输入不超过150个字符",trigger:"change"}]},h={name:"UserTypeForm",props:{isEdit:{type:Boolean,default:!1}},data:function(){var e=this;return{postList:[],ruleForm:{loginName:"",userEmail:"",userMobile:"",userPhone:"",userNumber:"",userName:"",remarks:"",postUuidList:[]},orgTypeList:[],orgAll:[],rolesAll:[],rules:c()({},g,{orgName:[{required:!0,message:"请选择",trigger:"change"},{validator:function(t){var r=arguments.length>1&&void 0!==arguments[1]?arguments[1]:"",a=arguments[2];e.orgAll.length>0&&""!==r?e.orgAll.some(function(e){return e.orgName===r})?a():a(new Error("没有找到对应的机构")):a(new Error("请选择"))},trigger:"blur"}]}),tree:[],defaultProps:{children:"children",label:"orgName"},multipleSelection:[],currentNodeKey:"",loading:!1}},computed:{currentNodeKeys:function(){var e=[];return this.currentNodeKey&&(e[0]=this.currentNodeKey),e}},created:function(){var e=this;return i()(o.a.mark(function t(){return o.a.wrap(function(t){for(;;)switch(t.prev=t.next){case 0:e.getOrgAll(),e.getRolesAll(),e.getPostList(),e.initForm();case 4:case"end":return t.stop()}},t,e)}))()},methods:{initForm:function(){var e=this.$route.query,t=e.orgUuid,r=e.orgName,a=e.uuid;a&&(this.uuid=a),"undefined"!==t&&"undefined"!==r&&(this.currentNodeKey=t,this.ruleForm=s()({},this.ruleForm,{orgUuid:t,orgName:r})),this.isEdit&&this.getDetail(a)},getPostList:function(){var e=this;return i()(o.a.mark(function t(){var r;return o.a.wrap(function(t){for(;;)switch(t.prev=t.next){case 0:return t.next=2,Object(p.j)();case 2:"0000"===(r=t.sent).code&&(e.postList=r.data.records);case 4:case"end":return t.stop()}},t,e)}))()},treeShow:function(){this.currentNodeKey&&this.$refs.tree.setCurrentKey(this.currentNodeKey)},handleSelectionChange:function(e){console.log(e),this.multipleSelection=e.map(function(e){return e.uuid})},getRolesAll:function(){var e=this;return i()(o.a.mark(function t(){var r;return o.a.wrap(function(t){for(;;)switch(t.prev=t.next){case 0:return t.next=2,Object(p.r)();case 2:"0000"===(r=t.sent).code&&(e.rolesAll=r.data);case 4:case"end":return t.stop()}},t,e)}))()},getDetail:function(e){var t=this;return i()(o.a.mark(function r(){var a,s;return o.a.wrap(function(r){for(;;)switch(r.prev=r.next){case 0:return r.next=2,Object(p.o)({userUuid:e});case 2:"0000"===(a=r.sent).code&&((s=a.data).orgUuid&&(t.currentNodeKey=s.orgUuid),s.postUuidList||(s.postUuidList=[]),t.ruleForm=s);case 4:case"end":return r.stop()}},r,t)}))()},filterInput:function(){this.$refs.tree.filter(this.ruleForm.orgName)},filterNode:function(e,t,r){return!e||-1!==t.orgName.indexOf(e)},dataToTree:function(e){return Object(d.a)(e)},getOrgAll:function(){var e=this;return i()(o.a.mark(function t(){var r;return o.a.wrap(function(t){for(;;)switch(t.prev=t.next){case 0:return t.next=2,Object(m.c)();case 2:"0000"===(r=t.sent).code&&(e.orgAll=r.data,e.tree=e.dataToTree(r.data));case 4:case"end":return t.stop()}},t,e)}))()},close:function(){this.jumpRouterCachedView(this.$route,{path:"/sys/user"})},handleNodeClick:function(e){this.ruleForm=s()({},this.ruleForm,{orgUuid:e.uuid,orgName:e.orgName})},validateSubmit:function(e){var t,r=this;this.$refs.ruleForm.validate((t=i()(o.a.mark(function t(a){return o.a.wrap(function(t){for(;;)switch(t.prev=t.next){case 0:if(!a){t.next=4;break}r.submit(e),t.next=5;break;case 4:return t.abrupt("return",!1);case 5:case"end":return t.stop()}},t,r)})),function(e){return t.apply(this,arguments)}))},submit:function(e){var t=this;return i()(o.a.mark(function r(){var a,s;return o.a.wrap(function(r){for(;;)switch(r.prev=r.next){case 0:if(e.roleUuidList=t.multipleSelection,e.orgName||delete e.orgUuid,t.loading=!0,!t.isEdit){r.next=10;break}return r.next=6,Object(p.u)(e);case 6:"0000"===(a=r.sent).code&&(t.$message({message:a.message,type:"success"}),t.jumpRouterView(t.$route,{path:"/sys/user"})),r.next=14;break;case 10:return r.next=12,Object(p.m)(e);case 12:"0000"===(s=r.sent).code&&(t.$message({message:s.message,type:"success"}),t.jumpRouterView(t.$route,{path:"/sys/user"}));case 14:t.loading=!1;case 15:case"end":return r.stop()}},r,t)}))()}}},b={render:function(){var e=this,t=e.$createElement,r=e._self._c||t;return r("div",{staticClass:"jee-page"},[r("el-form",{ref:"ruleForm",attrs:{model:e.ruleForm,rules:e.rules,"label-width":"110px"}},[r("h3",{staticClass:"jee-page-h3-title"},[e._v("基本信息")]),e._v(" "),r("div",{staticClass:"first-section"},[r("el-row",[r("el-col",{attrs:{offset:1,span:10}},[r("el-form-item",{attrs:{label:"归属机构：",prop:"orgName"}},[r("el-popover",{attrs:{placement:"bottom-start",width:"300",trigger:"click"},on:{show:e.treeShow}},[r("el-tree",{ref:"tree",staticStyle:{width:"270px","max-height":"200px","overflow-y":"scroll"},attrs:{data:e.tree,"default-expanded-keys":e.currentNodeKeys,"filter-node-method":e.filterNode,"highlight-current":!0,props:e.defaultProps,"node-key":"uuid"},on:{"node-click":e.handleNodeClick},scopedSlots:e._u([{key:"default",fn:function(t){var a=t.node;return r("span",{staticClass:"custom-tree-node",attrs:{title:a.label}},[e._v("\n                  "+e._s(a.label)+"\n                ")])}}])}),e._v(" "),r("el-input",{attrs:{slot:"reference",size:"small",clearable:""},on:{input:e.filterInput},slot:"reference",model:{value:e.ruleForm.orgName,callback:function(t){e.$set(e.ruleForm,"orgName",t)},expression:"ruleForm.orgName"}},[r("template",{slot:"append"},[r("span",{staticClass:"fa fa-search"})])],2)],1)],1)],1)],1),e._v(" "),r("el-row",[r("el-col",{attrs:{offset:1,span:10}},[r("el-form-item",{attrs:{label:"账号：",prop:"loginName"}},[r("el-input",{attrs:{clearable:"",maxlength:"20",placeholder:"只能包括英文字母、数字和下划线，最少要输入6个字符，不超过20个字符",size:"small"},model:{value:e.ruleForm.loginName,callback:function(t){e.$set(e.ruleForm,"loginName",t)},expression:"ruleForm.loginName"}})],1)],1),e._v(" "),r("el-col",{attrs:{offset:1,span:10}},[r("el-form-item",{attrs:{label:"电子邮箱：",prop:"userEmail"}},[r("el-input",{attrs:{size:"small",clearable:""},model:{value:e.ruleForm.userEmail,callback:function(t){e.$set(e.ruleForm,"userEmail",t)},expression:"ruleForm.userEmail"}},[r("template",{slot:"append"},[r("span",{staticClass:"fa fa-fw fa-envelope"})])],2)],1)],1)],1),e._v(" "),r("el-row",[r("el-col",{attrs:{offset:1,span:10}},[r("el-form-item",{attrs:{label:"手机号码：",prop:"userMobile"}},[r("el-input",{attrs:{size:"small",maxlength:"20",clearable:""},model:{value:e.ruleForm.userMobile,callback:function(t){e.$set(e.ruleForm,"userMobile",t)},expression:"ruleForm.userMobile"}},[r("template",{slot:"append"},[r("span",{staticClass:"fa fa-fw fa-mobile"})])],2)],1)],1),e._v(" "),r("el-col",{attrs:{offset:1,span:10}},[r("el-form-item",{attrs:{label:"办公电话：",prop:"userPhone"}},[r("el-input",{attrs:{size:"small",maxlength:"13",clearable:""},model:{value:e.ruleForm.userPhone,callback:function(t){e.$set(e.ruleForm,"userPhone",t)},expression:"ruleForm.userPhone"}},[r("template",{slot:"append"},[r("span",{staticClass:"fa fa-fw fa-phone"})])],2)],1)],1)],1),e._v(" "),r("el-row",[r("el-col",{attrs:{offset:1,span:10}},[r("el-form-item",{attrs:{label:"权重(排序)：",prop:"userSort"}},[r("el-input",{attrs:{type:"number",size:"small",maxlength:"9",clearable:"",placeholder:"排序号越大排名越靠后，请填写数字"},model:{value:e.ruleForm.userSort,callback:function(t){e.$set(e.ruleForm,"userSort",e._n(t))},expression:"ruleForm.userSort"}})],1)],1)],1)],1),e._v(" "),r("h3",{staticClass:"jee-page-h3-title"},[e._v("详细信息")]),e._v(" "),r("div",{staticClass:"first-section"},[r("el-row",[r("el-col",{attrs:{offset:1,span:10}},[r("el-form-item",{attrs:{label:"员工编号：",prop:"userNumber"}},[r("el-input",{attrs:{size:"small",maxlength:"32",clearable:"",placeholder:"包含英文字母、数字，长度不小于4位，不大于32位"},model:{value:e.ruleForm.userNumber,callback:function(t){e.$set(e.ruleForm,"userNumber",t)},expression:"ruleForm.userNumber"}})],1)],1),e._v(" "),r("el-col",{attrs:{offset:1,span:10}},[r("el-form-item",{attrs:{label:"员工姓名：",prop:"userName"}},[r("el-input",{attrs:{size:"small",maxlength:"10",clearable:"",placeholder:"不大于10位"},model:{value:e.ruleForm.userName,callback:function(t){e.$set(e.ruleForm,"userName",t)},expression:"ruleForm.userName"}})],1)],1)],1),e._v(" "),r("el-row",[r("el-col",{attrs:{offset:1,span:10}},[r("el-form-item",{attrs:{label:"员工岗位：",prop:"postUuidList"}},[r("el-select",{staticStyle:{width:"100%"},attrs:{multiple:"","value-key":"uuid",placeholder:"请选择"},model:{value:e.ruleForm.postUuidList,callback:function(t){e.$set(e.ruleForm,"postUuidList",t)},expression:"ruleForm.postUuidList"}},e._l(e.postList,function(e){return r("el-option",{key:e.uuid,attrs:{label:e.postName,value:e.uuid}})}))],1)],1)],1),e._v(" "),r("el-row",[r("el-col",{attrs:{offset:1,span:21}},[r("el-form-item",{attrs:{label:"备注信息：",prop:"remarks"}},[r("el-input",{attrs:{type:"textarea",size:"small",rows:"4"},model:{value:e.ruleForm.remarks,callback:function(t){e.$set(e.ruleForm,"remarks",t)},expression:"ruleForm.remarks"}})],1)],1)],1)],1),e._v(" "),e.isEdit?e._e():[r("h3",{staticClass:"jee-page-h3-title"},[e._v("分配角色")]),e._v(" "),r("el-row",{staticClass:"second-section"},[r("el-col",{attrs:{offset:1,span:21}},[r("el-table",{staticStyle:{width:"100%"},attrs:{data:e.rolesAll,border:""},on:{"selection-change":e.handleSelectionChange}},[r("el-table-column",{attrs:{type:"selection",align:"center",width:"50"}}),e._v(" "),r("el-table-column",{attrs:{prop:"roleName",align:"center",label:"角色名称"}}),e._v(" "),r("el-table-column",{attrs:{prop:"roleCode",align:"center",label:"角色编码"}})],1)],1)],1)],e._v(" "),r("div",{staticClass:"jee-submit-buttons",staticStyle:{"padding-left":"116px"}},[r("el-row",[r("el-col",{attrs:{offset:1,span:21}},[r("el-button",{staticClass:"fa fa-check",attrs:{loading:e.loading,type:"primary",size:"small"},on:{click:function(t){e.validateSubmit(e.ruleForm)}}},[e._v("\n            保存\n          ")]),e._v(" "),r("el-button",{staticClass:"fa fa-reply-all",attrs:{size:"small"},on:{click:e.close}},[e._v("\n            关闭\n          ")])],1)],1)],1)],2)],1)},staticRenderFns:[]};var v=r("VU/8")(h,b,!1,function(e){r("bQOZ")},"data-v-03b32892",null);t.a=v.exports},bQOZ:function(e,t){},"fx/j":function(e,t,r){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var a={name:"UserAdd",components:{TypeForm:r("Ob5x").a},data:function(){return{}},methods:{}},s={render:function(){var e=this.$createElement,t=this._self._c||e;return t("div",{staticClass:"jee-page user-add"},[t("div",{staticClass:"jee-header"},[t("div",{staticClass:"jee-title"},[t("i",{class:this.$route.meta.icon}),this._v(" "+this._s(this.$route.meta.title))])]),this._v(" "),t("div",{staticClass:"jee-page-container",staticStyle:{height:"calc(100% - 50px)"}},[t("TypeForm",{attrs:{"is-edit":!1}})],1)])},staticRenderFns:[]};var l=r("VU/8")(a,s,!1,function(e){r("Fq6D")},"data-v-3f65560d",null);t.default=l.exports}});