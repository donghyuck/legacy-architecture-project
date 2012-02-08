<html>
	<head>
		<title><#if page.title?exists && "" != page.title?trim>:</#if> ${page.title?default("")}</title>
	    ${page.head}
	</head>

    <body>
      <div id="pageTitle">${page.title}</div>
      <hr/>

      ${page.body}


      <#if !page.getProperty("meta.nofooter")??>
	      <div id="footer">
	          <b>Disclaimer:</b> This site is an example site to demonstrate SiteMesh. It serves no other purpose.
	      </div>        
      </#if>
	</body>
</html>
