<!DOCTYPE html>
<html lang="ko">
  <head>
    <meta charset="utf-8" />
    <title><@s.text name="main.page.title" /></title>
  </head>  
  <body>
    <header>
		<link rel="stylesheet" href="/styles/menu.css" type="text/css" media="all"> 
		<script src="/includes/scripts/yepnope/1.0.2/yepnope.min.js"></script>
		<script>
		
		yepnope([{
		  load:  ['preload!/includes/scripts/ext-core/3.1.0/ext-core.js',
		          'preload!/scripts/menu.js'],
		  complete: function () {           
		    
			new Ext.ux.Menu('simple-horizontal-menu', {
		       transitionType: 'slide',
		       direction: 'horizontal', // default  
		       delay: 0.2,              // default
		       autoWidth: true,         // default
		       transitionDuration: 0.3, // default
		       animate: true,           // default
		       currentClass: 'current'  // default
		    });
		    
		  }
		}]);
		
		</script>
		<style>

		.ux-menu a.current {
		    background-image: url('/images/menu-item-bg-current.png');
		    border-color: #cbc0b7;
		}

		</style>
		</header>
  	<section>  
  	<#if user.anonymous>
      anonymous
  	<#else>
        ${user}  
        ${SecurityHelper.getUserRoles()}
  	</#if>
        <h3>Simple horizontal menu</h3>
        <ul id="simple-horizontal-menu">
    		<li>
    		    <a href="#">menu item 1</a>
                <ul>
                    <li><a href="#">sub menu item 1</a></li>
                    <li>
                        <a href="#">sub menu item 2</a>
                        <ul>
                            <li><a href="#">sub sub menu item 1</a></li>
                            <li><a href="#">sub sub menu item 2</a></li>
                            <li><a href="#">sub sub menu item 3</a></li>
                        </ul>
                    </li>
                    <li><a href="#">sub menu item 3</a></li>
                </ul>
            </li>
            <li><a href="#">menu item 2</a></li>
            <li>
                <a href="#">menu item 3</a>
                <ul>
                    <li><a href="#">sub menu item 4</a></li>
                    <li><a href="#">sub menu item 5</a></li>
                    <li><a href="#">sub menu item 6</a></li>
                </ul>
            </li>
            <li><a href="#">menu item 4</a></li>
        </ul>
  	</section>
  	<tailer></tailer>  
	</body>
</html>