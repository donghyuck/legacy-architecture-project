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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
import architecture.ee.web.monitoring.moskito.ProducerObject;
import architecture.ee.web.monitoring.moskito.StatLineObject;
import architecture.ee.web.monitoring.moskito.StatValue;

@Controller ("secure-moskito-data-controller")
@RequestMapping("/secure/data")
public class SecureMoSKitoController {

	private static final Log log = LogFactory.getLog(SecureMoSKitoController.class);
	private static final IProducerRegistryAPI  producerRegistryAPI = new ProducerRegistryAPIFactory().createProducerRegistryAPI();
	
	public SecureMoSKitoController() {
	}


	@RequestMapping(value="/stage/producers/list.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public List<ProducerObject> getProducers(
		@RequestParam(value="class", defaultValue="", required=false ) String className,	
		@RequestParam(value="interval", defaultValue="1m", required=false ) String intervalName,	
		@RequestParam(value="category", defaultValue="", required=false ) String currentCategory,	
		@RequestParam(value="subsystem", defaultValue="", required=false ) String currentSubsystem,	
		NativeWebRequest request) throws NotFoundException 	
	{	
		
		//Collection<ProducerReference> allProducers = getIProducerRegistry().getProducerReferences() ;
		List<IStatsProducer> producers = producerRegistryAPI.getAllProducers();		
		List<ProducerObject> list = new ArrayList<ProducerObject>(producers.size());
		for (IStatsProducer producer : producers ){
			if( StringUtils.isNotEmpty(currentCategory) && !StringUtils.equals(producer.getCategory(), currentCategory)){
				continue;
			}			
			if( StringUtils.isNotEmpty(currentSubsystem) && !StringUtils.equals(producer.getSubsystem(), currentSubsystem)){
				continue;
			}	
			if( StringUtils.isNotEmpty(className) && !StringUtils.equals(producer.getClass().getSimpleName(), className)){
				continue;
			}				
			list.add(convertStatsProducerToPO( producer, intervalName, null, true, false));
		}
		return list;
	}

	@RequestMapping(value="/stage/producers/get.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public ProducerObject getProducer(
		@RequestParam(value="producerId", defaultValue="", required=true ) String producerId,		
		@RequestParam(value="interval", defaultValue="1m", required=false ) String intervalName,	
		NativeWebRequest request) throws NotFoundException 	
	{	
		
		IStatsProducer producer = producerRegistryAPI.getProducer(producerId); // getIProducerRegistry().getProducer(producerId);
		return convertStatsProducerToPO(producer, intervalName, null, false, true);
	}
	

	@RequestMapping(value="/stage/runtime/stats.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public ProducerObject getRuntimeStats(
			@RequestParam(value="interval", defaultValue="1m", required=false ) String intervalName,
			NativeWebRequest request) throws NotFoundException {		
		IStatsProducer<RuntimeStats> producer = ProducerRegistryFactory.getProducerRegistryInstance().getProducer("Runtime");		
		return convertStatsProducerToPO(producer, intervalName, null, true, false);
	}
	
	@RequestMapping(value="/stage/os/stats.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public ProducerObject getOSStats(
			@RequestParam(value="interval", defaultValue="1m", required=false ) String intervalName,
			NativeWebRequest request) throws NotFoundException {		
		IStatsProducer<OSStats> producer = ProducerRegistryFactory.getProducerRegistryInstance().getProducer("OS");		
		return convertStatsProducerToPO(producer, intervalName, null, true, false);
	}
	
	@RequestMapping(value="/stage/memory/stats.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public List<ProducerObject> getBuiltInMemoryStats(
			@RequestParam(value="class", defaultValue="BuiltInMemoryProducer", required=false ) final String className,
			@RequestParam(value="interval", defaultValue="1m", required=false ) String intervalName,
			NativeWebRequest request) throws NotFoundException {
		 
		List<IStatsProducer> producers = producerRegistryAPI.getProducers(new IProducerFilter(){
			public boolean doesFit(IStatsProducer producer) {
				if(StringUtils.equals(producer.getClass().getSimpleName(), className ))
					return true;
				else
				return false;
		}});
		List<ProducerObject> list = new ArrayList<ProducerObject>(producers.size());
		for (IStatsProducer producer : producers ){	
			list.add(convertStatsProducerToPO( producer, intervalName, null, true, false));
		}
		return list;
	}
	
	@RequestMapping(value="/stage/session/stats.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public List<ProducerObject> getSessionStats(
			@RequestParam(value="interval", defaultValue="1m", required=false ) String intervalName,
			NativeWebRequest request) throws NotFoundException {				
		
		List<IStatsProducer> producers = producerRegistryAPI.getProducers(new IProducerFilter(){
			@Override
			public boolean doesFit(IStatsProducer producer) {
				if(StringUtils.equals(producer.getProducerId(), "SessionCount") || StringUtils.equals(producer.getProducerId(), "SessionCountByTld") )
					return true;
				else
				return false;
			}});
		List<ProducerObject> list = new ArrayList<ProducerObject>(producers.size());
		for (IStatsProducer producer : producers ){	
			list.add(convertStatsProducerToPO( producer, intervalName, null, true, false));
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
		if(stat instanceof IMemoryPoolStats ){
			for( String name : stat.getAvailableValueNames()){	
				log.debug("stat class memory" );
				row.add( getLongStatValue(stat, name, intervalName, timeUnit ) );
			}
		}else if (stat instanceof OSStats ){
			log.debug("stat class os" );
			for( String name : stat.getAvailableValueNames()){					
				row.add( getLongStatValue(stat, name, intervalName, timeUnit ) );
			}
		}else if (stat instanceof RequestOrientedStats ){
			log.debug("stat class request" );
			for( String name : stat.getAvailableValueNames()){	
				row.add( getLongStatValue(stat, name, intervalName, timeUnit ) );
			}
		}else if (stat instanceof RuntimeStats ){
			log.debug("stat class runtime" );
			for( String name : stat.getAvailableValueNames()){	
				if(StringUtils.equals(name, "Starttime")){
					long value = Long.parseLong(stat.getValueByNameAsString(name, intervalName, timeUnit));	
					row.add( new architecture.ee.web.monitoring.moskito.LongValue( name, value) );
					row.add( new architecture.ee.web.monitoring.moskito.DateValue( name, new Date( value * 1000)) );
				}else if (StringUtils.equals(name, "Uptime") ){				
					long value = Long.parseLong(stat.getValueByNameAsString(name, intervalName, timeUnit));
					row.add( new architecture.ee.web.monitoring.moskito.LongValue( name, value ) );
					row.add( new architecture.ee.web.monitoring.moskito.LongValue( "Uphours", TimeUnit.SECONDS.toHours(value) ) );
					row.add( new architecture.ee.web.monitoring.moskito.LongValue( "Updays", TimeUnit.SECONDS.toDays(value) ) );
					
				}else{
					row.add( new architecture.ee.web.monitoring.moskito.StringValue( name, stat.getValueByNameAsString(name, intervalName, timeUnit)) );
				}
			}			
		}else{
			log.debug("stat class etc" );
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
