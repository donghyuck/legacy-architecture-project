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
import java.util.Collection;
import java.util.List;

import net.anotheria.moskito.core.predefined.OSStats;
import net.anotheria.moskito.core.producers.IStats;
import net.anotheria.moskito.core.producers.IStatsProducer;
import net.anotheria.moskito.core.registry.IProducerRegistry;
import net.anotheria.moskito.core.registry.ProducerReference;
import net.anotheria.moskito.core.registry.ProducerRegistryFactory;
import net.anotheria.moskito.core.stats.TimeUnit;
import net.anotheria.moskito.web.session.SessionCountStats;

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
	
	public SecureMoSKitoController() {
		// TODO 자동 생성된 생성자 스텁
	}


	@RequestMapping(value="/stage/producers/list.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public List<ProducerObject> getProducers(
		@RequestParam(value="interval", defaultValue="1m", required=false ) String intervalName,	
		NativeWebRequest request) throws NotFoundException 	
	{	
		Collection<ProducerReference> allProducers = getIProducerRegistry().getProducerReferences() ;
		List<ProducerObject> list = new ArrayList<ProducerObject>(allProducers.size());
		for (ProducerReference ref : getIProducerRegistry().getProducerReferences() ){
			IStatsProducer producer  = ref.get();		
			log.debug(producer.getProducerId());
			log.debug(producer.getClass().getName());
			list.add(convertStatsProducerToPO( producer, intervalName, null, true, false));
		}
		return list;
	}
	
	
	@RequestMapping(value="/stage/os/stats.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public ProducerObject getOSStats(
			@RequestParam(value="interval", defaultValue="1m", required=false ) String intervalName,
			NativeWebRequest request) throws NotFoundException {		
		IStatsProducer<OSStats> producer = ProducerRegistryFactory.getProducerRegistryInstance().getProducer("OS");		
		return convertStatsProducerToPO(producer, intervalName, null, true, false);
	}

	
	@RequestMapping(value="/stage/session/stats.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public ProducerObject getSessionCountByTld(
			@RequestParam(value="interval", defaultValue="1m", required=false ) String intervalName,
			NativeWebRequest request) throws NotFoundException {				
		IStatsProducer<SessionCountStats> producer = getIProducerRegistry().getProducer("SessionCountByTld");
		return convertStatsProducerToPO(producer, intervalName, null, false, true);
	}
	
	protected ProducerObject convertStatsProducerToPO(IStatsProducer<? extends IStats> p, String intervalName, TimeUnit timeUnit, boolean createFirstStats, boolean createAllStats){
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
	
	protected List<StatValue> getStatValues(IStats stat, String intervalName, TimeUnit timeUnit){
		
		log.debug("============================");
		log.debug("stat:" + stat );
		log.debug("stat value length :" + stat.getAvailableValueNames().size() );
		
		List<StatValue> row = new ArrayList<StatValue>(stat.getAvailableValueNames().size());
		for( String name : stat.getAvailableValueNames()){			
			log.debug("stat value: name=" +name + ", interval="+ intervalName + ", timeUnit="+ timeUnit );
			try {
				row.add( new architecture.ee.web.monitoring.moskito.StringValue( name, stat.getValueByNameAsString(name, intervalName, timeUnit) ) );
			} catch (Exception e) {
				row.add( new architecture.ee.web.monitoring.moskito.StringValue( name, "" ) );
			}
		}	
		log.debug("============================");
		return row;
	}
	
	protected IProducerRegistry getIProducerRegistry(){
		return ProducerRegistryFactory.getProducerRegistryInstance();
	}
	
}
