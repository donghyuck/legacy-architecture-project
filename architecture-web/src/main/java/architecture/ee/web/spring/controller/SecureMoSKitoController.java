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
package architecture.ee.web.spring.controller;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.management.BadAttributeValueExpException;
import javax.management.BadBinaryOpValueExpException;
import javax.management.BadStringOperationException;
import javax.management.InvalidApplicationException;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.QueryExp;

import net.anotheria.moskito.core.accumulation.AccumulatedValue;
import net.anotheria.moskito.core.accumulation.Accumulator;
import net.anotheria.moskito.core.accumulation.AccumulatorRepository;
import net.anotheria.moskito.core.predefined.IMemoryPoolStats;
import net.anotheria.moskito.core.predefined.OSStats;
import net.anotheria.moskito.core.predefined.RequestOrientedStats;
import net.anotheria.moskito.core.predefined.RuntimeStats;
import net.anotheria.moskito.core.producers.IStats;
import net.anotheria.moskito.core.producers.IStatsProducer;
import net.anotheria.moskito.core.registry.IProducerFilter;
import net.anotheria.moskito.core.registry.IProducerRegistry;
import net.anotheria.moskito.core.registry.IProducerRegistryAPI;
import net.anotheria.moskito.core.registry.ProducerRegistryAPIFactory;
import net.anotheria.moskito.core.registry.ProducerRegistryFactory;
import net.anotheria.moskito.core.threshold.Threshold;
import net.anotheria.moskito.core.threshold.ThresholdRepository;
import net.anotheria.moskito.core.threshold.alerts.AlertHistory;
import net.anotheria.moskito.core.threshold.alerts.ThresholdAlert;
import net.anotheria.moskito.web.session.SessionCountStats;
import net.anotheria.util.maven.MavenVersionReader;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.NativeWebRequest;

import architecture.ee.exception.NotFoundException;
import architecture.ee.web.monitoring.moskito.AccumulatedSingleGraphObject;
import architecture.ee.web.monitoring.moskito.AccumulatedValueObject;
import architecture.ee.web.monitoring.moskito.AccumulatorDefinitionObject;
import architecture.ee.web.monitoring.moskito.AccumulatorObject;
import architecture.ee.web.monitoring.moskito.LibraryObject;
import architecture.ee.web.monitoring.moskito.ProducerObject;
import architecture.ee.web.monitoring.moskito.StatLineObject;
import architecture.ee.web.monitoring.moskito.StatValue;
import architecture.ee.web.monitoring.moskito.ThresholdAlertObject;
import architecture.ee.web.monitoring.moskito.ThresholdStatusObject;

@Controller ("secure-moskito-data-controller")
@RequestMapping("/secure/data")
public class SecureMoSKitoController {

	private static final Log log = LogFactory.getLog(SecureMoSKitoController.class);
	private static final IProducerRegistryAPI  producerRegistryAPI = new ProducerRegistryAPIFactory().createProducerRegistryAPI();
	
	public SecureMoSKitoController() {
	}
	
	@RequestMapping(value="/stage/more/list_library.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public List<LibraryObject> getLibraries(
			NativeWebRequest request) throws Exception 	
	{	
		ArrayList<LibraryObject> aBeans = new ArrayList<LibraryObject>();
		String context = request.getContextPath();
		if( StringUtils.isEmpty(context) )
			context = "/";
		
		List<URL> classpath = getClassPathUrls(context);
		for (URL url : classpath){
			String fileName = url.getFile();
			if (!fileName.endsWith(".jar"))
				continue;
			File f = new File(fileName);
			LibraryObject bean = new LibraryObject();
			int lastSlash = fileName.lastIndexOf('/');
			try{
				bean.setName(fileName.substring(lastSlash + 1));
				bean.setMavenVersion(MavenVersionReader.readVersionFromJar(f));				
				if (bean.getMavenVersion()==null){
					bean.setLastModified(new Date(f.lastModified()));
				}
			}catch(Exception e){
				log.warn("couldn't obtain lib version, skipped this url "+url, e);
			}
			aBeans.add(bean);
		}
		
		return aBeans;
	}
	
