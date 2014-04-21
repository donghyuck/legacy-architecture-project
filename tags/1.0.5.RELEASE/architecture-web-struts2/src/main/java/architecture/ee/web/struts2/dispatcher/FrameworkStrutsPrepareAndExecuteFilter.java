package architecture.ee.web.struts2.dispatcher;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.dispatcher.Dispatcher;
import org.apache.struts2.dispatcher.ng.filter.StrutsPrepareAndExecuteFilter;

import architecture.common.event.api.EventListener;
import architecture.common.lifecycle.event.ApplicationPropertyChangeEvent;
import architecture.ee.util.ApplicationHelper;
import architecture.ee.web.util.WebApplicationHelper;

public class FrameworkStrutsPrepareAndExecuteFilter extends StrutsPrepareAndExecuteFilter {

	private static final Log log = LogFactory.getLog(FrameworkStrutsPrepareAndExecuteFilter.class);
	
	private AtomicBoolean initialized;
		
	private Dispatcher dispatcher;
	
	public FrameworkStrutsPrepareAndExecuteFilter() {
		dispatcher = null;
		initialized = new AtomicBoolean(false);
		
		ApplicationHelper.getEventPublisher().register(this);
	
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		super.init(filterConfig);
	}
	
	
	@Override
	protected void postInit(Dispatcher dispatcher, FilterConfig filterConfig) {
		if( ApplicationHelper.isSetupComplete() ){
			this.dispatcher = dispatcher;
			doPostInit();
		}
	}
	
	private synchronized void doPostInit(){
        if(!initialized.get())
        {
        	String encoding = ApplicationHelper.getCharacterEncoding();
        	dispatcher.setDefaultEncoding(encoding);
        	
        	File tempdir = ApplicationHelper.getRepository().getFile("temp");
        	if(!tempdir.exists())
                tempdir.mkdir();
        	
        	try {
				dispatcher.setMultipartSaveDir(tempdir.getCanonicalPath());
			} catch (IOException e) {
				log.error(e.getMessage(), e);
			}        	
			
			setMultiPartMaxSize();
			
			initialized.set(true);
        }
	}
	
	private void setMultiPartMaxSize(){				
        int maxNumber = WebApplicationHelper.getApplicationIntProperty(WebApplicationHelper.ATTACHMENTS_MAX_ATTACHMENTS_PER_MESSAGE, 5);
        int maxSize = WebApplicationHelper.getApplicationIntProperty(WebApplicationHelper.ATTACHMENTS_MAX_ATTACHMENT_SIZE, 1024);        
        long maxAllowedSize = maxNumber * maxSize * 1024 + 10240;
        long limit1 = (long)((double)maxAllowedSize * 1.5D);
        long limit2 = maxAllowedSize + 0xf00000L;
        maxAllowedSize = limit1 <= limit2 ? limit2 : limit1;        
    }


	@EventListener
	public void onEvent(ApplicationPropertyChangeEvent event) {
		log.debug("property changed " + event.getOldValue() + ">" + event.getNewValue() );
	}

}
