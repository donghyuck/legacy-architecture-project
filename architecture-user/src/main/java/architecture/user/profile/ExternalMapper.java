package architecture.user.profile;

import java.util.List;

public interface ExternalMapper {

    public abstract List<String> getObjectFieldMappingKeys();

    public abstract boolean isSingleFieldMapper();

}