	private List<URL> getClassPathUrls(final String context){
		log.debug("context: " + context);
		List<URL> forTomcat7 = getClassPathUrlsForTomcat(context, "context");
		if (forTomcat7!=null && forTomcat7.size()>0)
			return forTomcat7;
		List<URL> forTomcat6 = getClassPathUrlsForTomcat(context, "path");
		if (forTomcat6!=null && forTomcat6.size()>0)
			return forTomcat6;
		//add another lookup methods here.
		return new ArrayList<URL>();
	}
	
	private List<URL> getClassPathUrlsForTomcat(final String context, final String contextPropertyName) {
		List<MBeanServer> servers = MBeanServerFactory.findMBeanServer(null);
		for (MBeanServer s : servers) {
			Set<ObjectInstance> instances = s.queryMBeans(null, new QueryExp() {
 
				public boolean apply(ObjectName name)
						throws BadStringOperationException,
						BadBinaryOpValueExpException,
						BadAttributeValueExpException,
						InvalidApplicationException {
					String type = name.getKeyProperty("type");
					log.debug( name.getDomain()  + " : " + name.getKeyPropertyList() );
					if (!type.equals("WebappClassLoader"))
						return false;
					if (!name.getDomain().equals("Catalina"))
						return false;
					if (!name.getKeyProperty(contextPropertyName).equals(context))
						return false;
					return true;
				}
 
				public void setMBeanServer(MBeanServer s) {
				}
			});
			if (instances.size() > 0) {
				try {
					URL[] urls = (URL[]) s.getAttribute(instances.iterator().next().getObjectName(), "URLs");
					return Arrays.asList(urls);
				} catch (Exception e) {
				}

			}
		}
		return null;
	}
	
	
	@RequestMapping(value="/stage/thresholds/alerts.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public List<ThresholdAlertObject> getAlerts(
			NativeWebRequest request) throws Exception 	
	{	
		ArrayList<ThresholdAlertObject> aBeans = new ArrayList<ThresholdAlertObject>();
		for (ThresholdAlert alert : AlertHistory.INSTANCE.getAlerts()){
			ThresholdAlertObject alertBean = new ThresholdAlertObject();
			alertBean.setId(alert.getThreshold().getId());
			alertBean.setName(alert.getThreshold().getName());
			alertBean.setOldColorCode(alert.getOldStatus().toString().toLowerCase());
			alertBean.setOldStatus(alert.getOldStatus().toString());
			alertBean.setOldValue(alert.getOldValue());
			alertBean.setNewColorCode(alert.getNewStatus().toString().toLowerCase());
			alertBean.setNewStatus(alert.getNewStatus().toString());
			alertBean.setNewValue(alert.getNewValue());
			alertBean.setUpdatedDate(new Date( alert.getTimestamp()));
			aBeans.add(alertBean);
		}
		return aBeans;
	}
	
	@RequestMapping(value="/stage/thresholds/list.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public List<ThresholdStatusObject> getThresholds(
			NativeWebRequest request) throws Exception 	
	{	
		List<Threshold> thresholds = ThresholdRepository.getInstance().getThresholds();
		ArrayList<ThresholdStatusObject> ret = new ArrayList<ThresholdStatusObject>();
		for (Threshold t : thresholds){
			ThresholdStatusObject status = new ThresholdStatusObject();
			status.setName(t.getName());
			status.setColorCode(t.getStatus().toString().toLowerCase());
			status.setStatus(t.getStatus().toString().toLowerCase());
			status.setDescription(t.getDefinition().describe());
			status.setValue(t.getLastValue());
			status.setPreviousColorCode(t.getPreviousStatus().toString().toLowerCase());
			status.setPreviousStatus(t.getPreviousStatus().toString().toLowerCase());			
			status.setUpdatedDate(t.getStatusChangeTimestamp() == 0 ? null : new Date(t.getStatusChangeTimestamp()));
			status.setStatusForSorting(t.getStatus());
			status.setId(t.getId());
			status.setFlipCount(t.getFlipCount());
			ret.add(status);
		}
		return ret;
	}
	
	@RequestMapping(value="/stage/accumulators/list.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public List<AccumulatorDefinitionObject> getAccumulatorDefinitions (
			NativeWebRequest request) throws Exception 	
	{	
		List<Accumulator> accumulators = AccumulatorRepository.getInstance().getAccumulators();
		List<AccumulatorDefinitionObject> ret = new ArrayList<AccumulatorDefinitionObject>();
		for (Accumulator a : accumulators){
			AccumulatorDefinitionObject bean  = new AccumulatorDefinitionObject();
			bean.setName(a.getName());
			bean.setPath(a.getDefinition().describe());
			bean.setId(a.getId());
			List<AccumulatedValue> values = a.getValues();
			if (values!=null && values.size()>0){
				bean.setNumberOfValues(values.size());
				bean.setLastValueDate( new Date(values.get(values.size()-1).getTimestamp()));
			}else{
				bean.setNumberOfValues(0);
			}
			ret.add(bean);
		}		
		return ret;
	}
	
