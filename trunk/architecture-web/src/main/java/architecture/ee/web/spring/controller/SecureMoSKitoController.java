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
import java.util.List;

import net.anotheria.moskito.core.predefined.OSStats;
import net.anotheria.moskito.core.producers.IStatsProducer;
import net.anotheria.moskito.core.registry.ProducerRegistryFactory;
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

@Controller ("secure-moskito-data-controller")
@RequestMapping("/secure/data")
public class SecureMoSKitoController {

	private static final Log log = LogFactory.getLog(SecureMoSKitoController.class);
	
	public SecureMoSKitoController() {
		// TODO 자동 생성된 생성자 스텁
	}

	public static class StatValue {
		
		private String name;
		
		private String value;
		
		/**
		 * @param name
		 * @param value
		 */
		public StatValue(String name, String value) {
			super();
			this.name = name;
			this.value = value;
		}
		/**
		 * @return name
		 */
		public String getName() {
			return name;
		}
		/**
		 * @param name 설정할 name
		 */
		public void setName(String name) {
			this.name = name;
		}
		/**
		 * @return value
		 */
		public String getValue() {
			return value;
		}
		/**
		 * @param value 설정할 value
		 */
		public void setValue(String value) {
			this.value = value;
		}		
	}	

	
	@RequestMapping(value="/stage/os/stats.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public List<StatValue> getOSStats(
			@RequestParam(value="interval", defaultValue="1m", required=false ) String intervalName,
			NativeWebRequest request) throws NotFoundException {		
		IStatsProducer<OSStats> producer = ProducerRegistryFactory.getProducerRegistryInstance().getProducer("OS");		
		OSStats stats = producer.getStats().get(0);
		List<StatValue> list = new ArrayList<StatValue>(stats.getAvailableValueNames().size());
		for( String name : stats.getAvailableValueNames()){
			list.add( new StatValue( name, stats.getValueByNameAsString(name, intervalName, null) ) );
		}
		return list;
	}

	
	@RequestMapping(value="/stage/os/stats.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public List<List<StatValue>> getSessionCountByTld(
			@RequestParam(value="interval", defaultValue="1m", required=false ) String intervalName,
			NativeWebRequest request) throws NotFoundException {		
		IStatsProducer<SessionCountStats> producer = ProducerRegistryFactory.getProducerRegistryInstance().getProducer("SessionCountByTld");		
		
		List<List<StatValue>> l = new ArrayList(producer.getStats().size());
		for( SessionCountStats stats : producer.getStats()){
			List<StatValue> list = new ArrayList<StatValue>(stats.getAvailableValueNames().size());
			for( String name : stats.getAvailableValueNames()){
				list.add( new StatValue( name, stats.getValueByNameAsString(name, intervalName, null) ) );
			}
		}
		return l;
	}
}
