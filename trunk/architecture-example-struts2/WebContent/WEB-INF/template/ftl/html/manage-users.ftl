<#ftl encoding="UTF-8"/>
<html decorator="banded">
    <head>
        <title>사용자 관리</title>
        <script type="text/javascript">
                
        yepnope([{
            load: [             
               'css!${request.contextPath}/styles/kendo/kendo.common.min.css',
        	   'css!${request.contextPath}/styles/kendo/kendo.metro.min.css',
        	   '${request.contextPath}/js/common.js',  
        	   '${request.contextPath}/js/kendo/kendo.web.min.js' ],     
            complete: function() {   
            	            	
	            var selectedUser = kendo.observable({
					userId: { type: "number" },
	                username: { type: "string" },
	                name: { type: "string" },
	                email: { type: "string" },
	                creationDate: { type: "date" },
	                lastLoggedIn: { type: "date" },
	                lastProfileUpdate : { type: "date" },                                    
	                enabled : {type: "boolean" },
	                nameVisible : {type: "boolean" },	
	                emailVisible: {type: "boolean" },
	                formattedLastLoggedIn : { type: "string" },
	                formattedLastProfileUpdate : { type: "string" },
	                clear: function() {
	                    this.set("userId", 0 );
	                    this.set("username", "");
	                    this.set("name", "");
	                    this.set("email", false);
	                    this.set("creationDate", null ); 
	                    this.set("lastLoggedIn", null ); 
	                    this.set("lastProfileUpdate", null ); 
	                    this.set("formattedLastLoggedIn", "" ); 
	                    this.set("formattedLastProfileUpdate", "" );   
	                    this.set("enabled", false ); 
	                    this.set("nameVisible", false ); 
	                    this.set("emailVisible", false );
	                }
			    });
            		       
		        var selectedUserId = -1;		     
		          
		        var panelBar = $("#panelbar").kendoPanelBar({ expandMode: "single" } ).data("kendoPanelBar");  
		       
		        // 1. User List Grid 		        
				var user_grid = $("#user-grid").kendoGrid({
                    dataSource: {
                        transport: { 
                            read: { url:'${request.contextPath}/secure/main-user.do?output=json', type: 'POST', type:'jsonp' },
	                        parameterMap: function (options, type){
	                            return { startIndex: options.skip, pageSize: options.pageSize }
	                        }
                        },
                        schema: {
                            total: "totalUserCount",
                            data: "users",
                            model: {
                                id:"userId",
                                fields: {
                                    userId: { type: "number" },
                                    username: { type: "string" },
                                    name: { type: "string" },
                                    email: { type: "string" },
                                    creationDate: { type: "date" },
                                    lastLoggedIn: { type: "date" },
                                    lastProfileUpdate : { type: "date" },                                    
                                    enabled : {type: "boolean" },
                                    nameVisible : {type: "boolean" },
                                    emailVisible: {type: "boolean" }
                                }
                            }
                        },
                        batch: false,
                        pageSize: 10,
                        serverPaging: true,
                        serverFiltering: false,
                        serverSorting: false
                    },
                    columns: [
                        { field: "userId", title: "ID", width:50,  filterable: false, sortable: false }, 
                        { field: "username", title: "아이디", width: 100 }, 
                        { field: "name", title: "이름", width: 100 }, 
                        { field: "email", title: "메일" },
                        { field: "creationDate", title: "생성일", filterable: false,  width: 100, format: "{0:yyyy/MM/dd}" } ],         
                    filterable: true,
                    sortable: true,
                    pageable: { refresh:true, pageSizes:false,  messages: { display: ' {1} / {2}' }  },
                    selectable: 'row',
                    height: 420,
                    change: function(e) {                                 
                        var selectedCells = this.select();
  						if( selectedCells.length == 1){  						
                             var selectedCell = this.dataItem( selectedCells );                             
							 selectedUser.userId = selectedCell.userId ;         
							 selectedUser.username = selectedCell.username ;             
							 selectedUser.name = selectedCell.name ;                 
							 selectedUser.email = selectedCell.email ;                
							 selectedUser.creationDate = selectedCell.creationDate ;         
							 selectedUser.lastLoggedIn = selectedCell.lastLoggedIn ;         							 
							 selectedUser.formattedLastLoggedIn =  kendo.format("{0:yyyy.MM.dd}",  selectedUser.lastLoggedIn  );							 
							 selectedUser.lastProfileUpdate = selectedCell.lastProfileUpdate ;                 
							 selectedUser.formattedLastProfileUpdate =  kendo.format("{0:yyyy.MM.dd}",  selectedUser.lastProfileUpdate  );                   
							 selectedUser.enabled = selectedCell.enabled ;              
							 selectedUser.nameVisible = selectedCell.nameVisible ;          
							 selectedUser.emailVisible = selectedCell.emailVisible ;          
							 							                               
                             if( selectedUser.enabled ){
                             	$('#selected-user-disabled').hide();
                             } else {
                             	$('#selected-user-disabled').show();                
                             }
                              
                             // clear switch checkbox!!
                             
                             $('div.Switch').each(function() { 
                                 $(this).removeClass('On');
                                 $(this).removeClass('Off');
                             });
                              	                                           	             
                             kendo.bind($("#user-profile-fm"), selectedUser );                             
                             $(document).commonSwitchBox(); 
                                                                
                             if( selectedUser.userId > 0 ){                                 
							     panelBar.enable( panelBar.element.children("li") , true );
	                             panelBar.select( panelBar.element.children("li").eq(0) );
							     panelBar.expand( panelBar.element.children("li").eq(0) ); 
							     user_prop_datasource.read( { userId: selectedUser.userId } );							     
						     }
						     
                        }else{
                            selectedUser.clear();
						    panelBar.enable( panelBar.element.children("li") , false );
                        }                       
					},
					dataBound: function(e){   										
						 $('#panelbar').find( 'span[class*=k-state-selected]' ).each(
						     function ( index, value ) {
						          $( value ).removeClass("k-state-focused");   
						          $( value ).removeClass("k-state-selected");   
						     }
						 ) ;						
						 var selectedCells = this.select();
						 if(selectedCells.length == 0 ){						      
						      selectedUser.clear();
						      panelBar.collapse( panelBar.element.children("li") );   
						      panelBar.enable( panelBar.element.children("li") , false );
						 }
					}
                });                 
                                
                var user_prop_datasource = new kendo.data.DataSource({
						transport: { 
						      read: { url:'${request.contextPath}/secure/get-user-property.do?output=json', type:'post' },
						      create: { url:'${request.contextPath}/secure/update-user-property.do?output=json', type:'post' },
						      update: { url:'${request.contextPath}/secure/update-user-property.do?output=json', type:'post'  },
						      destroy: { url:'${request.contextPath}/secure/delete-user-property.do?output=json', type:'post' },
						 	  parameterMap: function (options, operation){
						 		  if (operation !== "read" && options.models) {
                                     return { userId: selectedUser.userId, items: kendo.stringify(options.models)};
                                 } 
		                         return { userId: options.userId }
		                     }
						 },						
						 batch: true, 
						 schema: {
	                            data: "targetUserProperty",
	                            model: {
	                                id:"name",
	                                fields: {
	                                    name: { type: "string" },
	                                    value: { type: "string" }
	                                }
	                            }
	                     }
                     }                
                );
                               
				var user_prop_grid = $("#user-prop-grid").kendoGrid({
				     dataSource: user_prop_datasource,
				     columns: [
				         { title: "속성", field: "name" },
				         { title: "값",   field: "value" },
				         { command:  { name: "destroy", text:"삭제" },  title: "&nbsp;", width: 100 }
				     ],
				     autoBind: false, 
				     pageable: false,
				     height: 200,
				     editable: true,
				     toolbar: [
				         { name: "create", text: "추가" },
                         { name: "save", text: "저장" },
                         { name: "cancel", text: "취소" }
					 ],				     
				     change: function(e) {  
				     }
			    });                
            }	
        }]);      
        </script>
    </head>
    <body>
            
            
	<section class="row">
	    <div class="seven columns last"><h4 class="subheader">사용자 관리</h4></div>
	</section>	            
	<!-- Main Page Content & Breadcrumbs -->   
    <section class="row">
	    <div class="seven columns"><div id="user-grid"></div></div>
	    <div class="five columns">	
	        <!-- selected user basic start -->        
	        <div class="row">	        
	            <div class="twelve columns">
		            <div id="detail-panel" class="panel">			    
		                  <form id="user-profile-fm">
		                      <div class="row" >			             
		                          <div class="three columns" >
		                              <a href="#" class="th"><img src="${request.contextPath}/images/user.png"></a>
		                          </div>         
		                          <div class="nine columns" >
                                          <table class="twelve">
											 <tbody>										 
											 	<tr>
											         <td class="five"><label>이름</label></td>
											         <td><span data-bind="text:name">&nbsp;</span>&nbsp;<span id="selected-user-disabled" style="display:none;" class="alert radius label">disabled</span></td>
											     </tr>
											    <tr>
											         <td><label>마지막 방문일</label></td>
											         <td>
		                                                 <span data-bind="text: formattedLastLoggedIn"></span>
													 </td>
											     </tr>
											     <tr>
											         <td><label for="checkbox1">계정 사용</label></td>
											         <td>
											             <div>
			                                                 <input type="checkbox" id="checkbox1" name="enabled"  style="display:none;" data-bind="checked: enabled" />
			                                                 <div class="Switch Round" for="checkbox1"><div class="Toggle"></div></div>
			                                             </div>
													 </td>
											     </tr>
											     <tr>
											         <td><label for="checkbox2">메일 공개</label></td>
											         <td>
											             <div>
														     <input type="checkbox" id="checkbox2" name="emailVisible" style="display:none;" data-bind="checked: emailVisible" />
														     <div class="Switch Round" for="checkbox2"><div class="Toggle"></div></div>
													     </div>
													 </td>
											     </tr>				
											     <tr>
											         <td><label for="checkbox3">이름 공개</label></td>
											         <td>
											             <div>
			                                                 <input type="checkbox" id="checkbox3" name="nameVisible" style="display:none;" data-bind="checked: nameVisible" />
			                                                 <div class="Switch Round" for="checkbox3" ><div class="Toggle"></div></div>
			                                             </div>    
		                                             </td>
											     </tr>										     					     
											 </tbody>  
										 </table>
							      </div>										 		                      
		                      </div>		                      
							<div class="row">
			    		        <div class="twelve columns" style="padding-top:15px;" align="right" >
			    		            <button type="button" id="update-user-btn" class="k-button small-text"> 정보 변경 </button>&nbsp;<button type="button" id="update-user-pwd-btn" class="k-button small-text">비밀번호 변경</button>
			    		        </div>		    		        
			    		    </div> 			    		    		                      
		                  </form>   
				    </div>			    	
			    </div>
	        </div>	        
	    	<!-- selected user basic end -->
            <div class="row">
                <div class="twelve columns">
					<ul id="panelbar">
		                <li id="panelbar-item-1" style="list-style: none;">
		                    프로퍼티
		                    <div>                  
		                    <div id="user-prop-grid" style="border:0px;"></div>
		                    </div>
		                </li>
		                <li style="list-style: none;">그룹<div>그룹정보를 보여줍니다.</div></li>
		                <li style="list-style: none;">롤<div>롤를 보여줍니다.</div></li>
		                <li style="list-style: none;">권한<div>권한 정보를 보여줍니다.</div></li>
		            </ul>	  	        
	            </div>
            </div>            
	    </div>
	</section>	
	
	<section class="row">
	    <div class="twelve columns">
	        <hr style="margin-top:10px;margin-bottom:10px;" />
			<ul class="breadcrumbs">
			  <li><a href="${request.contextPath}/main.do">홈</a></li>
			  <li class="unavailable"><a href="#">시스템</a></li>
			  <li class="current"><a href="#">사용자 관리</a></li>
			</ul>
	    </div>
	</section>	
	<!-- End Main Page Content & Breadcrumbs  -->	    
    </body>
</html>