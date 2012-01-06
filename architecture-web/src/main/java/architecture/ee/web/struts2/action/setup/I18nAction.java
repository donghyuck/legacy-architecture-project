package architecture.ee.web.struts2.action.setup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import architecture.ee.i18n.I18nLocalizer;
import architecture.ee.i18n.I18nTextManager;
import architecture.ee.util.OutputFormat;
import architecture.ee.web.struts2.action.FrameworkActionSupport;

import com.opensymphony.xwork2.Preparable;

public class I18nAction extends FrameworkActionSupport implements Preparable {
	
	public static final String VIEW_LOCALIZERS = "localizers";
	

	private I18nTextManager i18nTextManager;
	
	public I18nTextManager getI18nTextManager() {
		return i18nTextManager;
	}

	public void setI18nTextManager(I18nTextManager i18nTextManager) {
		this.i18nTextManager = i18nTextManager;
	}

	public void prepare() throws Exception {
		models.clear();
	}
	
	public List<I18nLocalizer> getI18nLocalizers(){
		return i18nTextManager.getI18nLocalizers();
	}
	
	
    public String execute()
    {
    	models.clear();
    	if(VIEW_LOCALIZERS.equals( getView() )){
    		if( OutputFormat.stingToOutputFormat( getDataType() ) == OutputFormat.JSON ){ 	
    			
    			List<I18nLocalizer> localizers = getI18nLocalizers();
    			List<Map> list = new ArrayList<Map>(localizers.size());    			
    			for(I18nLocalizer localizer : localizers){
    				Map<String, Object> row = new HashMap<String, Object>();
    				row.put("ID", localizer.getLocalizerId());
    				row.put("name", localizer.getName());
    				row.put("description", localizer.getDescription());
    				row.put("locale", localizer.getI18nLocale().toJavaLocale().toString());
    				row.put("texts", localizer.getI18nTexts());
    				list.add(row);
    			}    			
    			models.put("localizers", list );    			
    			return JSON;
    		}else if ( OutputFormat.stingToOutputFormat( getDataType() ) == OutputFormat.XML ){     
    			models.put("localizers", getI18nLocalizers());
    			return XML;
    		}
    	}    	
        return SUCCESS;
    }

}
