package architecture.ee.web.struts2.config;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import architecture.common.util.StringUtils;

import com.opensymphony.xwork2.config.entities.ActionConfig;
import com.opensymphony.xwork2.config.entities.ExceptionMappingConfig;
import com.opensymphony.xwork2.config.entities.InterceptorListHolder;
import com.opensymphony.xwork2.config.entities.InterceptorMapping;
import com.opensymphony.xwork2.config.entities.ResultConfig;
import com.opensymphony.xwork2.util.location.Location;

public class UnoverridableActionConfig extends ActionConfig
{
    private String caller;

    public UnoverridableActionConfig(UnoverridableActionConfig orig) {
		super(orig);
		this.caller = orig.caller;
	}

    public UnoverridableActionConfig(String packageName, String name, String className) {
		super(packageName, name, className);
	}
	
    public String getCaller()
    {
        return caller;
    }

    public void setCaller(String caller)
    {
        this.caller = caller;
    }

    

    /**
     * The builder for this object.  An instance of this object is the only way to construct a new instance.  The
     * purpose is to enforce the immutability of the object.  The methods are structured in a way to support chaining.
     * After setting any values you need, call the {@link #build()} method to create the object.
     */
    public static class Builder implements InterceptorListHolder{

        protected UnoverridableActionConfig target;
        
        private boolean gotMethods;

        public Builder(UnoverridableActionConfig toClone) {
            target = new UnoverridableActionConfig(toClone);
            addAllowedMethod(toClone.getAllowedMethods());
        }

        public Builder(String packageName, String name, String className) {
            target = new UnoverridableActionConfig(packageName, name, className);
        }

        public Builder packageName(String name) {
            target.packageName = name;
            return this;
        }

        public Builder name(String name) {
            target.name = name;
            return this;
        }

        public Builder className(String name) {
            target.className = name;
            return this;
        }

        public Builder defaultClassName(String name) {
            if (StringUtils.isEmpty(target.className)) {
                target.className = name;
            }
            return this;
        }

        public Builder methodName(String method) {
            target.methodName = method;
            return this;
        }

        public Builder addExceptionMapping(ExceptionMappingConfig exceptionMapping) {
            target.exceptionMappings.add(exceptionMapping);
            return this;
        }

        public Builder addExceptionMappings(Collection<? extends ExceptionMappingConfig> mappings) {
            target.exceptionMappings.addAll(mappings);
            return this;
        }

        public Builder exceptionMappings(Collection<? extends ExceptionMappingConfig> mappings) {
            target.exceptionMappings.clear();
            target.exceptionMappings.addAll(mappings);
            return this;
        }

        public Builder addInterceptor(InterceptorMapping interceptor) {
            target.interceptors.add(interceptor);
            return this;
        }

        public Builder addInterceptors(List<InterceptorMapping> interceptors) {
            target.interceptors.addAll(interceptors);
            return this;
        }

        public Builder interceptors(List<InterceptorMapping> interceptors) {
            target.interceptors.clear();
            target.interceptors.addAll(interceptors);
            return this;
        }

        public Builder addParam(String name, String value) {
            target.params.put(name, value);
            return this;
        }

        public Builder addParams(Map<String,String> params) {
            target.params.putAll(params);
            return this;
        }

        public Builder addResultConfig(ResultConfig resultConfig) {
            target.results.put(resultConfig.getName(), resultConfig);
            return this;
        }

        public Builder addResultConfigs(Collection<ResultConfig> configs) {
            for (ResultConfig rc : configs) {
                target.results.put(rc.getName(), rc);
            }
            return this;
        }

        public Builder addResultConfigs(Map<String,ResultConfig> configs) {
            target.results.putAll(configs);
            return this;
        }

        public Builder addAllowedMethod(String methodName) {
            target.allowedMethods.add(methodName);
            return this;
        }

        public Builder addAllowedMethod(Collection<String> methods) {
            if (methods != null) {
                gotMethods = true;
                target.allowedMethods.addAll(methods);
            }
            return this;
        }

        public Builder location(Location loc) {
            target.location = loc;
            return this;
        }


        public Builder caller(String caller) {
            target.caller = caller;
            return this;
        }

        public UnoverridableActionConfig build() {
            embalmTarget();
            UnoverridableActionConfig result = target;
            target = new UnoverridableActionConfig(target);
            return result;
        }

        protected void embalmTarget() {
            if (!gotMethods && target.allowedMethods.isEmpty()) {
                target.allowedMethods.add(WILDCARD);
            }
            target.params = Collections.unmodifiableMap(target.params);
            target.results = Collections.unmodifiableMap(target.results);
            target.interceptors = Collections.unmodifiableList(target.interceptors);
            target.exceptionMappings = Collections.unmodifiableList(target.exceptionMappings);
            target.allowedMethods = Collections.unmodifiableSet(target.allowedMethods);
        }
    }
}

