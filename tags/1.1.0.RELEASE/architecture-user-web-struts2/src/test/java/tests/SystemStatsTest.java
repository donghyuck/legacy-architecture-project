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
package tests;

import static org.junit.Assert.fail;

import org.junit.Test;

import sun.management.ManagementFactory;

public class SystemStatsTest {

	@Test
	public void test() {
		//fail("아직 구현되지 않음");
		Runtime.getRuntime().freeMemory();
		
		Runtime.getRuntime().maxMemory();
		
		Runtime.getRuntime().totalMemory();
		
		Runtime.getRuntime().availableProcessors();
		
		System.out.println(  ManagementFactory.getMemoryMXBean().getHeapMemoryUsage() );
		
		System.out.println( ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage() );
		
		
	}

}
