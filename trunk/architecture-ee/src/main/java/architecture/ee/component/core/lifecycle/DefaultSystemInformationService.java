/*
 * Copyright 2012, 2013 Donghyuck, Son
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
package architecture.ee.component.core.lifecycle;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import architecture.common.jdbc.JdbcUtils;
import architecture.common.license.License;
import architecture.common.license.LicenseManager;
import architecture.common.lifecycle.ComponentImpl;
import architecture.common.lifecycle.ConfigService;
import architecture.common.lifecycle.DatabaseInfo;
import architecture.common.lifecycle.MemoryInfo;
import architecture.common.lifecycle.SystemInfo;
import architecture.common.lifecycle.SystemInformationService;
import architecture.common.lifecycle.service.AdminService;
import architecture.common.util.RuntimeHelper;
import architecture.common.util.RuntimeHelperFactory;
import architecture.ee.component.admin.AdminHelper;
import architecture.ee.spring.lifecycle.SpringAdminService;

public class DefaultSystemInformationService extends ComponentImpl  implements
		SystemInformationService {

	private String dataSourceName ;
    
	private LicenseManager licenseManager ;	
	
	private ConfigService configService ;	
		
	public ConfigService getConfigService() {
		return configService;
	}

	
	public void setConfigService(ConfigService configService) {
		this.configService = configService;
	}

	public String getDataSourceName() {
		return dataSourceName;
	}

	public void setDataSourceName(String dataSourceName) {
		this.dataSourceName = dataSourceName;
	}

	public LicenseManager getLicenseManager() {
		return licenseManager;
	}

	public void setLicenseManager(LicenseManager licenseManager) {
		this.licenseManager = licenseManager;
	}
	
	public SystemInfo getSystemInfo() {
		
		RuntimeHelper helper = RuntimeHelperFactory.getRuntimeHelper();
		
		SystemInfo info = new SystemInfo();
		Date now = new Date();
		info.setDate( new SimpleDateFormat("EEEEEE, yyyy MMM dd", Locale.KOREA).format( now ) );
		info.setTime( new SimpleDateFormat("HH:mm:ss", Locale.KOREA).format( now ) );
		 
		Properties sysProps = System.getProperties();
		info.setJavaVersion(sysProps.getProperty("java.version"));
	    info.setJavaVendor(sysProps.getProperty("java.vendor"));
	    info.setJvmVersion(sysProps.getProperty("java.vm.specification.version"));
	    info.setJvmVendor(sysProps.getProperty("java.vm.specification.vendor"));
	    info.setJvmImplementationVersion(sysProps.getProperty("java.vm.version"));
	    info.setJavaRuntime(sysProps.getProperty("java.runtime.name"));
	    info.setJavaVm(sysProps.getProperty("java.vm.name"));
	    info.setUserName(sysProps.getProperty("user.name"));
	    info.setSystemLanguage(sysProps.getProperty("user.language"));
	    info.setSystemTimezone(sysProps.getProperty("user.timezone"));
	    info.setOperatingSystem((new StringBuilder()).append(sysProps.getProperty("os.name")).append(" ").append(sysProps.getProperty("os.version")).toString());
	    info.setOperatingSystemArchitecture(sysProps.getProperty("os.arch"));
	    info.setFileSystemEncoding(sysProps.getProperty("file.encoding"));
	    info.setJvmInputArguments(helper.getJvmInputArguments());
	    info.setWorkingDirectory(sysProps.getProperty("user.dir"));
	    info.setTempDirectory(sysProps.getProperty("java.io.tmpdir"));
		
	    return info;
	}

	public MemoryInfo getMemoryInfo() {
		MemoryInfo mem = new MemoryInfo(); 
		return mem;
	}

	public License getLicenseInfo() {		
		return licenseManager.getLicense();
	}

	public List<DatabaseInfo> getDatabaseInfos() {
		List<DatabaseInfo> databaseInfos = new ArrayList<DatabaseInfo>();
		AdminService admin = AdminHelper.getAdminService();		
		if( admin instanceof SpringAdminService ){
			SpringAdminService sas = (SpringAdminService)admin ;
			
			Map<String, DataSource> beans1 = sas.getApplicationContext().getParent().getBeansOfType(DataSource.class);
			Map<String, DataSource> beans2 =  sas.getApplicationContext().getBeansOfType(DataSource.class);
		
			for( Map.Entry<String,DataSource> entry : beans1.entrySet()){
				String name = entry.getKey();
				DataSource dataSource = entry.getValue();
				DatabaseInfo info = new DatabaseInfo(JdbcUtils.getDatabaseType(dataSource));				
				info.setName(name);
				databaseInfos.add( info );	            
	            log.debug( "parent context :" + info.toString() );	            
			}
			for( Map.Entry<String,DataSource> entry : beans2.entrySet()){
				String name = entry.getKey();
				DataSource dataSource = entry.getValue();
				DatabaseInfo info = new DatabaseInfo(JdbcUtils.getDatabaseType(dataSource));				
				info.setName(name);
				databaseInfos.add( info );	            
	            log.debug( "context :" + info.toString() );	            
			}
		}
		
		return databaseInfos;
	}


}
