package tests;

import org.junit.Test;

import architecture.ext.sync.impl.DefaultDataSyncMetaInfo;

public class SyncTest {

	@Test
	public void testProcessType() {

		System.out.println(
			DefaultDataSyncMetaInfo.Type.valueOf("READ")
		);

	}
}
