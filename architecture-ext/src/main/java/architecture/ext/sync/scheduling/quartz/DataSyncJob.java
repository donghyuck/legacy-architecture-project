package architecture.ext.sync.scheduling.quartz;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import org.springframework.scheduling.quartz.QuartzJobBean;

import architecture.ext.sync.client.DataSyncClient;

public class DataSyncJob extends QuartzJobBean {

	private Log log = LogFactory.getLog(DataSyncJob.class);
	
	private String jobCode;
	
	private DataSyncClient dataSyncClient;

	public String getJobCode() {
		return jobCode;
	}

	public void setDataSyncClient(DataSyncClient dataSyncClient) {
		this.dataSyncClient = dataSyncClient;
	}

	public void setJobCode(String jobCode) {
		this.jobCode = jobCode;
	}
	
	protected void executeInternal(JobExecutionContext ctx) throws JobExecutionException {
		try {
			
			if(log.isDebugEnabled())
				log.debug("execute sync job : " + jobCode + " with " +  dataSyncClient.getClass().getName() );
			
			System.out.println("execute sync job : " + jobCode + " with " +  dataSyncClient.getClass().getName());
			dataSyncClient.process(jobCode);
			
		} catch (Throwable e) {
			e.printStackTrace();
			log.error(e);
			
			throw new JobExecutionException(e);
		}
	}
	
}
