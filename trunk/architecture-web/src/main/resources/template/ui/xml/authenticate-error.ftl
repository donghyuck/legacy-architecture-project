<?xml version="1.0" encoding="UTF-8" ?>
<response>
    <#if !actionErrors.empty>
    <#list actionErrors as actionError>
    <error>${actionError!?html}</error>
    </#list>    
    </#if>
</response>