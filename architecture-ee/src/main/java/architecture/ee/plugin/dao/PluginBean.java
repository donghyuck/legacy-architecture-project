package architecture.ee.plugin.dao;

import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class PluginBean {

    private long id;
    private String name;
    private Date creationDate;
    private Date modifiedDate;

    public PluginBean()
    {
    }

    public void setID(long id)
    {
        this.id = id;
    }

    public long getID()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Date getCreationDate()
    {
        return creationDate;
    }

    public void setCreationDate(Date creationDate)
    {
        this.creationDate = creationDate;
    }

    public Date getModifiedDate()
    {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate)
    {
        this.modifiedDate = modifiedDate;
    }

    public int hashCode()
    {
    	
        return (new HashCodeBuilder(13, 11)).append(id).append(name).toHashCode();
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
            PluginBean other = (PluginBean)obj;
            return (new EqualsBuilder()).append(id, other.id).append(name, other.name).isEquals();
        }
    }

    public String toString()
    {
        return (new ToStringBuilder(this)).append("id", id).append("name", name).append("creationDate", creationDate).append("modifiedDate", modifiedDate).toString();
    }
    
}
