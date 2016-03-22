package architecture.common.license.validator;

import java.security.KeyFactory;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import architecture.common.exception.LicenseException;
import architecture.common.license.License;

public class CheckSignatureValidator implements Validator {

    private static final Log log = LogFactory.getLog(CheckSignatureValidator.class);

    public CheckSignatureValidator() {
    }

    public void validate(License license) throws LicenseException {
	try {
	    // 서명 알고리즘으로 DSA 알고리즘을 사용한다.
	    String publicKey = "308201b83082012c06072a8648ce3804013082011f02818100fd7f53811d75122952df4a9c2eece4e7f611b7523cef4400c31e3f80b6512669455d402251fb593d8d58fabfc5f5ba30f6cb9b556cd7813b801d346ff26660b76b9950a5a49f9fe8047b1022c24fbba9d7feb7c61bf83b57e7c6a8a6150f04fb83f6d3c51ec3023554135a169132f675f3ae2b61d72aeff22203199dd14801c70215009760508f15230bccb292b982a2eb840bf0581cf502818100f7e1a085d69b3ddecbbcab5c36b857b97994afbbfa3aea82f9574c0b3d0782675159578ebad4594fe67107108180b449167123e84c281613b7cf09328cc8a6e13c167a8b547c8d28e0a3ae1e2bb3a675916ea37f0bfa213562f1fb627a01243bcca4f1bea8519089a883dfe15ae59f06928b665e807b552564014c3bfecf492a0381850002818100faf2d25b2866aa68501094d1097bebc95c6bcf1c58766f18b35fbf5e9d761cc5bf913447e374c21d279777859f9f043d1dc0d58b93a2081b56b4f5269a81b076907a3b11b01ec5cfde5dae4dfd7d26346e53e611235e714e69ec1bc141c77a8a28c4c799df570a4c3240e7f2fee19d6ed4caaa1b15b5da4a967ee82e3eb4d4ca";

	    byte pub[] = Hex.decodeHex(publicKey.toCharArray());
	    X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(pub);

	    KeyFactory keyFactory = KeyFactory.getInstance("DSA");
	    java.security.PublicKey pubKey = keyFactory.generatePublic(pubKeySpec);
	    Signature sig = Signature.getInstance("DSA");

	    sig.initVerify(pubKey);
	    // license.getSignature().getBytes("UTF-8");
	    byte decoded[] = Hex.decodeHex(license.getSignature().toCharArray());

	    log.debug("decoded sig: " + Hex.encodeHexString(decoded));

	    log.info((new StringBuilder()).append("Validating license. License fingerprint: ")
		    .append(license.getSignature()).toString());

	    sig.update(license.getFingerprint());

	    boolean verified = sig.verify(decoded);

	    if (!verified)
		throw new LicenseException("License signature is invalid.");
	} catch (Exception e) {
	    log.fatal(e.getMessage(), e);
	    throw new LicenseException(e);
	}
    }

}
