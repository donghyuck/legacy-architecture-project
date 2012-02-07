package architecture.common.license.validator;

import architecture.common.exception.LicenseException;
import architecture.common.license.License;

public class NameValidator implements Validator {
	private final String name;

	public NameValidator(String name) {
		this.name = name;
	}

	public void validate(License license) throws LicenseException {
		if (!name.equals(license.getName()))
			throw new LicenseException((new StringBuilder())
					.append("License must be for ").append(name)
					.append(" not ").append(license.getName()).toString());
		else
			return;
	}
}
