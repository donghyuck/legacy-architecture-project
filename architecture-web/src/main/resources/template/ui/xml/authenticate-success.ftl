<?xml version="1.0" encoding="UTF-8" ?>
<response>
    <success>
	    <username>${user.username}</username>
	    <anonymous><#if user.anonymous>true<#else>false</#if></anonymous>
	    <#if !actionMessages.empty>
	    <#list actionMessages as actionMessage>
	    <welcome>${actionMessage!?html}</welcome>
	    </#list>    
	    </#if>	    
    </success>
</response>