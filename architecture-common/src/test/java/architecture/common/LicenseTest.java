package architecture.common;

import java.util.Date;

import org.junit.Test;

import architecture.common.license.License;
import architecture.common.license.License.Type;
import architecture.common.license.LicenseSigner;
import architecture.common.license.validator.CheckSignatureValidator;

public class LicenseTest {

   
	@Test
	public void testPrivateKey() {
         License license = new License("architecture", "enterprise", 1, 0, 0 , "ljh855", "inkium");
         license.setType(Type.COMMERCIAL);
         license.setModule("architecture-web-ui");
         license.setCreationDate(new Date());
         LicenseSigner signer = new LicenseSigner();
         try {
			signer.sign(license);
		} catch (Exception e) {
			e.printStackTrace();
		}
         System.out.println(
        	license.toXML()
         );
         
         CheckSignatureValidator validator = new CheckSignatureValidator();
         validator.validate(license);
	}
	
	

}
