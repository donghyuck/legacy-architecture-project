package architecture.ee.user.profile;

import java.util.List;

public interface ExternalMapper {
	
    public abstract List getObjectFieldMappingKeys();

    public abstract boolean isSingleFieldMapper();
    
}
