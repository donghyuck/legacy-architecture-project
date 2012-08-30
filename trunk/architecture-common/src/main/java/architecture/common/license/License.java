/*
 * Copyright 2012 Donghyuck, Son
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package architecture.common.license;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import architecture.common.exception.LicenseException;
import architecture.common.lifecycle.ApplicationConstants;
import architecture.common.util.L10NUtils;

/**
 * @author  donghyuck
 */
public class License {
    
	/**
	 * @author  donghyuck
	 */
	public static class Client implements Serializable {

		/**
		 * @uml.property  name="name"
		 */
		private String name;

		/**
		 * @uml.property  name="company"
		 */
		private String company;

		/**
		 * @uml.property  name="url"
		 */
		private String url;

		public Client() {
		}

		public Client(String name, String company) {
			this.name = name;
			this.company = company;
		}

		public Client(String name, String company, String url) {
			this.name = name;
			this.company = company;
			this.url = url;
		}

		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (o == null || getClass() != o.getClass())
				return false;
			Client client = (Client) o;
			if (company == null ? client.company != null : !company.equals(client.company))
				return false;
			return name == null ? client.name == null : name.equals(client.name);
		}

		/**
		 * @return
		 * @uml.property  name="company"
		 */
		public String getCompany() {
			return company;
		}

		/**
		 * @return
		 * @uml.property  name="name"
		 */
		public String getName() {
			return name;
		}

		/**
		 * @return
		 * @uml.property  name="url"
		 */
		public String getUrl() {
			return url;
		}
		public int hashCode() {
			int result = name == null ? 0 : name.hashCode();
			result = 11 * result + (company == null ? 0 : company.hashCode());
			return result;
		}
		/**
		 * @param company
		 * @uml.property  name="company"
		 */
		public void setCompany(String company) {
			this.company = company;
		}

		/**
		 * @param name
		 * @uml.property  name="name"
		 */
		public void setName(String name) {
			this.name = name;
		}

		/**
		 * @param url
		 * @uml.property  name="url"
		 */
		public void setUrl(String url) {
			this.url = url;
		}

