(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["TemplateDynamicList"],{c621:function(t,a,e){"use strict";var n=e("9efd"),i="/api/template",s={findByType:function(t){return Object(n["a"])({url:"".concat(i,"/find/").concat(t),method:"get"})},setStatus:function(t){return Object(n["a"])({url:"".concat(i,"/setStatus/").concat(t),method:"get"})},list:function(t){return Object(n["a"])({url:i,params:t,method:"get"})},create:function(t){return Object(n["a"])({url:i,data:t,method:"post"})}};a["a"]=s},dd38:function(t,a,e){"use strict";e.r(a);var n=function(){var t=this,a=t.$createElement,e=t._self._c||a;return e("div",[e("a-table",{attrs:{columns:t.columns,dataSource:t.template,pagination:!1,rowKey:function(t){return t.id}},scopedSlots:t._u([{key:"status",fn:function(a,n){return e("div",{},[e("a-switch",{attrs:{defaultChecked:""},on:{change:function(a){return t.onChangeStatus(n.id)}},model:{value:n.status,callback:function(a){t.$set(n,"status",a)},expression:"record.status"}})],1)}},{key:"action",fn:function(a,n){return e("span",{},[e("a",{attrs:{href:"javascript:;"},on:{click:function(a){return t.handleEditClick(n)}}},[t._v("编辑")]),e("a-divider",{attrs:{type:"vertical"}}),e("a",{attrs:{href:"javascript:;"},on:{click:function(a){return t.handleShowPostSettings(n)}}},[t._v("设置")])],1)}}])}),e("div",{staticClass:"page-wrapper",style:{textAlign:"right"}},[e("a-pagination",{staticClass:"pagination",attrs:{current:t.pagination.page,total:t.pagination.total,defaultPageSize:t.pagination.size,pageSizeOptions:["1","2","5","10","20","50","100"],showSizeChanger:""},on:{showSizeChange:t.handlePaginationChange,change:t.handlePaginationChange}})],1)],1)},i=[],s=e("c621"),o=[{title:"模板名称",dataIndex:"name",key:"name"},{title:"英文名称",key:"enName",dataIndex:"enName"},{title:"模板类型",dataIndex:"type",key:"type"},{title:"视图路径",dataIndex:"path",key:"path"},{title:"视图名称",dataIndex:"viewName",key:"viewName"},{title:"是否展示在主页",dataIndex:"status",key:"status",scopedSlots:{customRender:"status"}},{title:"创建时间",dataIndex:"createDate",key:"createDate"},{title:"Action",key:"action",scopedSlots:{customRender:"action"}}],r={data:function(){return{pagination:{page:1,size:5,sort:null},queryParam:{page:0,size:10,sort:null,keyword:null,categoryId:null,status:null},columns:o,article:[],template:[]}},created:function(){this.loadTemplate()},methods:{loadTemplate:function(){var t=this;this.queryParam.page=this.pagination.page-1,this.queryParam.size=this.pagination.size,this.queryParam.sort=this.pagination.sort,s["a"].list(this.queryParam).then((function(a){t.template=a.data.data.content,t.pagination.total=a.data.data.totalElements}))},handlePaginationChange:function(t,a){this.pagination.page=t,this.pagination.size=a,this.loadTemplate()},onChangeStatus:function(t){var a=this;s["a"].setStatus(t).then((function(t){a.$notification["success"]({message:"操作"+t.data.message})}))}}},c=r,u=e("2877"),l=Object(u["a"])(c,n,i,!1,null,null,null);a["default"]=l.exports}}]);
//# sourceMappingURL=TemplateDynamicList.0f41fa86.js.map