	@RequestMapping(value="/stage/accumulators/get.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public AccumulatorDefinitionObject getAccumulatorDefinition (
			@RequestParam(value="accumulator", required=true ) String accumulatorId,	
			NativeWebRequest request) throws Exception 	
	{	
		Accumulator a = AccumulatorRepository.getInstance().getById(accumulatorId);
		AccumulatorDefinitionObject bean = new AccumulatorDefinitionObject();
		bean.setName(a.getName());
		bean.setPath(a.getDefinition().describe());
		bean.setId(a.getId());
		List<AccumulatedValue> values = a.getValues();
		if (values != null && values.size() > 0) {
			bean.setNumberOfValues(values.size());
			bean.setLastValueDate(new Date(values.get(values.size() - 1).getTimestamp()));
		} else {
			bean.setNumberOfValues(0);
			//bean.setLastValueDate(new Date(0L));
		}
		return bean;
	}	

	@RequestMapping(value="/stage/accumulators/graph.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public AccumulatedSingleGraphObject getAccumulatorGraphData(
			@RequestParam(value="accumulator", required=true ) String accumulatorId,
			NativeWebRequest request) throws Exception 	
	 {
		Accumulator accumulator = AccumulatorRepository.getInstance().getById(accumulatorId);
		AccumulatedSingleGraphObject singleGraphDataBean = new AccumulatedSingleGraphObject(accumulator.getName());
		singleGraphDataBean.setData(new AccumulatorObject(accumulator).getValues());
		return singleGraphDataBean;
	}

	@RequestMapping(value="/stage/accumulators/graph_data_only.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public List<AccumulatedValueObject> getAccumulatorGraphDataOnly(
			@RequestParam(value="accumulator", required=true ) String accumulatorId,
			NativeWebRequest request) throws Exception 	
	 {
		Accumulator accumulator = AccumulatorRepository.getInstance().getById(accumulatorId);
		List<AccumulatedValueObject> values = new LinkedList<AccumulatedValueObject>();
		for (AccumulatedValue v : accumulator.getValues()){
			AccumulatedValueObject ao = new AccumulatedValueObject(new Date(v.getTimestamp()));
			ao.setName(accumulator.getName());
			ao.addValue(v.getValue());
			values.add(ao);
		}
		
		return values;
	}
	
	@RequestMapping(value="/stage/producers/list.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public List<ProducerObject> getProducers(
		@RequestParam(value="class", defaultValue="", required=false ) String className,	
		@RequestParam(value="interval", defaultValue="1m", required=false ) String intervalName,	
		@RequestParam(value="unit", defaultValue="SECONDS", required=false ) String unit,
		@RequestParam(value="createFirstStats", defaultValue="true", required=false ) boolean createFirstStats,	
		@RequestParam(value="createAllStats", defaultValue="false", required=false ) boolean createAllStats,	
		@RequestParam(value="category", defaultValue="", required=false ) String currentCategory,	
		@RequestParam(value="subsystem", defaultValue="", required=false ) String currentSubsystem,	
		NativeWebRequest request) throws NotFoundException 	
	{	
		List<IStatsProducer> producers = producerRegistryAPI.getAllProducers();		
		List<ProducerObject> list = new ArrayList<ProducerObject>(producers.size());
		for (IStatsProducer<?> producer : producers ){
			if( StringUtils.isNotEmpty(currentCategory) && !StringUtils.equals(producer.getCategory(), currentCategory)){
				continue;
			}			
			if( StringUtils.isNotEmpty(currentSubsystem) && !StringUtils.equals(producer.getSubsystem(), currentSubsystem)){
				continue;
			}	
			if( StringUtils.isNotEmpty(className) && !StringUtils.equals(producer.getClass().getSimpleName(), className)){
				continue;
			}				
			list.add(convertStatsProducerToPO( producer, intervalName, net.anotheria.moskito.core.stats.TimeUnit.fromString(unit), createFirstStats, createAllStats));
		}
		return list;
	}

