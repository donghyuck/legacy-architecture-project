<#ftl encoding="UTF-8"/>
<html decorator="banded">
    <head>
        <title>그룹 관리</title>
        <script type="text/javascript">
        yepnope([{
            load: [ 
                    'css!${request.contextPath}/styles/kendo/kendo.common.min.css',
        	        'css!${request.contextPath}/styles/kendo/kendo.metro.min.css',
        	        '${request.contextPath}/js/kendo/kendo.web.min.js' ],        	        
            complete: function() {       
                // 1. Menu 
                                
		        // 2. panelBar
		        var panelBar = $("#panelbar").kendoPanelBar({ expandMode: "single" } ).data("kendoPanelBar");  
		        
		        // 3. Group Grid 		        				
		        var selectedGroupId = 0 ;		        
		        var group_grid = $("#group-grid").kendoGrid({
                    dataSource: {	
                        transport: { 
                            read: { url:'${request.contextPath}/secure/main-group.do?output=json', type:'jsonp' },
                            create: { url:'${request.contextPath}/secure/create-group.do?output=json', type:'post' },             
                            update: { url:'${request.contextPath}/secure/update-group.do?output=json', type:'post' },
	                        parameterMap: function (options, operation){	          
	                            if (operation != "read" && options) {
	                                return { groupId: options.groupId, item: kendo.stringify(options)};
	                            }else{
	                                return { startIndex: options.skip, pageSize: options.pageSize }
	                            }
	                        }                  
                        },
                        schema: {
                            total: "totalGroupCount",
                            data: "groups",
                            model: {
                                id:"groupId",
                                fields: {
                                    groupId: { type: "number", editable: false },
                                    name: { type: "string", validation: { required: true }},
                                    description: { type: "string" },
                                    modifiedDate: { type: "date", editable: false },
                                    creationDate: { type: "date", editable: false }
                                }
                            },
                        },
                        pageSize: 10,
                        serverPaging: true,
                        serverFiltering: false,
                        serverSorting: false,                        
                        error: function(e) {
					            $("#group-grid").data("kendoGrid").dataSource.read();					        
					    }
                    },
                    columns: [
                        { field: "groupId", title: "ID", width:40,  filterable: false, sortable: false }, 
                        { field: "name",    title: "이름",  filterable: true, sortable: true,  width: 100 }, 
                        { field: "description", title: "설명", width: 100, filterable: false, sortable: false },
                       <!-- { field: "modifiedDate", title: "수정일",  width: 80, format: "{0:yyyy/MM/dd}" }, -->
                       <!-- { field: "creationDate",  title: "생성일",  width: 80, format: "{0:yyyy/MM/dd}" }, -->
                        { command: [ {name:"edit", text:"수정"}  ], title: "&nbsp;" }],
                    detailInit: detailInit,    
                    filterable: true,
                    editable: "inline",
                    selectable: 'row',
                    height: 420,
                    batch: false,
                    toolbar: [ { name: "create", text: "그룹추가" } ],                    
                    pageable: { refresh:true, pageSizes:false,  messages: { display: ' {1} / {2}' }  },                    
                    change: function(e) {                
                        var selectedCells = this.select();
                        if( selectedCells.length == 1){
                             var selectedCell = this.dataItem( selectedCells );
                             selectedGroupId = selectedCell.groupId ;	                                                          
                             $('#selected-group-title').text(selectedCell.name);                                                 
                             if( selectedGroupId > 0 ){                                 
							     panelBar.enable( panelBar.element.children("li") , true );
	                             panelBar.select( panelBar.element.children("li").eq(0) );
							     panelBar.expand( panelBar.element.children("li").eq(0) ); 
							     group_prop_datasource.read( { groupId: selectedGroupId } );							     
						     }
                        }else{
                            $('#toptitle').text("");
                            selectedGroupId = 0 ;
						    panelBar.enable( panelBar.element.children("li") , false );
                        }
					},					
					dataBound: function(e){   						 
						 $( '#panelbar' ).find( 'span[class*=k-state-selected]' ).each(
						     function ( index, value ) {
						          $( value ).removeClass("k-state-focused");   
						          $( value ).removeClass("k-state-selected");   
						     }
						 ) ;					     
						  var selectedCells = this.select();
						 if(selectedCells.length == 0 ){						      
						      $('#selected-group-title').text("");
						      panelBar.collapse( panelBar.element.children("li") );   
						      panelBar.enable( panelBar.element.children("li") , false );				      						      
						 }
					}
                }); 
                
               // 4. Group Props Grid                  
                var group_prop_datasource = new kendo.data.DataSource({
						transport: { 
						      read: { url:'${request.contextPath}/secure/get-group-property.do?output=json', type:'post' },
						      create: { url:'${request.contextPath}/secure/update-group-property.do?output=json', type:'post' },
						      update: { url:'${request.contextPath}/secure/update-group-property.do?output=json', type:'post'  },
						      destroy: { url:'${request.contextPath}/secure/delete-group-property.do?output=json', type:'post' },
						 	  parameterMap: function (options, operation){			
						 		 if (operation !== "read" && options.models) {
						 		    return { groupId: selectedGroupId, items: kendo.stringify(options.models)};
                                 } 
		                         return { groupId: options.groupId }
		                     }
						 },						
						 batch: true, 
						 schema: {
	                            data: "targetGroupProperty",
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
                               
				var group_prop_grid = $("#group-prop-grid").kendoGrid({
				     dataSource: group_prop_datasource,
				     columns: [
				         { title: "속성", field: "name" },
				         { title: "값",   field: "value" },
				         { command:  { name: "destroy", text:"삭제" },  title: "&nbsp;", width: 100 }
				     ],
				     pageable: false,
				     height: 200,
				     editable: true,
				     toolbar: [
				         { name: "create", text: "추가" },
                         { name: "save", text: "저장" },
                         { name: "cancel", text: "취소" }
					 ],				     
				     change: function(e) {}
			    });                
            }	
        }]);      

        <!--  그룹 상세 -->        
		function detailInit(e) {
            $("<div/>").appendTo(e.detailCell).kendoGrid({
                dataSource: {
                    type: "odata",
                    transport: {
                        read: "http://demos.kendoui.com/service/Northwind.svc/Orders"
                    },
                    serverPaging: true,
                    serverSorting: true,
                    serverFiltering: true,
                    pageSize:6,
                    filter: { 
                        field: "EmployeeID", 
                        operator: "eq", 
                        value: e.data.EmployeeID 
                    }
                },
                scrollable: false,
                sortable: true,
                pageable: true,
                columns: [
                    { field: "userId", title: "ID", width:50,  filterable: false, sortable: false }, 
                    { field: "username", title: "아이디", width: 100 }, 
                    { field: "name", title: "이름", width: 100 }, 
                    { field: "admin", title: "관리자", width: 200 }
                ]
            });
         }                            
        </script>
    </head>
    <body>
    
	<section class="row">
	    <div class="seven columns last"><h4 class="subheader">그룹 관리</h4></div>
	</section>    

	<!-- Main Page Content & Breadcrumbs -->   
    <section class="row">   
	    <div class="seven columns"><div id="group-grid"></div></div>
	    <div class="five columns">
	                <!--
	    		    <div id="detail-panel" class="panel">
	    		        <div class="row">
	    		            <div class="three columns"> <a href="#" class="th"><img src="${request.contextPath}/images/groups.png"></a></div>
	    		            <div class="nine columns">
	    		          				<table class="twelve">
											 <tbody>										 
											 	<tr>
											         <td class="five"><label>그룹</label></td>
											         <td><span id="selected-group-title"></span></td>
											     </tr>
											    <tr>
											         <td></td>
											         <td>		                                                 
													 </td>
											     </tr>							     					     
											 </tbody>  
										 </table>
	    		            </div>
	    		        </div>
                    </div>
                    -->
					<ul id="panelbar">
	                    <li id="panelbar-item-1">
	                        프로퍼티
	                        <div>  
	                        	<div id="group-prop-grid" style="border:0px;"></div>
	                        </div>
	                    </li>
	                    <li>멤버<div>그룹 멤버정보를 보여줍니다.</div></li>
	                    <li>관리자<div>그룹 관리자정보를 보여줍니다.</div></li>
	                    <li>그룹 롤/권한<div>그룹 롤/권한 정보를 보여줍니다.</div></li>
	                </ul>	    
	    </div>	
	</section>
	<section class="row">
	    <div class="twelve columns">
	        <hr style="margin-top:10px;margin-bottom:10px;" />
			<ul class="breadcrumbs">
			  <li><a href="${request.contextPath}/main.do">홈</a></li>
			  <li class="unavailable"><a href="#">시스템</a></li>
			  <li class="current"><a href="#">그룹 관리</a></li>
			</ul>
		</div>
	</section>
	<!-- End Main Content and Breadcrumbs -->	    
    </body>
</html>