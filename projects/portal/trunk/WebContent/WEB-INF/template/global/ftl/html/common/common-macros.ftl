<#macro compress_single_line>
	<#local captured><#nested></#local>
	${ captured?replace("^\\s+|\\s+$|\\n|\\r", "", "rm") }
</#macro>