	@RequestMapping(value="/stage/producers/get.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public ProducerObject getProducer(
		@RequestParam(value="producerId", defaultValue="", required=true ) String producerId,		
		@RequestParam(value="interval", defaultValue="1m", required=false ) String intervalName,	
		@RequestParam(value="unit", defaultValue="SECONDS", required=false ) String unit,
		@RequestParam(value="createFirstStats", defaultValue="false", required=false ) boolean createFirstStats,	
		@RequestParam(value="createAllStats", defaultValue="true", required=false ) boolean createAllStats,	
		NativeWebRequest request) throws NotFoundException 	
	{			
		IStatsProducer producer = producerRegistryAPI.getProducer(producerId); // getIProducerRegistry().getProducer(producerId);
		return convertStatsProducerToPO(producer, intervalName, net.anotheria.moskito.core.stats.TimeUnit.fromString(unit), createFirstStats, createAllStats);
	}
	

	@RequestMapping(value="/stage/runtime/stats.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public ProducerObject getRuntimeStats(
			@RequestParam(value="interval", defaultValue="1m", required=false ) String intervalName,
			@RequestParam(value="unit", defaultValue="SECONDS", required=false ) String unit,
			NativeWebRequest request) throws NotFoundException {		
		IStatsProducer<RuntimeStats> producer = ProducerRegistryFactory.getProducerRegistryInstance().getProducer("Runtime");		
		return convertStatsProducerToPO(producer, intervalName, net.anotheria.moskito.core.stats.TimeUnit.fromString(unit), true, false);
	}
	
	@RequestMapping(value="/stage/os/stats.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public ProducerObject getOSStats(
			@RequestParam(value="interval", defaultValue="1m", required=false ) String intervalName,
			@RequestParam(value="unit", defaultValue="SECONDS", required=false ) String unit,
			NativeWebRequest request) throws NotFoundException {		
		IStatsProducer<OSStats> producer = ProducerRegistryFactory.getProducerRegistryInstance().getProducer("OS");		
		return convertStatsProducerToPO(producer, intervalName, net.anotheria.moskito.core.stats.TimeUnit.fromString(unit), true, false);
	}
	
	@RequestMapping(value="/stage/memory/stats.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public List<ProducerObject> getBuiltInMemoryStats(
			@RequestParam(value="class", defaultValue="BuiltInMemoryProducer", required=false ) final String className,
			@RequestParam(value="interval", defaultValue="1m", required=false ) String intervalName,
			@RequestParam(value="unit", defaultValue="SECONDS", required=false ) String unit,
			NativeWebRequest request) throws NotFoundException {
		 
		List<IStatsProducer> producers = producerRegistryAPI.getProducers(new IProducerFilter(){
			public boolean doesFit(IStatsProducer producer) {
				if(StringUtils.equals(producer.getClass().getSimpleName(), className ))
					return true;
				else
				return false;
		}});
		List<ProducerObject> list = new ArrayList<ProducerObject>(producers.size());
		for (IStatsProducer<?> producer : producers ){	
			list.add(convertStatsProducerToPO( producer, intervalName, net.anotheria.moskito.core.stats.TimeUnit.fromString(unit), true, false));
		}
		return list;
	}
	
	@RequestMapping(value="/stage/session/stats.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public List<ProducerObject> getSessionStats(
			@RequestParam(value="interval", defaultValue="1m", required=false ) String intervalName,
			@RequestParam(value="unit", defaultValue="SECONDS", required=false ) String unit,
			NativeWebRequest request) throws NotFoundException {				
		
		List<IStatsProducer> producers = producerRegistryAPI.getProducers(new IProducerFilter(){
  
			public boolean doesFit(IStatsProducer producer) {
				if(StringUtils.equals(producer.getProducerId(), "SessionCount") || StringUtils.equals(producer.getProducerId(), "SessionCountByTld") )
					return true;
				else
				return false;
			}});
		List<ProducerObject> list = new ArrayList<ProducerObject>(producers.size());
		for (IStatsProducer producer : producers ){	
			list.add(convertStatsProducerToPO( producer, intervalName, net.anotheria.moskito.core.stats.TimeUnit.fromString(unit), true, false));
		}
		return list;
	}
	