		public String toString() {
			return L10NUtils.format("002103", name, company, url);
		}
	};

	/**
	 * @author  donghyuck
	 */
	public static class Module implements Serializable {

		/**
		 * @uml.property  name="name"
		 */
		private String name;

		public Module() {
		}

		public Module(String name) {
			this.name = name;
		}

		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (o == null || getClass() != o.getClass())
				return false;
			Module module = (Module) o;
			return name == null ? module.name == null : name.equals(module.name);
		}

		/**
		 * @return
		 * @uml.property  name="name"
		 */
		public String getName() {
			return name;
		}

		public int hashCode() {
			int result = name == null ? 0 : name.hashCode();
			return result;
		}

		/**
		 * @param name
		 * @uml.property  name="name"
		 */
		public void setName(String name) {
			this.name = name;
		}

		public String toString() {
			return L10NUtils.format("002104", name);
		}
	}

	/**
	 * @author   donghyuck
	 */
	public enum Type {

		COMMERCIAL,
		NON_COMMERCIAL,
		EVALUATION
	}
	
	/**
	 * @author  donghyuck
	 */
	public static class Version implements Comparable<Version>, Serializable {

		public static Version parseVersion(String versionString) {
			if (versionString == null)
				throw new LicenseException("Version string can not be null");
			Version version = new Version();
			String items[] = versionString.trim().split("\\.");
			try {
				version.major = Integer.parseInt(items[0]);
				version.minor = Integer.parseInt(items[1]);
				if (items.length > 2)
					if (items[2].indexOf(" ") > -1) {
						version.revision = Integer.parseInt(items[2].substring( 0, items[2].indexOf(" ")));
						version.extraVersionInfo = items[2].substring(items[2].indexOf(" "), items[2].length()).trim();
					} else {
						version.revision = Integer.parseInt(items[2]);
					}
			} catch (NumberFormatException e) {
				throw new LicenseException((new StringBuilder()).append("Illegal version ").append(versionString).toString());
			}
			return version;
		}

		/**
		 * @uml.property  name="major"
		 */
		private int major;

		/**
		 * @uml.property  name="minor"
		 */
		private int minor;

		/**
		 * @uml.property  name="revision"
		 */
		private int revision;

		/**
		 * @uml.property  name="extraVersionInfo"
		 */
		private String extraVersionInfo;

		private Version() {
		}

		public Version(int major, int minor, int release) {
			this.major = major;
			this.minor = minor;
			revision = release;
		}

		public Version(int major, int minor, int release, String versionString) {
			this.major = major;
			this.minor = minor;
			revision = release;
			extraVersionInfo = versionString;
		}

		public int compareTo(Version version) {
			if (major > version.major)
				return 1;
			if (major < version.major)
				return -1;
			if (minor > version.minor)
				return 1;
			return minor >= version.minor ? 0 : -1;
		}

		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (o == null || getClass() != o.getClass())
				return false;
			Version version = (Version) o;
			if (major != version.major)
				return false;
			if (minor != version.minor)
				return false;
			if (revision != version.revision)
				return false;
			else
				return extraVersionInfo == null ? version.extraVersionInfo != null : !extraVersionInfo.equals(version.extraVersionInfo);
		}

		/**
		 * @return
		 * @uml.property  name="extraVersionInfo"
		 */
		public String getExtraVersionInfo() {
			return extraVersionInfo;
		}

		/**
		 * @return
		 * @uml.property  name="major"
		 */
		public int getMajor() {
			return major;
		}

		/**
		 * @return
		 * @uml.property  name="minor"
		 */
		public int getMinor() {
			return minor;
		}

		/**
		 * @return
		 * @uml.property  name="revision"
		 */
		public int getRevision() {
			return revision;
		}

		public String getVersionString() {
			StringBuffer buffer = new StringBuffer();
			buffer.append(major);
			buffer.append(".");
			buffer.append(minor);
			buffer.append(".");
			buffer.append(revision);
			if (extraVersionInfo != null) {
				buffer.append(" ");
				buffer.append(extraVersionInfo);
			}
			return buffer.toString();
		}

		public int hashCode() {
			int result = major;
			result = 29 * result + minor;
			result = 29 * result + revision;
			result = 29 * result + (extraVersionInfo == null ? 0 : extraVersionInfo.hashCode());
			return result;
		}

		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append("Version");
			sb.append("{major=").append(major);
			sb.append(", minor=").append(minor);
			sb.append(", revision=").append(revision);
			sb.append(", extraVersionInfo='").append(extraVersionInfo).append('\'');
			sb.append('}');
			return sb.toString();
		}
	}

	private static final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    private static final Log log = LogFactory.getLog(License.class);
    private long id;
    /**
	 * @uml.property  name="name"
	 */
    private String name;
    /**
	 * @uml.property  name="edition"
	 */
    private String edition;
    /**
	 * @uml.property  name="creationDate"
	 */
    private Date creationDate;
    /**
	 * @uml.property  name="version"
	 * @uml.associationEnd  
	 */
    private Version version;
    /**
	 * @uml.property  name="client"
	 * @uml.associationEnd  
	 */
    private Client client;
    /**
	 * @uml.property  name="modules"
	 */
    private List<Module> modules;
    /**
	 * @uml.property  name="properties"
	 */
    private Map<String, String> properties;
    /**
	 * @uml.property  name="signature"
	 */
    private String signature;
    /**
	 * @uml.property  name="type"
	 * @uml.associationEnd  
	 */
    private Type type;
    
    public License()
    {
        properties = new HashMap<String, String>();
        modules = new ArrayList<Module>();
    }
    
    public License(String name, String edition, int versionMajor, int versionMinor, int versionRelease, String clientName, String clientCompany)
    {
        properties = new HashMap<String, String>();
        modules = new ArrayList<Module>();
        this.name = name;
        this.edition = edition;
        version = new Version(versionMajor, versionMinor, versionRelease);
        client = new Client(clientName, clientCompany);
    }
    
	public long getLicenseId() {
		return id;
	}

	public void setID(long id) {
		this.id = id;
	}

	/**
	 * @return
	 * @uml.property  name="name"
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 * @uml.property  name="name"
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return
	 * @uml.property  name="edition"
	 */
	public String getEdition() {
		return edition;
	}

	/**
	 * @param edition
	 * @uml.property  name="edition"
	 */
	public void setEdition(String edition) {
		this.edition = edition;
	}

	/**
	 * @return
	 * @uml.property  name="creationDate"
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * @return
	 * @uml.property  name="version"
	 */
	public Version getVersion() {
		return version;
	}

	/**
	 * @param version
	 * @uml.property  name="version"
	 */
	public void setVersion(Version version) {
		this.version = version;
	}

	public void setVersion(String version) {
		this.version = Version.parseVersion(version);
	}

	/**
	 * @param creationDate
	 * @uml.property  name="creationDate"
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * @return
	 * @uml.property  name="client"
	 */
	public Client getClient() {
		return client;
	}

	/**
	 * @param client
	 * @uml.property  name="client"
	 */
	public void setClient(Client client) {
		this.client = client;
	}

	public void setClient(String name, String company) {
		Client client = new Client(name, company);
		setClient(client);
	}

	/**
	 * @return
	 * @uml.property  name="modules"
	 */
	public List<Module> getModules() {
		return modules;
	}

	/**
	 * @param modules
	 * @uml.property  name="modules"
	 */
	public void setModules(List<Module> modules) {
		this.modules = modules;
	}

	public void setModule(String moduleName) {
		modules.add(new Module(moduleName));
	}

	public void setModules(String moduleNames[]) {
		for(String moduleName : moduleNames){
			modules.add(new Module(moduleName));			
		}
	}

	/**
	 * @return
	 * @uml.property  name="properties"
	 */
	public Map<String, String> getProperties() {
		return properties;
	}

	/**
	 * @param properties
	 * @uml.property  name="properties"
	 */
	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	/**
	 * @return
	 * @uml.property  name="signature"
	 */
	public String getSignature() {
		return signature;
	}

	/**
	 * @param signature
	 * @uml.property  name="signature"
	 */
	public void setSignature(String signature) {
		this.signature = signature;
	}

	/**
	 * @return
	 * @uml.property  name="type"
	 */
	public Type getType() {
		return type;
	}

	/**
	 * @param type
	 * @uml.property  name="type"
	 */
	public void setType(Type type) {
		this.type = type;
	}
	
	public byte[] getFingerprint()
    {
        StringBuffer buf = new StringBuffer(100);
        buf.append(id);
        buf.append(name);
        buf.append(version);
        buf.append(dateFormat.format(creationDate));
        buf.append(type.name());
        SortedSet<Module> sortedModules = new TreeSet<Module>(new Comparator<Module>() {
            public int compare(Module module, Module module1)
            {
                return module.name.compareTo(module1.name);
            }
        });
        
        sortedModules.addAll(modules);
        
        for( Module m : sortedModules){
        	buf.append(m.name);
        }
        

        SortedSet<String> sortedKeys = new TreeSet<String>(properties.keySet());
        for(String key: sortedKeys){        	
        	buf.append(key);
        }
        
        if(client != null)
        {
            buf.append(client.getCompany());
            buf.append(client.getName());
            buf.append(client.getUrl());
        }
        try
        {
            return buf.toString().getBytes(ApplicationConstants.DEFAULT_CHAR_ENCODING);
        }
        catch(UnsupportedEncodingException ue)
        {
            log.fatal(ue.getMessage(), ue);
        }
        return buf.toString().getBytes();
    }

	public boolean equals(Object o)
    {
        if(this == o)
            return true;
        if(o == null || getClass() != o.getClass())
            return false;
        License license = (License)o;
        if(id != license.id)
            return false;
        if(client == null ? license.client != null : !client.equals(license.client))
            return false;
        if(creationDate == null ? license.creationDate != null : !creationDate.equals(license.creationDate))
            return false;
        if(modules == null ? license.modules != null : !modules.equals(license.modules))
            return false;
        if(name == null ? license.name != null : !name.equals(license.name))
            return false;
        if(edition == null ? license.edition != null : !edition.equals(license.edition))
            return false;
        if(properties == null ? license.properties != null : !properties.equals(license.properties))
            return false;
        if(signature == null ? license.signature != null : !signature.equals(license.signature))
            return false;
        if(type != license.type)
            return false;
        return version == null ? license.version == null : version.equals(license.version);
    }
	

    public int hashCode()
    {
        int result = (int)(id ^ id >>> 32);
        result = 31 * result + (name == null ? 0 : name.hashCode());
        result = 31 * result + (edition == null ? 0 : edition.hashCode());
        result = 31 * result + (creationDate == null ? 0 : creationDate.hashCode());
        result = 31 * result + (version == null ? 0 : version.hashCode());
        result = 31 * result + (client == null ? 0 : client.hashCode());
        result = 31 * result + (modules == null ? 0 : modules.hashCode());
        result = 31 * result + (properties == null ? 0 : properties.hashCode());
        result = 31 * result + (signature == null ? 0 : signature.hashCode());
        result = 31 * result + (type == null ? 0 : type.hashCode());
        return result;
    }
    
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("License");
        sb.append("{id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", edition='").append(edition).append('\'');
        sb.append(", creationDate=").append(creationDate);
        sb.append(", version=").append(version);
        sb.append(", client=").append(client);
        sb.append(", modules=").append(modules);
        sb.append(", properties=").append(properties);
        sb.append(", signature='").append(signature).append('\'');
        sb.append(", type=").append(type);
        sb.append('}');
        return sb.toString();
    }
    
    public String toXML()
    {
        DocumentFactory factory = DocumentFactory.getInstance();
        Document document = factory.createDocument();
        Element root = document.addElement("license");
        
        root.addAttribute("id", String.valueOf(getLicenseId()));
        root.addAttribute("name", getName());
        if(edition != null)
            root.addAttribute("edition", getEdition());
        root.addAttribute("creationDate", formatDate(getCreationDate()));
        root.addAttribute("version", getVersion().getVersionString());
        root.addAttribute("type", getType().name());
        if(getClient() != null)
        {
            Element client = root.addElement("client");
            if(getClient().getName() != null)
                client.addAttribute("name", getClient().getName());
            if(getClient().getCompany() != null)
                client.addAttribute("company", getClient().getCompany());
        }
                
        for(Module m : getModules()){
        	Element me = root.addElement("module");
        	me.addAttribute("name", m.getName());
        }
        
        for(java.util.Map.Entry<String, String> entry : getProperties().entrySet()){
        	Element prop = root.addElement("property");
        	prop.addAttribute("name", (String)entry.getKey());
        	prop.setText((String)entry.getValue());
        }
        return document.asXML();
    }
    
    

    public static License fromXML(String xml)
    {
        try
        {
            Document d = DocumentHelper.parseText(xml);
            Element root = d.getRootElement();
            License l = new License();
            String id = root.attributeValue("id");
            if(id == null)
                throw new LicenseException(L10NUtils.format("002105"));
            l.setID(Long.parseLong(id));
            String name0 = root.attributeValue("name");
            if(name0 == null)
                throw new LicenseException(L10NUtils.format("002106"));
            l.setName(name0);
            String edition = root.attributeValue("edition");
            if(edition != null)
                l.setEdition(edition);
            String dateString = root.attributeValue("creationDate");
            if(dateString == null)
                throw new LicenseException(L10NUtils.format("002107"));
            try
            {
                Date date = parseDate(dateString);
                l.setCreationDate(date);
            }
            catch(Exception e)
            {
                throw new LicenseException(L10NUtils.format("002108"));
            }
            String license = root.attributeValue("version");
            if(license == null)
                throw new LicenseException(L10NUtils.format("002109"));
            l.setVersion(Version.parseVersion(license));
            try
            {
                l.setType(Type.valueOf(root.attributeValue("type")));
            }
            catch(IllegalArgumentException e)
            {
                throw new LicenseException(L10NUtils.format("002110"));
            }
            Element clientElement = root.element("client");
            Client client = new Client();
            client.setName(clientElement.attributeValue("name"));
            client.setCompany(clientElement.attributeValue("company"));
            l.setClient(client);
            
            for(Element e : (List<Element>)root.elements("module") ){
            	Module m = new Module();
            	m.setName(e.attributeValue("name"));
            	l.getModules().add(m);
            }
            
            
            Map<String, String> properties = new HashMap<String, String>();
            for(Element e : (List<Element>)root.elements("property")){
            	String name = e.attributeValue("name");
                String value = e.getTextTrim();
                properties.put(name, value);
            }
                        
            l.setProperties(properties);
            Element sig = root.element("signature");
            l.setSignature(sig.getTextTrim());
            return l;
        }
        catch(DocumentException e)
        {
            log.fatal(e.getMessage(), e);
            throw new LicenseException(L10NUtils.format("002111"), e);
        }
    }

    protected static Date parseDate(String string)
    {
        if(string != null)
            try
            {
                return dateFormat.parse(string);
            }
            catch(ParseException e)
            {
                return null;
            }
        else
            return null;
    }

    protected static String formatDate(Date date)
    {
        if(date == null)
            return "";
        else
            return dateFormat.format(date);
    }
}
