package architecture.common;

import org.junit.Test;

import architecture.common.util.profiling.Profiler;
import architecture.common.util.profiling.Profiler.ProfileStat;
import architecture.common.util.profiling.impl.ProfilerFactoryImpl;

public class ProfilerTest {

	
	@Test
	public void testProfiler () {
		
		ProfilerFactoryImpl impl = new ProfilerFactoryImpl();
		Profiler profiler = impl.getProfiler();
		profiler.start("JDBC");
		try {
			Thread.currentThread().sleep(1000);
		} catch (InterruptedException e) {
		}
		profiler.stop("JDBC");
		
		profiler.start("JDBC");
		try {
			Thread.currentThread().sleep(2000);
		} catch (InterruptedException e) {
		}
		profiler.stop("JDBC");
		
		for(ProfileStat stat : profiler.getStats().values() )
		System.out.println
		(
				stat.getPercentOfParent()
		);
		
	}
}
