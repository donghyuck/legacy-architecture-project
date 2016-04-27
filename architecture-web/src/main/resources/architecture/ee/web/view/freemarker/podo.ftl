<#ftl strip_whitespace=true>
<#macro compress>
	<#local captured><#nested></#local>
	${ captured?replace("^\\s+|\\s+$|\\n|\\r", "", "rm") }
</#macro>