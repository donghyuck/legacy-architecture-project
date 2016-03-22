package architecture.common.license.validator;

import architecture.common.exception.LicenseException;
import architecture.common.license.License;

public interface Validator {
    public abstract void validate(License license) throws LicenseException;
}
