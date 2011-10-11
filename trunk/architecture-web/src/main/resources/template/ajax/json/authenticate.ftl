{"response":{
<#if SecurityHelper.isApplicaitonUser() >
        "authentication":"<#if action.authnFailed>false<#else>true</#if>",
        "authorization":"<#if action.authzFailed>false<#else>true</#if>",
        "user":{
            "username":"${user.username}",
            "anonymous":"<#if user.anonymous>true<#else>false</#if>"
        }
    }
<#else>
<#assign authen = SecurityHelper.getAuthentication() >
        "authentication":"<#if authen.authenticated>false<#else>true</#if>",
        "authorization":"<#if action.authzFailed>false<#else>true</#if>",
        "user":{
            "username":"${authen.principal.username}",
            "anonymous":"<#if SecurityHelper.isAnonymous(authen) >true<#else>false</#if>"
        }
    }    
</#if>   
}
