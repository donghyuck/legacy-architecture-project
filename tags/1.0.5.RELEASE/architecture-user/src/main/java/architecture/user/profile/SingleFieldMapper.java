package architecture.user.profile;

import java.util.Collections;
import java.util.List;


public abstract class SingleFieldMapper implements ExternalMapper {

	public SingleFieldMapper() {
	}

	public List getObjectFieldMappingKeys() {
		return Collections.emptyList();
	}

	public boolean isSingleFieldMapper() {
		return true;
	}
}
