webpackJsonp([24],{Fvwy:function(e,t){},Zcyz:function(e,t){},w58g:function(e,t,r){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var s={name:"DictAdd",components:{typeForm:r("y5BB").a}},a={render:function(){var e=this.$createElement,t=this._self._c||e;return t("div",{staticClass:"jee-page"},[t("div",{staticClass:"jee-header"},[t("div",{staticClass:"jee-title"},[t("i",{class:this.$route.meta.icon}),this._v(" "+this._s(this.$route.meta.title))])]),this._v(" "),t("div",{staticClass:"jee-body"},[t("type-form")],1)])},staticRenderFns:[]};var i=r("VU/8")(s,a,!1,function(e){r("Fvwy")},"data-v-07756090",null);t.default=i.exports},y5BB:function(e,t,r){"use strict";var s=r("Xxa5"),a=r.n(s),i=r("exGp"),o=r.n(i),n=r("Dd8w"),l=r.n(n),c=r("M4fF"),u=r.n(c),m=r("V/8j"),d={dictName:[{required:!0,message:"请输入字典名称",trigger:"blur"},{min:3,max:30,message:"请输入3-30个字符",trigger:"blur"}],dictType:[{required:!0,message:"请输入字典类型",trigger:"blur"},{min:3,max:30,message:"请输入3-30个字符",trigger:"blur"},{pattern:/^\w+$/,message:"只能包括英文字母、数字和下划线",trigger:["blur","change"]}],sysFlag:[{required:!0,message:"请选择",trigger:"change"}],remarks:[{max:150,message:"请输入不超过150个字符",trigger:"change"}]},f={props:{isEdit:{type:Boolean,default:!1}},data:function(){return{form:{dictName:"",dictType:"",sysFlag:"0",remarks:""},rules:l()({},d),loading:!1}},mounted:function(){this.initForm()},methods:{initForm:function(){this.$refs.form.resetFields(),this.isEdit&&(this.$route.query.dictUuid?this.getDetail(this.$route.query.dictUuid):this.jumpRouterCachedView(this.$route,{path:"/sys/dict"}))},getDetail:function(e){var t=this;return o()(a.a.mark(function r(){var s,i;return a.a.wrap(function(r){for(;;)switch(r.prev=r.next){case 0:return r.next=2,Object(m.v)({dictUuid:e});case 2:if("0000"===(s=r.sent).code&&s.data)for(i in s.data)s.data.hasOwnProperty(i)&&null!==s.data[i]&&(t.form[i]=s.data[i]);case 4:case"end":return r.stop()}},r,t)}))()},save:function(){var e=this;this.$refs.form.validate(function(t){if(t){var r=u.a.cloneDeep(e.form);e.isEdit?e.submit(m.n,r):e.submit(m.c,r)}})},submit:function(e,t){var r=this;return o()(a.a.mark(function s(){var i;return a.a.wrap(function(s){for(;;)switch(s.prev=s.next){case 0:return r.loading=!0,s.next=3,e(t);case 3:i=s.sent,r.loading=!1,"0000"===i.code&&(r.$message({type:"success",message:"操作成功"}),r.jumpRouterView(r.$route,{path:"/sys/dict"}));case 6:case"end":return s.stop()}},s,r)}))()},close:function(){this.jumpRouterCachedView(this.$route,{path:"/sys/dict"})}}},p={render:function(){var e=this,t=e.$createElement,r=e._self._c||t;return r("el-form",{ref:"form",attrs:{model:e.form,rules:e.rules,"label-width":"100px",size:"small"}},[r("div",{staticClass:"jee-form-title"},[e._v("基本信息")]),e._v(" "),r("el-row",{attrs:{gutter:10}},[r("el-col",{attrs:{offset:1,span:10}},[r("el-form-item",{attrs:{label:"字典名称：",prop:"dictName"}},[r("el-input",{attrs:{maxlength:"30"},model:{value:e.form.dictName,callback:function(t){e.$set(e.form,"dictName",t)},expression:"form.dictName"}})],1)],1)],1),e._v(" "),r("el-row",{attrs:{gutter:10}},[r("el-col",{attrs:{offset:1,span:10}},[r("el-form-item",{attrs:{label:"字典类型：",prop:"dictType"}},[r("el-input",{attrs:{minlength:"3",maxlength:"30"},model:{value:e.form.dictType,callback:function(t){e.$set(e.form,"dictType",t)},expression:"form.dictType"}})],1)],1)],1),e._v(" "),r("el-row",{attrs:{gutter:10}},[r("el-col",{attrs:{offset:1,span:10}},[r("el-form-item",{attrs:{label:"是否系统：",prop:"sysFlag"}},[r("el-radio-group",{attrs:{disabled:""},model:{value:e.form.sysFlag,callback:function(t){e.$set(e.form,"sysFlag",t)},expression:"form.sysFlag"}},[r("el-radio",{attrs:{label:"1"}},[e._v("是")]),e._v(" "),r("el-radio",{attrs:{label:"0"}},[e._v("否")])],1)],1)],1)],1),e._v(" "),r("el-row",{attrs:{gutter:10}},[r("el-col",{attrs:{offset:1,span:21}},[r("el-form-item",{attrs:{label:"备注信息：",prop:"remarks"}},[r("el-input",{attrs:{type:"textarea",rows:"3"},model:{value:e.form.remarks,callback:function(t){e.$set(e.form,"remarks",t)},expression:"form.remarks"}})],1)],1)],1),e._v(" "),r("el-row",{staticClass:"jee-submit-buttons"},[r("el-col",{attrs:{offset:1,span:21}},[r("el-button",{attrs:{loading:e.loading,type:"primary",icon:"fa fa-check",size:"small"},on:{click:e.save}},[e._v("  保 存\n      ")]),e._v(" "),r("el-button",{attrs:{icon:"fa fa-reply-all",size:"small"},on:{click:e.close}},[e._v("  关 闭")])],1)],1)],1)},staticRenderFns:[]};var g=r("VU/8")(f,p,!1,function(e){r("Zcyz")},"data-v-297b7362",null);t.a=g.exports}});