package architecture.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import architecture.common.util.L10NUtils;

public class L10NTest {

	Log log = LogFactory.getLog(getClass());
	
	@Test
	public void testLoad(){
		log.debug(
				L10NUtils.format("002103", "a", "b", "c")
		);
		log.debug(
				L10NUtils.format("002099")
		);		
	}
}
