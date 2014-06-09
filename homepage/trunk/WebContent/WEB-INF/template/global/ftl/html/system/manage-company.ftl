<#ftl encoding="UTF-8"/>
<html decorator="secure">
<head>
		<title>관리자 메인</title>		
		<link  rel="stylesheet" type="text/css"  href="${request.contextPath}/styles/common/common.admin.style.css" />
		<script type="text/javascript">
		<!--		
		yepnope([{
			load: [
			'css!${request.contextPath}/styles/font-awesome/4.0.3/font-awesome.min.css',
			'css!${request.contextPath}/styles/common.extension/animate.css',/*
			'css!${request.contextPath}/styles/common/common.admin.widgets.css',			
			'css!${request.contextPath}/styles/common/common.admin.rtl.css',	*/		
			'css!${request.contextPath}/styles/common/common.admin.themes.css',
			'css!${request.contextPath}/styles/common/common.admin.pages.css',	
			/*'${request.contextPath}/js/jquery/2.1.1/jquery-2.1.1.min.js',*/
			'${request.contextPath}/js/jquery/1.10.2/jquery.min.js',
			'${request.contextPath}/js/kendo/kendo.web.min.js',
			'${request.contextPath}/js/kendo.extension/kendo.ko_KR.js',
			'${request.contextPath}/js/kendo/cultures/kendo.culture.ko-KR.min.js',
			'${request.contextPath}/js/jgrowl/jquery.jgrowl.min.js',			
			'${request.contextPath}/js/bootstrap/3.0.3/bootstrap.min.js',			
			'${request.contextPath}/js/common.plugins/fastclick.js', 
			'${request.contextPath}/js/common.plugins/jquery.slimscroll.min.js', 
			'${request.contextPath}/js/common/common.admin.js',
			'${request.contextPath}/js/common/common.models.js',       	    
			'${request.contextPath}/js/common/common.api.js',
			'${request.contextPath}/js/common/common.ui.js',
			'${request.contextPath}/js/common/common.ui.admin.js'
			],
			complete: function() {
				// 1-1.  한글 지원을 위한 로케일 설정
				common.api.culture();
				// 1-2.  페이지 렌딩
				common.ui.landing();				
				// 1-3.  관리자  로딩
				var currentUser = new User();
				
				var targetCompany = new Company();	
				
				common.ui.admin.setup({
					authenticate : function(e){
						e.token.copy(currentUser);
					},
					companyChanged: function(item){
						item.copy(targetCompany);
					}
				});		

				common.ui.handleButtonActionEvents(
					$("button.btn-control-group"), 
					{event: 'click', handlers: {
						'create-company' : function(e){
							$("#company-grid").data('kendoGrid').addRow();			
						}, 	
						user : function(e){
							topBar.go('main-user.do');								
						}, 	
						group : function(e){
							topBar.go('main-group.do');							
						}, 							
						menu : function(e){
							showMenuWindow();
						},
						role : function(e){					
							showRoleWindow();			
						}  						 
					}}
				);

				var company_grid = $("#company-grid").kendoGrid({
					dataSource: {	
						transport: { 
							read: { url:'${request.contextPath}/secure/list-company.do?output=json', type: 'POST' },
							create: { url:'${request.contextPath}/secure/create-company.do?output=json', type:'POST' },             
							update: { url:'${request.contextPath}/secure/update-company.do?output=json', type:'POST' },
							parameterMap: function (options, operation){	          
								if (operation != "read" && options) {
									return { companyId: options.companyId, item: kendo.stringify(options)};
								}else{
									return { startIndex: options.skip, pageSize: options.pageSize }
								}
							}
						},
						schema: {
							total: "totalCompanyCount",
							data: "companies",
							model : Company
						},
						pageSize: 15,
						serverPaging: true,
						serverFiltering: false,
						serverSorting: false,                        
						error: common.api.handleKendoAjaxError
					},
					columns: [
						{ field: "companyId", title: "ID", width:40,  filterable: false, sortable: false }, 
						{ field: "name",    title: "KEY",  filterable: true, sortable: true,  width: 80 }, 
						{ field: "displayName",   title: "이름",  filterable: true, sortable: true,  width: 100 }, 
						{ field: "domainName",   title: "도메인",  filterable: true, sortable: false,  width: 100 }, 
						{ field: "description", title: "설명", width: 200, filterable: false, sortable: false },
						{ command: [ {name:"edit",  text: { edit: "수정", update: "저장", cancel: "취소"}  }  ], title: "&nbsp;", width: 180  }], 
					filterable: true,
					editable: "inline",
					selectable: 'row',
					height: '100%',
					batch: false,              
					pageable: { refresh:true, pageSizes:false,  messages: { display: ' {1} / {2}' }  },					
					change: function(e) {
						// 1-1 SELECTED EVENT  
						var selectedCells = this.select();
						if( selectedCells.length > 0){
							var selectedCell = this.dataItem( selectedCells );	     
							if( selectedCell.companyId > 0 )
								showCompanyDetails();
							
							/*
							if( selectedCell.companyId > 0 && selectedCell.companyId != selectedCompany.companyId ){
								selectedCompany.companyId = selectedCell.companyId;
								selectedCompany.name = selectedCell.name;
								selectedCompany.displayName = selectedCell.displayName;
								selectedCompany.domainName = selectedCell.domainName;
								selectedCompany.description = selectedCell.description;
								selectedCompany.modifiedDate = selectedCell.modifiedDate;
								selectedCompany.creationDate = selectedCell.creationDate;	                                 
								selectedCompany.formattedCreationDate  =  kendo.format("{0:yyyy.MM.dd}",  selectedCell.creationDate );      
								selectedCompany.formattedModifiedDate =  kendo.format("{0:yyyy.MM.dd}",  selectedCell.modifiedDate );    
								
								
								$('#company-details').show().html(kendo.template($('#company-details-template').html()));	
								kendo.bind( $('#company-details'), selectedCompany );								}	
								*/
						}
					},
					dataBound: function(e){   
						// 1-2 Company 데이터를 새로 읽어드리면 기존 선택된 정보들과 상세 화면을 클리어 한다. 
						var selectedCells = this.select();				
						if(selectedCells.length == 0 )
						{
							//selectedCompany = new Company({});		    
							//kendo.bind($(".tabular"), selectedCompany );
							//$("#menu").hide(); 	
							$("#company-details").hide(); 	 		
						}   
					}	                    
				}); //.css("border", 0);
				
				// MENU WINDOW
				$('#menu-grid').data("menuPlaceHolder", new Menu() )	;
												
				// END SCRIPT
			}
		}]);
		
		
		function creatCompanyGrid(){
			
		} 
		
		function getSelectedCompany(){
			var renderTo = $('#company-details');
			var grid = renderTo.data('kendoGrid');
			var selectedCells = grid.select();
			var selectedCell = grid.dataItem( selectedCells );   
			return selectedCell;
		}
		
		function showCompanyDetails(){
			var renderTo = $('#company-details');
			var companyPlaceHolder = getSelectedCompany();
			var slide = kendo.fx(renderTo).slideIn("down");
			
			if( renderTo.text().length === 0 ){
				renderTo.html(kendo.template($('#company-details-template').html()));					
				var detailsModel = kendo.observable({
					company : new Company(),
					logoUrl : ""
					
				});				
				kendo.bind(renderTo, detailsModel );	
				renderTo.data("model", detailsModel );					
				//renderTo.show();			
				slide.play();
			}
			
			companyPlaceHolder.copy( renderTo.data("model").company );
			$('#company-details').data("model").set("logoUrl", "/download/logo/company/" + companyPlaceHolder.name );
			
			/*
				
			if( ! $("#image-grid").data("kendoGrid") ){	
				$('#myTab').on( 'show.bs.tab', function (e) {		
					//e.preventDefault();			
					var show_bs_tab = $(e.target);
					switch( show_bs_tab.attr('href') ){
						case "#template-mgmt" :
							createTemplatePane();
							break;
						case  '#image-mgmt' :
							createImagePane();
							break;
						case  '#attachment-mgmt' :	
							createAttachPane();
							break;	
						case  '#social-mgmt' :	
							createSocialPane();
							break;								
					}	
				});			
				$('#myTab a:first').tab('show') ;		
			}
			$('#company-details').toggleClass('hide');			
			*/						
		}
		
		-->
		</script> 		 
		<style>
		.k-grid-content{
			height:300px;
		}
					




				
		</style>
	</head>
	<body class="theme-default main-menu-animated">
		<div id="main-wrapper">
			<#include "/html/common/common-system-navigation.ftl" >	
			<div id="content-wrapper">
				<ul class="breadcrumb breadcrumb-page">
					<div class="breadcrumb-label text-light-gray">You are here: </div>
					<li><a href="#">Home</a></li>
					<li class="active"><a href="#">Dashboard</a></li>
				</ul>
				<div class="page-header bg-dark-gray">				
					<#assign selectedMenu = WebSiteUtils.getMenuComponent("SYSTEM_MENU", "MENU_2_1") />
					<div class="row">
						<h1 class="col-xs-12 col-sm-6 text-center text-left-sm"><#if selectedMenu.isSetIcon() ><i class="fa ${selectedMenu.icon} page-header-icon"></i></#if> ${selectedMenu.title}
							<p><small><i class="fa fa-quote-left"></i> ${selectedMenu.description} <i class="fa fa-quote-right"></i></small></p>
						</h1>
						<div class="col-xs-12 col-sm-6">
							<div class="row">
								<hr class="visible-xs no-grid-gutter-h">							
								<div class="pull-right col-xs-12 col-sm-auto">
									<h6 class="text-light-gray text-semibold text-xs" style="margin:20px 0 10px 0;">옵션</h6>
									<div class="btn-group">
										<button type="button" class="btn btn-primary btn-sm btn-control-group" data-action="menu"><i class="btn-label icon fa fa-sitemap"></i> 메뉴</button>
										<button type="button" class="btn btn-primary btn-sm btn-control-group" data-action="role"><i class="btn-label icon fa fa-lock"></i> 권한 & 롤</button>
									</div>									
								</div>
							</div>
						</div>
					</div>				
				</div><!-- / .page-header -->
				<div class="row">				
					<div class="col-sm-12">					
						<div class="panel panel-default" style="min-height:300px;">
							<div class="panel-heading">
								<span class="panel-title"><i class="panel-title-icon fa fa-building-o"></i></span>
								<div class="panel-heading-controls">
									<span class="panel-heading-text"><em>Just some text with <a href="#">link</a></em>&nbsp;&nbsp;<span style="color: #ccc">|</span>&nbsp;&nbsp;</span>
									<button class="btn btn-danger btn-sm btn-control-group" data-action="create-company">회사 만들기 </button>
								</div>
							</div>
							<div id="company-grid" class="no-border"></div>	
						</div>
					</div>	
				</div>
				<!--
				<div class="row">					
					<div class="col-sm-12">					
						<div id="company-details" style="display:none;"></div>					
					</div>
				</div>
				-->
				<!-- company details -->
				<div id="company-details" class="page-details" style="display:none;"></div><!-- /company details -->		
			</div> <!-- / #content-wrapper -->
			<div id="main-menu-bg">
			</div>
		</div> <!-- / #main-wrapper -->
		<script type="text/x-kendo-template" id="company-details-template">		
					<div class="details-full-name">
						<span class="text-semibold" data-bind="text:company.displayName"></span> 상세정보
					</div>
					<div class="details-row">					
						<div class="left-col">
							<div class="details-block">
								<div class="panel details-photo">
									<img data-bind="attr: { src: logoUrl }" alt="">
								</div>
								<br>
								<a href="\\#" class="btn btn-success"><i class="fa fa-check"></i> Following</a> 
								<a href="\\#" class="btn"><i class="fa fa-comment"></i></a>
							</div>				
							<div class="panel panel-transparent">
								<div class="panel-heading">
									<span class="panel-title"  data-bind="text:company.description"></span>
								</div>
								<div class="panel-body">
								Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et <a href="\\#">dolore magna</a> aliqua.
								</div>
							</div>
						</div>
						<div class="right-col">
							<hr class="details-content-hr no-grid-gutter-h">	
							<div class="details-content">
								<ul id="myTab" class="nav nav-tabs nav-tabs-sm">
									<li class="active"><a href="\\#props" data-toggle="tab">프로퍼티</a></li>
									<li><a href="\\#groups" data-toggle="tab">그룹 <span class="label label-success">22</span></a></li>
									<li><a href="\\#users" data-toggle="tab">사용자 <span class="label label-success">1234</span></a></li>
								</ul>	
								<!-- .tab-content -->	
								<div class="tab-content tab-content-bordered no-padding">
									<div class="tab-pane active" id="props">
										<div class="alert alert-info alert-dark no-border-radius no-margin-b">
											<button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
											프로퍼티는 수정 후 저장 버튼을 클릭하여야 최종 반영됩니다.
										</div>						
										<div id="company-prop-grid" class="props no-border"></div>
									</div>
									<div class="tab-pane" id="groups">					
										<div class="alert alert-info alert-dark no-border-radius no-margin-b">
											<button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
											그룹관리는  그룹관리를 사용하여 관리 하실수 있습니다.	     
										</div>						
										<div id="company-group-grid"  class="groups no-border"></div>					
									</div>
									<div class="tab-pane" id="users">
										<div class="alert alert-info alert-dark no-border-radius no-margin-b">
											<button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
											사용자관리는 사용자관리를 사용하여 관리 하실수 있습니다.	     
										</div>			
										<div id="company-user-grid"  class="users no-border"></div>
									</div>
								</div><!-- / .tab-content -->
							</div><!-- / .details-content -->
						</div><!-- / .right-col -->
					</div><!-- / .details-row -->	
		</script>				
		<#include "/html/common/common-system-templates.ftl" >			
	</body>    
</html>