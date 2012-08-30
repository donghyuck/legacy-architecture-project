<?xml version="1.0" encoding="UTF-8"?>
<response>
  <totalItemSize>${list?size}</totalItemSize>
  <items>
<#list list as item>
    <item>
    <#list item?keys as key>
        <${key}>${item[key]}</${key}>
    </#list>
    </item>
</#list>
  </items>
</response>