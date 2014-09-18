package architecture.common;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.StringReader;
import java.util.Date;
import java.util.UUID;

import org.junit.Test;

import architecture.common.license.License;
import architecture.common.license.License.Type;
import architecture.common.license.LicenseSigner;
import architecture.common.license.io.LicenseReader;
import architecture.common.license.io.LicenseWriter;
import architecture.common.license.validator.CheckSignatureValidator;

public class LicenseTest {

	@Test
	public void testPrivateKeyAndWriteLicense() {
		
		
        
		License license = new License("APPLICATION ARCHITECTURE for JAVA", "STANDARD", 1, 0, 3, "ANY", "ANY");
		license.setID(20130614);
		license.setType(Type.COMMERCIAL);
		license.setModule("architecture-common");
		license.setModule("architecture-ee");
		license.setModule("architecture-user");
		license.setModule("architecture-web");
		license.setCreationDate(new Date());
		license.setClient("www.inkium.com","INKIUM");
		
		
		try {
			
			StringReader reader = new StringReader(
					"3082014b0201003082012c06072a8648ce3804013082011f02818100fd7f53811d75122952df4a9c2eece4e7f611b7523cef4400c31e3f80b6512669455d402251fb593d8d58fabfc5f5ba30f6cb9b556cd7813b801d346ff26660b76b9950a5a49f9fe8047b1022c24fbba9d7feb7c61bf83b57e7c6a8a6150f04fb83f6d3c51ec3023554135a169132f675f3ae2b61d72aeff22203199dd14801c70215009760508f15230bccb292b982a2eb840bf0581cf502818100f7e1a085d69b3ddecbbcab5c36b857b97994afbbfa3aea82f9574c0b3d0782675159578ebad4594fe67107108180b449167123e84c281613b7cf09328cc8a6e13c167a8b547c8d28e0a3ae1e2bb3a675916ea37f0bfa213562f1fb627a01243bcca4f1bea8519089a883dfe15ae59f06928b665e807b552564014c3bfecf492a0416021427031ae01feac12380e9332e5c9016bf2ef1ff6b"
			);
		    LicenseSigner signer = new LicenseSigner( reader );
            //  signer.sign(license);
            
            LicenseWriter writer = new LicenseWriter(license, signer);
            writer.write(new FileWriter(  "C:/TOOLS/workspace/profile/license.xml"  ));
            
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println(license.toXML());

		CheckSignatureValidator validator = new CheckSignatureValidator();
		validator.validate(license);
	}
	
	@Test
	public void testReadLicense(){
		
		LicenseReader reader = new LicenseReader();		
		try {
			License license = reader.read(new FileReader( "C:/TOOLS/workspace/profile/license.xml"  ) );
			System.out.println( license.toXML() ) ;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
