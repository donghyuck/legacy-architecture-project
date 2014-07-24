<#ftl encoding="UTF-8"/>
<html decorator="homepage">
<head>
		<title>기업소개</title>
		<script type="text/javascript">
		<!--
		yepnope([{
			load: [
				'css!${request.contextPath}/styles/codedrop/cbpSlidePushMenus.css',
				'css!${request.contextPath}/styles/codedrop/codedrop.overlay.css',
				'${request.contextPath}/js/jquery/1.10.2/jquery.min.js',
				
				'${request.contextPath}/js/jgrowl/jquery.jgrowl.min.js',
				'${request.contextPath}/js/headroom/headroom.min.js',
				'${request.contextPath}/js/headroom/jquery.headroom.min.js',
				
				'${request.contextPath}/js/kendo/kendo.web.min.js',
				'${request.contextPath}/js/kendo.extension/kendo.ko_KR.js',	
				'${request.contextPath}/js/kendo/cultures/kendo.culture.ko-KR.min.js',
				
				'${request.contextPath}/js/bootstrap/3.1.0/bootstrap.min.js',
				
				'${request.contextPath}/js/pdfobject/pdfobject.js',
				'${request.contextPath}/js/common/common.modernizr.custom.js',
				
				'${request.contextPath}/js/common/common.models.js',
				'${request.contextPath}/js/common/common.api.js',
				'${request.contextPath}/js/common/common.ui.js'],
				complete: function() {
			
				// 1.  한글 지원을 위한 로케일 설정
				kendo.culture("ko-KR");
				// 1-2.  페이지 렌딩
				common.ui.landing();		
				
				      
				// START SCRIPT	
				// ACCOUNTS LOAD	
				var currentUser = new User();			
				$("#account-navbar").extAccounts({
					externalLoginHost: "${ServletUtils.getLocalHostAddr()}",	
					<#if action.isAllowedSignIn() ||  !action.user.anonymous  >
					template : kendo.template($("#account-template").html()),
					</#if>
					authenticate : function( e ){
						e.token.copy(currentUser);
					}				
				});			
				
				createContactGrid(); 
				<#if !action.user.anonymous >				
				
				</#if>	
				// END SCRIPT    
				checkContactTag();
			}
		}]);	
		-->
		function checkContactTag(){
			$('div[data-address-tag]').each(function(){
				var that = $(this);				
				//data-address-tag='인키움' data-search-type='1'  data-show-type='table'
				var srchName = that.attr('data-address-tag');
				var srchType = that.attr('data-search-type');
				var showType = that.attr('data-show-type'); 
				var contact = new common.models.Contact();
				contact.set("tag", srchName);
				contact.set("srchType", srchType);
				//contact.set("showType", showType);
				
				common.api.callback({  
					url : '${request.contextPath}/contact/list-contacts-tags.do?output=json',
					data : { item: kendo.stringify(contact) },
					success : function(response){
						//console.log(response);
						var list = response.contactsByTagNames;
						if(showType == 'table'){
							makeContactTable(list, that);
						}else{
							console.log('showType 에 맞는 출력 컨트롤이 없습니다...');
						}
						
					}
				});
			});
			
		}
		
		function makeContactTable(list, dom){
			var renderTo = dom;
			var str = '';
			if(list.length == 0){
				str = '검색된 연락처가 없습니다.';
			}else{
				str = "<table border='1'><colgroup></colgroup><tr><th>분류</th><th>부문</th><th>이름</th><th>설명</th><th>핸드폰</th><th>전화번호</th><th>이메일</th><th>관련태그</th></tr>";
				for(var i=0; i< list.length; i++){
					str += '<tr>';
					str += '<td>' + list[i].contactGroup.parentGroupName + '</td>';
					str += '<td>' + list[i].contactGroup.groupName + '</td>';
					str += '<td>' + list[i].name + '</td>';
					str += '<td>' + list[i].contactDesc + '</td>';
					str += '<td>' + list[i].cellPhone + '</td>';
					str += '<td>' + list[i].phone + '</td>';
					str += '<td>' + list[i].email + '</td>';
					str += '<td>' + list[i].tag + '</td>';
					str += '</tr>';
				}
				str += '</table>';
			}
			//console.log(str);
			renderTo.html(str);
			console.log('renderTo.html() : ' + renderTo.html());
			//renderTo.text(str);
		}
		
		function createContactGrid(){
			$("#contact-grid").kendoGrid({
				dataSource: new kendo.data.DataSource({
					transport: {
						read: {
							type : 'POST',
							dataType : "json", 
							url : '${request.contextPath}/contact/list-contacts.do?output=json'
						},
						parameterMap: function(options, operation) {
							if (operation != "read" && options.models) {
								return {models: kendo.stringify(options.models)};
							}else{
								 return { companyId : 1, typeCode:2, startIndex: options.skip, pageSize: options.pageSize };
							}
						},
					},
					pageSize: 15,
					error:common.api.handleKendoAjaxError,				
					schema: {
						total : "targetContactsCount",
						data : "targetContacts",
						model : common.models.Contact
					},
					serverPaging:true
				}),	
				columns: [
					{field: "contactId", title: "ID", sortable : false , width:100 },
					{field: "name", title: "이름", sortable : false},
					{field: "typeCode", title: "타입코드", sortable : false},
					{field:"typeName", title: "타입명", sortable : false},
					{field: "tag", title: "태그", sortable : false},
					{field: "groupIds", title: "매핑그룹", sortable : false},
					{field: "creationDate", title: "등록일", width: "120px", format: "{0:yyyy.MM.dd}", attributes: { "class": "table-cell", style: "text-align: center " }}
				],
				sortable: true,
				pageable: true,
				selectable: "single",
				height: 430,
				change: function(e) { 
					var selectedCells = this.select();
					
					if( selectedCells.length > 0){
						var selectedCell = this.dataItem( selectedCells );
						setContactEditorSource(selectedCell);
						showContactViewer();
					}
							
										
				}			
			});	
			
			
			common.ui.handleButtonActionEvents($("#addContactBtn"), 	{event: 'click', handlers: {
					'new-contact' : function(e){
						var contactPlaceHolder = new common.models.Contact();
						setContactEditorSource(contactPlaceHolder);
						showContactEditor();			
					}
				}}				
			);
			
		}
		
		<!-- ============================== -->
		<!-- Contact viewer , editor 		-->
		<!-- ============================== -->			
		
		function getContactEditorSource(){
			if( !$("#contact-editor").data("contactPlaceHolder") ){
				var contactPlaceHolder = new common.models.Contact();
				$("#contact-editor").data("contactPlaceHolder", contactPlaceHolder );				
			}
			return $("#contact-editor").data("contactPlaceHolder");			
		}
		
		function setContactEditorSource(source){	
			source.copy(getContactEditorSource());		
		}
		
		
		function showContactViewer(){
			var contactPlaceHolder = getContactEditorSource();
			if( contactPlaceHolder.contactId > 0 ){	
				if( $('#contact-viewer').text().trim().length == 0 ){
					var template = kendo.template($('#contact-viewer-template').html());
					$('#contact-viewer').html( template );			
					var contactViewerModel =  kendo.observable({ 
						contact : contactPlaceHolder,
						/*profilePhotoUrl : function(){
							return common.api.user.photoUrl (this.get("topic").user, 150,150);
						},*/
						editable : function(){
							var currentUser = $("#account-navbar").data("kendoExtAccounts").token;
							if( currentUser.hasRole("ROLE_ADMIN") || currentUser.hasRole("ROLE_SITE_ADMIN") ){
								return true;
							}
							return false;
						},
						openContactEditor : showContactEditor,
						closeViewer : function(e){
							kendo.fx($("#contact-viewer-panel")).expand("vertical").duration(200).reverse();								
						}
					});
					kendo.bind($("#contact-viewer-panel"), contactViewerModel );
				}			
				$('#contact-editor-panel').hide();
				kendo.fx($("#contact-viewer-panel")).expand("vertical").play();			
			}
		}
		
		function showContactEditor(){	
			var contactPlaceHolder = getContactEditorSource();
			
			console.log('contactPlaceHolder.typeCode : '+ contactPlaceHolder.typeCode);
			
			var renderTo = $("#contact-editor-panel");
			
			if( $('#contact-editor').text().trim().length == 0 ){
				var template = kendo.template($('#contact-editor-template').html());		
				$('#contact-editor').html( template );	
				
				var contactEditorModel =  kendo.observable({ 
					contact : contactPlaceHolder,
					value : function( value ){
						if( typeof value === 'undefined' ){
							return this.contact.name ;
						}else{
							this.contact.set('name' , value);
						}
					},					
					isNew : false,
					doSave : function (e) {
						var btn = $(e.target);
						btn.button('loading');
						var template = kendo.template('<p class="text-danger">#:message#</p>');
						
						this.contact.user = null;
						this.contact.properties = null;
						
						getCheckedNodes();

						common.api.callback({  
							url : '${request.contextPath}/contact/update-contacts.do?output=json',
							data : { item: kendo.stringify( this.contact ) },
							success : function(response){
								common.ui.notification({title:"연락처", message: "정상적으로 저장되었습니다.", type: "success" });
								$("#contact-grid").data('kendoGrid').dataSource.read();
								//$("#forum-list-view").data('kendoGrid').dataSource.read();
							},
							fail: function(){								
								common.ui.notification({title:"연락처", message: "시스템 운영자에게 문의하여 주십시오." });
							},
							requestStart : function(){
								kendo.ui.progress(renderTo, true);
							},
							requestEnd : function(){
								kendo.ui.progress(renderTo, false);
							},
							always : function(e){
								btn.button('reset');
								//contactEditorModel.closeEditor(e);
							}
						});
					},
					showClickItem : function (e){
							console.log('e.data.selectedCategory : ' +e.data.selectedCategory);
							if(e.data.selectedCategory != "0"){
								$("#contact-group-treeview").kendoTreeView({
						            checkboxes: {
						                checkChildren: false
						            },
						            dataTextField: "text",
					            	dataSource : {
					            		transport: {
											read: {
												type : 'POST',
												dataType : "json", 
												url : '${request.contextPath}/contact/list-contact-groups.do?output=json'
											},
											parameterMap: function(options, operation) {
												return {typeCode : options.typeCode || e.target.value, groupIds : contactPlaceHolder.groupIds };
											},
										},								
										schema: {
											data : "targetContactGroups",									
											model:{
												id: "id",
												children: "items",
												hasChildren : "hasChildren",
												expanded: "hasChildren"
											}
										}
					            	}
					            });
								
								//console.log('e.target.value : ' + e.target.value);
								//contactPlaceHolder.set("typeCode", e.target.value); 
							}
					},
					selectedCategory :"0",
					updateRequired : true,
					editable : function(){
						var currentUser = $("#account-navbar").data("kendoExtAccounts").token;
						if( currentUser.hasRole("ROLE_ADMIN") || currentUser.hasRole("ROLE_SITE_ADMIN") ){
							return true;
						}
						return false;
					},
					closeEditor : function(e){
						kendo.fx(renderTo).expand("vertical").duration(200).reverse();
						kendo.fx($('#contact-viewer-panel')).expand("vertical").play();
						//kendo.fx($('#contact-list-panel > .panel > .panel-body').first()).expand("vertical").duration(200).play();
					}
				});
				
				/*
				contactEditorModel.bind("change", function(e){				
					if( e.field.match('^contact.')){ 
						//console.log('valid check name.len: ' + this.contact.name.length);
						if( this.contact.name.length > 0 )	{			//required field
							contactEditorModel.set("`", true);
						}
					}	
				});	
				*/
				
				kendo.bind(renderTo, contactEditorModel ); <#-- Binding the View to the View-Model -->
				renderTo.data("model", contactEditorModel );
				
			}
			
			
			// 그룹 선택 세팅 .
			//console.log('contactPlaceHolder.contactId : ' + contactPlaceHolder.contactId);
			//console.log('contactPlaceHolder.typeCode : ' + contactPlaceHolder.typeCode);
			if(contactPlaceHolder.contactId > 0){
				$("#contact-editor-panel").data("model").selectedCategory = contactPlaceHolder.typeCode;
				console.log('clicked category : ' + $("#contact-editor-panel").data("model").selectedCategory);
				$('#contact-editor select').click();
			}
			
			//renderTo.data("model").set("updateRequired", false);			
			renderTo.data("model").set("isNew", (contactPlaceHolder.contactId < 1 ));
				
			$('#contact-viewer-panel').hide();
			kendo.fx(renderTo).expand("vertical").duration(200).play();			
		}
		
		function checkedNodesIds(nodes, checkedNodes){
			for (var i=0; i < nodes.length; i++){
				if(nodes[i].checked){
					checkedNodes.push(nodes[i].id);
				}
				if(nodes[i].hasChildren){
					checkedNodesIds (nodes[i].children.view(), checkedNodes);
				}
			}
		}
		
		function getCheckedNodes(){
			var contactPlaceHolder = getContactEditorSource();
			
			var checkedNodes = [];
			var treeView = $('#contact-group-treeview').data('kendoTreeView');
			var message;
				
			checkedNodesIds(treeView.dataSource.view(), checkedNodes);
			
			if(checkedNodes.length > 0){
				message = "IDs of checked nodes: " + checkedNodes.join(",");
				contactPlaceHolder.set("groupIds", checkedNodes.join(","));
				//console.log("contactPlaceHolder.groupIds : " + contactPlaceHolder.groupIds);
			} else{
				message = "No nodes checked.";
				contactPlaceHolder.groupIds = "";
			}
			console.log(message);
		}
		
		
	
		
		
		
		<!-- ============================== -->
		<!-- Utils for editor									       -->
		<!-- ============================== -->						
		function createEditor( renderToString, bodyEditor, model ){
			if(!bodyEditor.data("kendoEditor") ){			
				//var imageBroswer = createEditorImageBroswer( renderToString + "-imagebroswer", bodyEditor);				
				var linkPopup = createEditorLinkPopup(renderToString + "-linkpopup", bodyEditor);	
				var htmlEditor = createCodeEditor(renderToString + "-html-editor", bodyEditor, model );									
				bodyEditor.kendoEditor({
						tools : [ 'bold', 'italic', 'insertUnorderedList', 'insertOrderedList',
							{	
								name: "createLink",
								exec: function(e){
									linkPopup.show();
									return false;
								}},
							'unlink', 
							{	
								name: "insertImage",
								exec: function(e){
									imageBroswer.show();
									return false;
								}},
							{
								name: 'viewHtml',
								exec: function(e){
									htmlEditor.open();
									return false;
								}}							
						],
						stylesheets: [
							"${request.contextPath}/styles/bootstrap/3.1.0/bootstrap.min.css",
							"${request.contextPath}/styles/common/common.ui.css"
						]
				});
			}			
		}

		function createCodeEditor( renderToString, editor, model ) {		
			if( $("#"+ renderToString).length == 0 ){
				$('body').append('<div id="'+ renderToString +'"></div>');
			}							
			var renderTo = $("#"+ renderToString);		
			if( !renderTo.data('kendoExtModalWindow') ){						
				renderTo.extModalWindow({
					title : "HTML",
					backdrop : 'static',
					template : $("#code-editor-modal-template").html(),
					refresh : function(e){
						var editor = ace.edit("htmleditor");
						editor.getSession().setMode("ace/mode/xml");
						editor.getSession().setUseWrapMode(true);
						
					},
					open: function (e){
						ace.edit("htmleditor").setValue(model.value());
					}					
				});					
				renderTo.find('button.custom-update').click(function () {
					var btn = $(this)			
					var newValue = ace.edit("htmleditor").getValue();
					var oldValue = model.value();
					if( newValue.length != oldValue.length ){
						model.value(newValue);
					}		
					ace.edit("htmleditor").setValue("");			
					renderTo.data('kendoExtModalWindow').close();
				});
			}
			return renderTo.data('kendoExtModalWindow');			
		}
				
		function createEditorImageBroswer(renderToString, editor ){			
			if( $("#"+ renderToString).length == 0 ){
				$('body').append('<div id="'+ renderToString +'"></div>');
			}					
			var renderTo = $("#"+ renderToString);	
			if(!renderTo.data("kendoExtImageBrowser")){
				var imageBrowser = renderTo.extImageBrowser({
					template : $("#image-broswer-template").html(),
					apply : function(e){						
						editor.data("kendoEditor").exec("inserthtml", { value : e.html } );
						imageBrowser.close();
					}				
				});
			}
			return renderTo.data("kendoExtImageBrowser");
		}
		
		function createEditorLinkPopup(renderToString, editor){		
			if( $("#"+ renderToString).length == 0 ){
				$('body').append('<div id="'+ renderToString +'"></div>');
			}				
			var renderTo = $("#"+ renderToString);		
			if(!renderTo.data("kendoExtEditorPopup") ){		
				var hyperLinkPopup = renderTo.extEditorPopup({
					type : 'createLink',
					title : "하이퍼링크 삽입",
					template : $("#link-popup-template").html(),
					apply : function(e){						
						editor.data("kendoEditor").exec("inserthtml", { value : e.html } );
						hyperLinkPopup.close();
					}
				});
			}
			return renderTo.data("kendoExtEditorPopup");
		}
		
		</script>		
		<style scoped="scoped">
		blockquote p {
			font-size: 15px;
		}

		.k-grid table tr.k-state-selected{
			background: #428bca;
			color: #ffffff; 
		}
		
		#announce-view .popover {
			position : relative;
			max-width : 500px;
		}
						
		</style>   	
	</head>
	<body class="color0">
	<!-- START HEADER -->
	<#include "/html/common/common-homepage-menu.ftl" >	
	<#assign current_menu = action.getWebSiteMenu("USER_MENU", "MENU_PERSONALIZED_3") />
	<header class="cloud">
		<div class="container">
			<div class="col-lg-12">	
				<h2>${ current_menu.title }</h2>
				<h4><i class="fa fa-quote-left"></i>&nbsp;${ current_menu.description ? replace ("{displayName}" , action.webSite.company.displayName ) }&nbsp;<i class="fa fa-quote-right"></i></h4>
			</div>
		</div>
	</header>			
	<!-- END HEADER -->					
	<!-- START MAIN CONTENT -->	
	<section class="container-fluid" style="min-height:600px;">		
		<div id="personalized-area " class="row blank-top-10" >	
			<!-- 연락처-패널-레이아웃 -->
			<div id="contact-panel-layout">
				<!-- 연락처-좌측-패널 -->
				<div id="contact-left-panel" class="custom-panels-group col-sm-6" >
					<!--패널 헤더-->
					<div class="page-header page-nounderline-header" style="height:50px;">
					<h5>
						<small><i class="fa fa-info"></i> <span id="forum-desc">연락처 목록</span></small>
					<#if request.isUserInRole("ROLE_ADMIN") || request.isUserInRole("ROLE_SITE_ADMIN") >
						<div class="pull-right">
							<button type="button" id="addContactBtn" class="btn btn-primary btn-sm btn-control-group" data-action="new-contact">
								<i class="fa fa-plus"></i> 연락처 추가
							</button>
						</div>											
					</#if>
					</h5>
					</div>	
					<!-- 연락처-리스트-패널-->
					<div id="contact-list-panel" class="panel-body" style="padding:5px;" >
						<div id="contact-grid"></div>		
					</div>
					
					<div data-contact-tag='인키움' data-search-type='2'  data-show-type='table'></div>
				</div>
				<!-- 연락처-우측-패널 -->
				<div id="contact-detail-panel" class="custom-panels-group col-sm-6" >
					<!-- 연락처-상세뷰어-패널 -->
					<div id="contact-viewer-panel" class="panel-body" style="display:none;">
						<div class="row">
							<div class="col-lg-12">
								<div class="page-header page-nounderline-header text-primary" style="min-height: 45px;">
									<h5 >
										<small><i class="fa fa-info"></i> 편집 버튼을 클릭하면 수정가능합니다.</small>
										<div class="pull-right">
										<div class="btn-group">
											<button type="button" class="btn btn-primary btn-sm" data-bind="click: openContactEditor, enabled: editable" >편집</button>													
										</div>						
										<!--<button type="button" class="btn btn-primary btn-notice-control-group btn-sm" data-bind="click: closeViewer">&times;  닫기</button>-->
										</div>
									</h5>
								</div>																		
							</div>
						</div>		
						<div class="row">
							<div class="col-lg-12">
								<div class="panel panel-default" style="margin-bottom: 20px;">
									<div class="panel-body">													
										<div  id="contact-viewer"></div>																										
									</div>
								</div>												
							</div>																		
						</div>
					</div>
					<!-- 연락처-에디터-패널 -->
					<div id="contact-editor-panel" class="panel-body"  style="display:none;">
						<div class="page-header page-nounderline-header" style="min-height: 45px;">
							<h5 >
								<small><i class="fa fa-info"></i> 저장 버튼을 클릭하여 저장합니다.</small>
								<div class="pull-right">
								<div class="btn-group">
									<button type="button" class="btn btn-primary btn-sm" data-bind="click: doSave, enabled: updateRequired" data-loading-text='<i class="fa fa-spinner fa-spin"></i>' >저장</button>			
									<button type="button" class="btn btn-primary btn-sm" data-toggle="button"  data-bind="click: openForumProps, enabled: editable, invisible:isNew">프로퍼티</button>
									<button type="button" class="btn btn-primary btn-notice-control-group btn-sm" data-bind="click: closeEditor">&times;  닫기</button>
								</div>
							</div>
							</h5>
							
						</div>				
						<div  id="contact-editor"></div>	
					</div>
				</div>
			</div>
		</div>									 
	</section>	
	<!-- END MAIN CONTENT -->	
	

	<script type="text/x-kendo-tmpl" id="news-view-template">		
		
		<div class="page-heading">
			<h4 data-bind="html:subject"></h4>				
		</div>
		
		<div class="media">
			<a class="pull-left" href="\\#">
			<img src="${request.contextPath}/download/profile/#: user.photoUrl #?width=150&height=150" width="30" height="30" class="img-rounded">
			</a>
			<div class="media-body">
				<h5 class="media-heading">
					# if( user.nameVisible ){#
					#: user.name # (#: user.username #)
					# } else { #
					#: user.username #
					# } # 		
					# if( user.emailVisible ){#
					<br>(#: user.email #)
					# } #	
				</h5>		
			</div>
		</div>	
		
		<div class="blank-top-5" ></div>
		<div data-bind="html:content"></div>	
		<div class="blank-top-5" ></div>
		<div class="btn-group pull-right">
			<button  type="button" class="btn btn-info btn-sm custom-list "><i class="fa fa-angle-double-up"></i> 목록</button>		
		</div>
	</script>


		<!-- START FOOTER -->
	<#include "/html/common/common-homepage-footer.ftl" >		
	<!-- END FOOTER -->	
	<!-- START TEMPLATE -->
	<!-- ============================== -->
	<!-- contact viewer template          -->
	<!-- ============================== -->
	<script type="text/x-kendo-tmpl" id="contact-viewer-template">		
		<div class="page-heading">
			<h4 data-bind="html:contact.name"></h4>		
		</div>													
		<div class="media">
			<!--<a class="pull-left" href="\\#">
				<img data-bind="attr:{ src: profilePhotoUrl }" width="30" height="30" class="img-rounded">
			</a>
			<div class="media-body">
				<h5 class="media-heading">																	
					<p><span data-bind="visible:contact.user.nameVisible, text: contact.user.name"></span> <code data-bind="text: contact.user.username"></code></p>
					<p data-bind="visible:contact.user.emailVisible, text: contact.user.email"></p>
				</h5>		
			</div>-->
			<div> 성명 : <span data-bind="html: contact.name" /></div> <br/>
			<div>이메일 : <span data-bind="html: contact.email" /></div>  <br/>
			<div>연락처 : <span data-bind="html: contact.phone" /></div> <br/>
			<div>핸드폰 : <span data-bind="html: contact.cellPhone" /></div> <br/>
			<div>설명 : <span data-bind="html: contact.contactDesc" /></div> <br/>
			<div>태그 : <span data-bind="html: contact.tag" /></div>
		</div>	
	</script>
	
	<!-- ============================== -->
	<!-- contact editor template          -->
	<!-- ============================== -->
	<script type="text/x-kendo-tmpl" id="contact-editor-template">
		<div class="panel panel-default">
			<div class="panel-heading" data-bind="visible: isNew" style="padding:5px;">
		
			</div>
			<div class="panel-body"  style="padding:5px;">									
				<div  class="form">
					<div class="form-group">
						<label class="control-bale"><small>연락처 그룹</small></label>
						<div>
						 	카테고리 <br/>
							<select data-bind="value : selectedCategory, click : showClickItem" id="categorySelect">
							<option value="0">카테고리를 선택해주세요. </option>
						 	<option value="1">우리회사 연락처 </option>
						 	<option value="2">우리회사 담당자 연락처</option>
						 	<option value="3">고객사 연락처</option>
						 	<option value="4">고객사 담당자 연락처</option>
						 	</select>
						 </div>
						  체크박스로 선택한 그룹이 매핑됩니다. 
						 <div id="contact-group-treeview">
						 </div>
					</div>
					<div class="form-group">
						<label class="control-label"><small>이름</small></label>							
						<input type="text" data-bind="value: contact.name"  class="form-control" placeholder="이름을 입력하세요." />
						<label class="control-label"><small>이메일</small></label>
						<input type="text" data-bind="value: contact.email" class="form-control" placeholder="이메일을 입력하세요." />
						<label class="control-label"><small>연락처</small></label>
						<input type="text" data-bind="value: contact.phone" class="form-control" placeholder="연락처을 입력하세요." />
						<label class="control-label"><small>핸드폰</small></label>
						<input type="text" data-bind="value: contact.cellPhone" class="form-control" placeholder="핸드폰을 입력하세요." />
						<label class="control-label"><small>설명</small></label>
						<input type="text" data-bind="value: contact.contactDesc" class="form-control" placeholder="설명을 입력하세요." />
						<label class="control-label"><small>태그</small></label>
						<input type="text"  data-bind="value: contact.tag" class="form-control" placeholder="태그를 입력하세요. 콤마로 구분해서 넣을 수 있습니다." />
				</div>
				</div>									
			</div>	
		</div>								
	</script>
	
	
	<#include "/html/common/common-homepage-templates.ftl" >		
	<!-- END TEMP11LATE -->
		
	</body>    
</html>