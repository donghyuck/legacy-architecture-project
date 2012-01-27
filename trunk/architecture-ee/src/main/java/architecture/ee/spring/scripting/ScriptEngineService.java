package architecture.ee.spring.scripting;

import java.util.List;

import org.apache.bsf.BSFManager;

public class ScriptEngineService extends BSFManager {
	
    public void setScriptEngineDefinitions(List<ScriptEngineDefinition> defs)
    {    	
    	for(ScriptEngineDefinition def : defs){
    		registerScriptingEngine(def.getId(), def.getEngineName(), def.getFileTypesAsArray());
    	}
    }
    
}
