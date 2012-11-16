<#ftl encoding="UTF-8"/>
<html decorator="banded">
    <head>
        <title>사용자 관리</title>
        <script type="text/javascript">
        yepnope([{
            load: [ 'css!${request.contextPath}/styles/kendo.common.min.css',
        	         'css!${request.contextPath}/styles/kendo.metro.min.css',
        	         '${request.contextPath}/js/jquery/1.7.2/jquery.min.js',      
        	         '${request.contextPath}/js/kendo/kendo.web.min.js' ],
            complete: function() {        	
		       
		       var selectedUser = -1;
		        // 1. User List Grid 		        
				var user_grid = $("#user-grid").kendoGrid({
                    dataSource: {
                        transport: { 
                            read: { url:'${request.contextPath}/accounts/admin/manage-user.do?output=json', type: 'POST', type:'jsonp' },
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
                                    creationDate: { type: "date" }
                                }
                            }
                        },
                        pageSize: 10,
                        serverPaging: true,
                        serverFiltering: false,
                        serverSorting: false
                    },
                    columns: [
                        { field: "userId", title: "ID", width:50,  filterable: false, sortable: false }, 
                        { field: "username", title: "아이디", width: 100 }, 
                        { field: "name", title: "이름", width: 200 }, 
                        { field: "email", title: "메일" },
                        { field: "creationDate", title: "생성일",  width: 100, format: "{0:yyyy/MM/dd}" } ],                    
                    filterable: true,
                    sortable: true,
                    pageable: { refresh:true, pageSizes:false,  messages: { display: ' {1} / {2}' }  },
                    selectable: 'row',
                    height: 300,
                    change: function(e) {  
						var userId =  this.dataItem( this.select() ).userId ;
						selectedUser = userId;
						user_prop_datasource.read( { userId: selectedUser } );
					},
					dataBound: function(e){   
						 $("#user-prop-grid").data("kendoGrid").refresh();
					}
                }); 
                
                var user_prop_datasource = new kendo.data.DataSource({
						transport: { 
						      read: { url:'${request.contextPath}/accounts/admin/user-property.do?output=json', type:'post' },
						      create: { url:'${request.contextPath}/accounts/admin/update-user-property.do?output=json', type:'post' },
						      update: { url:'${request.contextPath}/accounts/admin/update-user-property.do?output=json', type:'post'  },
						      destory: { url:'${request.contextPath}/accounts/admin/delete-user-property.do?output=json', type:'post' },
						 	  parameterMap: function (options, operation){
						 		 if (operation !== "read" && options.models) {
						 			 alert( selectedUser );
                                     return { userId: selectedUser, properties: kendo.stringify(options.models)};
                                 } 
		                         return { userId: options.userId }
		                     }
						 },						
						 batch: true, 
						 schema: {
	                            data: "targetUserProperty",
	                            model: {
	                                id:"key",
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
				         { command: "destroy", title: "&nbsp;", width: 100 }
				     ],
				     autoBind: false,
				     pageable: false,
				     height: 200,
				     editable: true,
				     toolbar: [
				         { name: "create", text: "추가" },
                         { name: "save", text: "저장" }
					 ],				     
				     change: function(e) {  

				     }
			    });                
            }	
        }]);      
        </script>
    </head>
    <body>
            
	<!-- Main Page Content & Breadcrumbs -->   
	<div class="row">	
 	<div class="nine columns push-three">      
        
        <h3></h3>                    
        <div id="user-grid"></div>


		<dl class="tabs">
		  <dd class="active"><a href="#simple1">프로퍼티</a></dd>
		  <dd><a href="#simple2">그룹</a></dd>
		  <dd><a href="#simple3">권한</a></dd>
		</dl>
		
		<ul class="tabs-content">
		  <li class="active" id="simple1Tab">
					<div id="user-prop-grid"></div>
          </li>
		  <li id="simple2Tab"><div id="user-info-grid"></div></li>
		  <li id="simple3Tab">This is simple tab 3's content. It's, you know...okay.</li>
		</ul>
    </div>    
    
    <!-- Nav Sidebar -->
    <!-- This is source ordered to be pulled to the left on larger screens -->
    <div class="three columns pull-nine">
	    <p><img src="http://placehold.it/320x240&text=Ad" /></p>    
		<dl class="nice vertical tabs">
		  <dd class="active"><a href="#vertical1">Vertical Tab 1</a></dd>
		  <dd><a href="#vertical2">Vertical Tab 2</a></dd>
		  <dd><a href="#vertical3">Vertical Tab 3</a></dd>
		</dl>  
    </div>
	
	</div>	
	<!-- End Main Content and Sidebar -->	    
    </body>
</html>