	protected ProducerObject convertStatsProducerToPO(IStatsProducer<? extends IStats> p, String intervalName, net.anotheria.moskito.core.stats.TimeUnit timeUnit, boolean createFirstStats, boolean createAllStats){
		ProducerObject ao = new ProducerObject();
		ao.setProducerId(p.getProducerId());
		ao.setCategory(p.getCategory());
		ao.setSubsystem(p.getSubsystem());
		ao.setProducerClassName(p.getClass().getSimpleName());
		ao.setFullProducerClassName(p.getClass().getName());
		
		if(createFirstStats){
			IStats firstStats = p.getStats().get(0);
			ao.setFirstStatsValues( getStatValues(firstStats, intervalName, timeUnit) );
		}
		if(createAllStats){
			List<? extends IStats> allStats = p.getStats();
			for (IStats statObject : allStats){
				StatLineObject line = new StatLineObject();
				line.setStatName(statObject.getName());
				line.setValues(getStatValues(statObject, intervalName, timeUnit));
				ao.addStatLine(line);
			}			
		}
		return ao;
	} 
	
	
	protected List<StatValue> getStatValues(IStats stat, String intervalName, net.anotheria.moskito.core.stats.TimeUnit timeUnit){		
		
		List<StatValue> row = new ArrayList<StatValue>(stat.getAvailableValueNames().size());

		log.debug("STAT(interval=" + intervalName + ", timeunit=" + timeUnit.name() + ") --- " + stat.toStatsString(intervalName, timeUnit) );
		
		if(stat instanceof IMemoryPoolStats ){
			for( String name : stat.getAvailableValueNames()){	
				row.add( getLongStatValue(stat, name, intervalName, timeUnit ) );
			}
		}else if (stat instanceof OSStats ){
			for( String name : stat.getAvailableValueNames()){					
				row.add( getLongStatValue(stat, name, intervalName, timeUnit ) );
			}
		}else if (stat instanceof SessionCountStats ){
			for( String name : stat.getAvailableValueNames()){					
				row.add( getLongStatValue(stat, name, intervalName, timeUnit ) );
			}			
		}else if (stat instanceof RequestOrientedStats ){
			for( String name : stat.getAvailableValueNames()){	
				row.add( getLongStatValue(stat, name, intervalName, timeUnit ) );
			}
		}else if (stat instanceof RuntimeStats ){
			for( String name : stat.getAvailableValueNames()){	
				if(StringUtils.equals(name, "Starttime")){
					long value = Long.parseLong(stat.getValueByNameAsString(name, intervalName, timeUnit));	
					row.add( new architecture.ee.web.monitoring.moskito.LongValue( name, value) );
					row.add( new architecture.ee.web.monitoring.moskito.DateValue( name, new Date( value )) );
				}else if (StringUtils.equals(name, "Uptime") ){				
					long value = Long.parseLong(stat.getValueByNameAsString(name, intervalName, timeUnit));
					row.add( new architecture.ee.web.monitoring.moskito.LongValue( name, value ) );
					row.add( new architecture.ee.web.monitoring.moskito.LongValue( "Uphours", TimeUnit.MILLISECONDS.toHours(value) ) );
					row.add( new architecture.ee.web.monitoring.moskito.LongValue( "Updays", TimeUnit.MILLISECONDS.toDays(value) ) );
					
				}else{
					row.add( new architecture.ee.web.monitoring.moskito.StringValue( name, stat.getValueByNameAsString(name, intervalName, timeUnit)) );
				}
			}			
		}else{
			for( String name : stat.getAvailableValueNames()){	
				row.add( new architecture.ee.web.monitoring.moskito.StringValue( name, stat.getValueByNameAsString(name, intervalName, timeUnit)) );
			}
		}
		return row;
	}

	
	protected architecture.ee.web.monitoring.moskito.LongValue getLongStatValue( IStats stat, String name, String intervalName, net.anotheria.moskito.core.stats.TimeUnit timeUnit ){
		long value = 0L;	
		try {
			String stringValue = stat.getValueByNameAsString(name, intervalName, timeUnit);
			if( StringUtils.isNotEmpty(stringValue) &&  !StringUtils.isAlpha(stringValue))	
				value = Long.parseLong(stringValue);
		} catch (Exception e) {}		
		return new architecture.ee.web.monitoring.moskito.LongValue( name, value );
	}
	
	protected IProducerRegistry getIProducerRegistry(){
		return ProducerRegistryFactory.getProducerRegistryInstance();
	}
	
}
