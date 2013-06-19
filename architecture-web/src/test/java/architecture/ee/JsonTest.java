package architecture.ee;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonTest {

	@Test
	public void jsonToJava(){
		String json = "{\"items\":[{\"TABLE_NAME\":\"\"},{\"TABLE_NAME\":\"PLUGIN_DATA\"},{\"TABLE_NAME\":\"I18N_LOCALE\"},{\"TABLE_NAME\":\"I18N_TEXT\"},{\"TABLE_NAME\":\"I18N_COUNTRY\"},{\"TABLE_NAME\":\"V2_MENU\"},{\"TABLE_NAME\":\"V2_MENU_ITEM\"},{\"TABLE_NAME\":\"V2_MENU_ITEM_PROPERTY\"},{\"TABLE_NAME\":\"V2_I18N_OBJECT_TEXT\"},{\"TABLE_NAME\":\"I18N_REGION\"},{\"TABLE_NAME\":\"GLOBAL_LOCALIZED_PROPERTY\"},{\"TABLE_NAME\":\"GLOBAL_PROPERTY\"},{\"TABLE_NAME\":\"V2_USERS\"},{\"TABLE_NAME\":\"V2_I18N_LOCALIZER\"},{\"TABLE_NAME\":\"V2_I18N_TEXT\"},{\"TABLE_NAME\":\"REGION\"},{\"TABLE_NAME\":\"QRTZ_JOB_DETAILS\"},{\"TABLE_NAME\":\"QRTZ_TRIGGERS\"},{\"TABLE_NAME\":\"QRTZ_SIMPLE_TRIGGERS\"},{\"TABLE_NAME\":\"QRTZ_CRON_TRIGGERS\"},{\"TABLE_NAME\":\"V2_USER_PROPERTY\"},{\"TABLE_NAME\":\"V2_LOCALIZED_PROPERTY\"},{\"TABLE_NAME\":\"V2_PROPERTY\"},{\"TABLE_NAME\":\"V2_I18N_LOCALE\"},{\"TABLE_NAME\":\"V2_ZIPCODE\"},{\"TABLE_NAME\":\"V2_I18N_COUNTRY\"},{\"TABLE_NAME\":\"V2_I18N_REGION\"},{\"TABLE_NAME\":\"V2_PLUGIN_DATA\"},{\"TABLE_NAME\":\"V2_PROFILE_FIELD\"},{\"TABLE_NAME\":\"V2_PROFILE_FIELD_OPT\"},{\"TABLE_NAME\":\"V2_USER_PROFILE\"},{\"TABLE_NAME\":\"V2_GROUPS\"},{\"TABLE_NAME\":\"V2_GROUP_MEMBERS\"},{\"TABLE_NAME\":\"V2_GROUP_PROPERTY\"},{\"TABLE_NAME\":\"V2_ROLES\"},{\"TABLE_NAME\":\"V2_GROUP_ROLES\"},{\"TABLE_NAME\":\"V2_USER_ROLES\"},{\"TABLE_NAME\":\"V2_PERMISSION_MASK\"},{\"TABLE_NAME\":\"QRTZ_SIMPROP_TRIGGERS\"},{\"TABLE_NAME\":\"QRTZ_BLOB_TRIGGERS\"},{\"TABLE_NAME\":\"QRTZ_CALENDARS\"},{\"TABLE_NAME\":\"QRTZ_PAUSED_TRIGGER_GRPS\"},{\"TABLE_NAME\":\"QRTZ_FIRED_TRIGGERS\"},{\"TABLE_NAME\":\"QRTZ_SCHEDULER_STATE\"},{\"TABLE_NAME\":\"QRTZ_LOCKS\"},{\"TABLE_NAME\":\"V2_THEME_MAP\"},{\"TABLE_NAME\":\"V2_SEQUENCER\"},{\"TABLE_NAME\":\"V2_ZIPCODE_STREET\"}]}";
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			Map<String, List<Map<String, String>>> map = mapper.readValue(json, Map.class);
			List lit = (List) map.get("items") ;
			
			System.out.println( map.get("items") );
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
