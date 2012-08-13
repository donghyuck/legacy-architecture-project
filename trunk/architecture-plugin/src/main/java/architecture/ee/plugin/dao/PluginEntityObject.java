package architecture.ee.plugin.dao;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class PluginEntityObject implements Serializable {

	private long pluginId = -1L;
	
	private String name;
	
	private Date creationDate;
	
	private Date modifiedDate;
	
		
    public long getPluginId() {
		return pluginId;
	}

	public void setPluginId(long pluginId) {
		this.pluginId = pluginId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public int hashCode()
    {
        return (new HashCodeBuilder(13, 11)).append(pluginId).append(name).toHashCode();
    }
    
    public boolean equals(Object obj)
    {
        if(this == obj)
            return true;
        if(obj == null)
            return false;
        if(getClass() != obj.getClass())
        {
            return false;
        } else
        {
        	PluginEntityObject other = (PluginEntityObject)obj;
            return (new EqualsBuilder()).append(pluginId, other.pluginId).append(name, other.name).isEquals();
        }
    }
    
    public String toString()
    {
        return (new ToStringBuilder(this)).append("id", pluginId).append("name", name).append("creationDate", creationDate).append("modificationDate", modifiedDate).toString();
    }
    
}
