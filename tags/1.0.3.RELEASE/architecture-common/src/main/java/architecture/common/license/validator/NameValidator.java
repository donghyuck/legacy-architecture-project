package architecture.common.license.validator;

import architecture.common.exception.LicenseException;
import architecture.common.license.License;
import architecture.common.util.L10NUtils;

public class NameValidator implements Validator {
	private final String name;

	public NameValidator(String name) {
		this.name = name;
	}

	public void validate(License license) throws LicenseException {		
		if (!name.equals(license.getName()))
			throw new LicenseException(L10NUtils.format("002102", name, license.getName()));
		else
			return;
	}
}
