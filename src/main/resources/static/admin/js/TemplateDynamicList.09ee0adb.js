(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["TemplateDynamicList"],{c621:function(t,a,e){"use strict";var n=e("9efd"),i="/api/template",o={findByType:function(t){return Object(n["a"])({url:"".concat(i,"/find/").concat(t),method:"get"})},list:function(t){return Object(n["a"])({url:i,param:t,method:"get"})},create:function(t){return Object(n["a"])({url:i,data:t,method:"post"})}};a["a"]=o},dd38:function(t,a,e){"use strict";e.r(a);var n=function(){var t=this,a=t.$createElement,e=t._self._c||a;return e("div",[e("a-table",{attrs:{columns:t.columns,dataSource:t.template,pagination:!1,rowKey:function(t){return t.id}},scopedSlots:t._u([{key:"action",fn:function(a,n){return e("span",{},[e("a",{attrs:{href:"javascript:;"},on:{click:function(a){return t.handleEditClick(n)}}},[t._v("编辑")]),e("a-divider",{attrs:{type:"vertical"}}),e("a",{attrs:{href:"javascript:;"},on:{click:function(a){return t.handleShowPostSettings(n)}}},[t._v("设置")])],1)}}])}),e("div",{staticClass:"page-wrapper",style:{textAlign:"right"}},[e("a-pagination",{staticClass:"pagination",attrs:{current:t.pagination.page,total:t.pagination.total,defaultPageSize:t.pagination.size,pageSizeOptions:["1","2","5","10","20","50","100"],showSizeChanger:""},on:{showSizeChange:t.handlePaginationChange,change:t.handlePaginationChange}})],1)],1)},i=[],o=e("c621"),r=[{title:"模板名称",dataIndex:"name",key:"name"},{title:"模板描述",key:"description",dataIndex:"description"},{title:"模板类型",dataIndex:"type",key:"type"},{title:"视图路径",dataIndex:"path",key:"path"},{title:"视图名称",dataIndex:"viewName",key:"viewName"},{title:"创建时间",dataIndex:"createDate",key:"createDate"},{title:"Action",key:"action",scopedSlots:{customRender:"action"}}],s={data:function(){return{pagination:{page:1,size:5,sort:null},queryParam:{page:0,size:10,sort:null,keyword:null,categoryId:null,status:null},columns:r,article:[],template:[]}},created:function(){this.loadTemplate()},methods:{loadTemplate:function(){var t=this;this.queryParam.page=this.pagination.page-1,this.queryParam.size=this.pagination.size,this.queryParam.sort=this.pagination.sort,o["a"].list(this.queryParam).then((function(a){t.template=a.data.data.content,t.pagination.total=a.data.data.totalElements,console.log(a)}))},handlePaginationChange:function(t,a){this.pagination.page=t,this.pagination.size=a,this.loadArticle()}}},l=s,c=e("2877"),d=Object(c["a"])(l,n,i,!1,null,null,null);a["default"]=d.exports}}]);
//# sourceMappingURL=TemplateDynamicList.09ee0adb.js.map