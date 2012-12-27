<#ftl encoding="UTF-8"/>
<html decorator="banded">
    <head>
        <title>회원정보</title>
        <script type="text/javascript">
        yepnope([{
        	  load: [ '${request.contextPath}/js/jquery/1.8.2/jquery.min.js',
        	          '${request.contextPath}/js/kendo/kendo.web.min.js',
        	          '${request.contextPath}/js/common.js',
        	          '${request.contextPath}/styles/kendo/kendo.common.min.css',
        	          '${request.contextPath}/styles/kendo/kendo.metro.min.css'
        	         ],
               	  complete: function() {        	
               		var crudServiceBaseUrl = "http://demos.kendoui.com/service",
                    dataSource = new kendo.data.DataSource({
                        transport: {
                            read:  {
                                url: crudServiceBaseUrl + "/Products",
                                dataType: "jsonp"
                            },
                            update: {
                                url: crudServiceBaseUrl + "/Products/Update",
                                dataType: "jsonp"
                            },
                            destroy: {
                                url: crudServiceBaseUrl + "/Products/Destroy",
                                dataType: "jsonp"
                            },
                            create: {
                                url: crudServiceBaseUrl + "/Products/Create",
                                dataType: "jsonp"
                            },
                            parameterMap: function(options, operation) {
                                if (operation !== "read" && options.models) {
                                    return {models: kendo.stringify(options.models)};
                                }
                            }
                        },
                        batch: true,
                        pageSize: 30,
                        schema: {
                            model: {
                                id: "ProductID",
                                fields: {
                                    ProductID: { editable: false, nullable: true },
                                    ProductName: { validation: { required: true } },
                                    UnitPrice: { type: "number", validation: { required: true, min: 1} },
                                    Discontinued: { type: "boolean" },
                                    UnitsInStock: { type: "number", validation: { min: 0, required: true } }
                                }
                            }
                        }
                    });

                $("#user-info-grid").kendoGrid({
                    dataSource: dataSource,
                    pageable: true,
                    height: 400,
                    toolbar: ["create"],
                    columns: [
                        { title: "ProductName", width: "150px" },
                        { field: "UnitPrice", title: "Unit Price", format: "{0:c}", width: "150px" },
                        { field: "UnitsInStock", title:"Units In Stock", width: "150px" },
                        { field: "Discontinued", width: "100px" },
                        { command: ["edit", "destroy"], title: "&nbsp;", width: "200px" }],
                    editable: "inline"
                });           
               		  
               		$("#user-props-grid").kendoGrid({
                        height: 250
                    });
            	  } 
            	}]);      
        </script>
    </head>
    <body>
            
	<!-- Main Page Content & Breadcrumbs -->   
	<div class="row">	
 	<div class="nine columns push-three">      
      <h3>회원정보 <small>${ user.name } </small></h3>
 		<form name="fm1"  method="POST" >
        <input type="hidden" id="output" name="output" value="json" />        
   <div class="row">
    <div class="two mobile-one columns">
      <label class="right inline">Address Name:</label>
    </div>
    <div class="ten mobile-three columns">
      <input type="text" placeholder="e.g. Home" class="eight" />
    </div>
  </div>
  <div class="row">
    <div class="two mobile-one columns">
      <label class="right inline">City:</label>
    </div>
    <div class="ten mobile-three columns">
      <input type="text" class="eight" />
    </div>
  </div>
  <div class="row">
    <div class="two mobile-one columns">
      <label class="right inline">ZIP:</label>
    </div>
    <div class="ten mobile-three columns">
      <input type="text" class="three" />
    </div>
  </div>
  
        </form>            
      
      
		<dl class="tabs">
		  <dd class="active"><a href="#simple1">프로퍼티</a></dd>
		  <dd><a href="#simple2">그룹</a></dd>
		  <dd><a href="#simple3">권한</a></dd>
		</dl>
		
		<ul class="tabs-content">
		  <li class="active" id="simple1Tab">
					<table id="user-props-grid">
		                <thead>
		                    <tr>
		                        <th data-field="prop-key">프로퍼티</th>
		                        <th data-field="prop-val">값</th>
		                    </tr>
		                </thead>
		                <tbody>
		                <#assign item = user.properties >
		                <#list item?keys as key>
		                    <tr>
		                        <td>${key}</td><td>${ item[key] }</td>
		                    </tr>		                    
		                </#list>
		                </tbody>
		            </table>
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