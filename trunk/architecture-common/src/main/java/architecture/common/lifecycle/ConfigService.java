package architecture.common.lifecycle;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public interface ConfigService extends Component{

	public Repository getWorkspace();
	
	public ConfigRoot getConfigRoot();
	
	public String getEffectiveRootPath();
	
	/**
	 * 로케일을 리턴한다.	
	 * @return
	 */
	public Locale getLocale();
	
	/**
	 * 로케일을 지정한다.
	 * @param newLocale
	 */
	public void setLocale(Locale newLocale);
	
	/**
	 * 인코딩을 리턴한다.
	 * @return
	 */
	public String getCharacterEncoding();
	
	/**
	 * 인코딩을 설정한다.
	 * @param characterEncoding
	 * @throws UnsupportedEncodingException
	 */
	public void setCharacterEncoding(String characterEncoding) throws UnsupportedEncodingException ;
		
	/**
	 * 시간대를 리턴한다.
	 * @return
	 */
	public TimeZone getTimeZone();
	
	/**
	 * 시간대를 설정한다.
	 * @param newTimeZone
	 */
	public void setTimeZone(TimeZone newTimeZone);
	
	/**
	 * 날짜를 yyyy.MM.dd 형식으로 리턴한다.
	 * @param date
	 * @return
	 */
	public String formatDate(Date date);
	
	/**
	 * 시간을 24 시간 형식인 HH:MM:ss 형식으로 리턴한다.
	 * @param date
	 * @return
	 */
	public String formatDateTime(Date date);
	
	
	
	public String getLocalProperty(String name);

	public int getLocalProperty(String name, int defaultValue);

	public String getLocalProperty(String name, String defaultValue);
	
	public boolean getLocalProperty(String name, boolean defaultValue);
	
	public List<String> getLocalProperties(String parent);

	public void setLocalProperty(String name, String value);

	public void setLocalProperties(Map<String, String> map);

	public void deleteLocalProperty(String name);

	
	public String getApplicationProperty(String name);

	public String getApplicationProperty(String name, String defaultValue);

	public List<String> getApplicationPropertyNames();

	public List<String> getApplicationPropertyNames(String parent);

	public List<String> getApplicationProperties(String parent);

	public int getApplicationIntProperty(String name, int defaultValue);

	public boolean getApplicationBooleanProperty(String name);

	public boolean getApplicationBooleanProperty(String name, boolean defaultValue);

	public void setApplicationProperty(String name, String defaultValue);

	public void setApplicationProperties(Map<String, String> map);

	public void deleteApplicationProperty(String name);
	
	public String getLocalizedApplicationProperty(String name, Locale locale);

	public List<Locale> getLocalizedApplicationPropertyLocales(String name);

	public void setLocalizedApplicationProperty(String name, String value, Locale locale);

	public void deleteLocalizedApplicationProperty(String name, Locale locale);
	
}
