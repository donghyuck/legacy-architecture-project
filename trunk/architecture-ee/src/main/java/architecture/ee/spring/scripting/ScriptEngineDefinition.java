package architecture.ee.spring.scripting;

import java.util.List;

public class ScriptEngineDefinition {


	private String id;
    private String engineName;
    private List<String> fileTypes;

    public ScriptEngineDefinition() {
	}

    
    public void setId(String id)
    {
        this.id = id;
    }

    public void setEngineName(String engineName)
    {
        this.engineName = engineName;
    }

    public void setFileTypes(List<String> fileTypes)
    {
        this.fileTypes = fileTypes;
    }

    public String getId()
    {
        return id;
    }

    public String getEngineName()
    {
        return engineName;
    }

    public String[] getFileTypesAsArray()
    {
        return (String[])(String[]) fileTypes.toArray(new String[fileTypes.size()]);
    }

}
