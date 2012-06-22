package architecture.ee.spring.scripting;

import java.util.List;

/**
 * @author  donghyuck
 */
public class ScriptEngineDefinition {


	/**
	 * @uml.property  name="id"
	 */
	private String id;
    /**
	 * @uml.property  name="engineName"
	 */
    private String engineName;
    private List<String> fileTypes;

    public ScriptEngineDefinition() {
	}

    
    /**
	 * @param id
	 * @uml.property  name="id"
	 */
    public void setId(String id)
    {
        this.id = id;
    }

    /**
	 * @param engineName
	 * @uml.property  name="engineName"
	 */
    public void setEngineName(String engineName)
    {
        this.engineName = engineName;
    }

    /**
	 * @param fileTypes
	 * @uml.property  name="fileTypes"
	 */
    public void setFileTypes(List<String> fileTypes)
    {
        this.fileTypes = fileTypes;
    }

    /**
	 * @return
	 * @uml.property  name="id"
	 */
    public String getId()
    {
        return id;
    }

    /**
	 * @return
	 * @uml.property  name="engineName"
	 */
    public String getEngineName()
    {
        return engineName;
    }

    public String[] getFileTypesAsArray()
    {
        return (String[])(String[]) fileTypes.toArray(new String[fileTypes.size()]);
    }

}
