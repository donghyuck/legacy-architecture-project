package architecture.common.license;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import architecture.common.exception.LicenseException;

public class LicenseSigner
{
	private static String privateKeyPath = "C:/TOOLS/workspace/architecture_v2/architecture-ee/profile/private.key";
	
    private static final Log log = LogFactory.getLog(LicenseSigner.class);
    private Signature sig;
    
    public LicenseSigner()
    {
        try
        {
            String keyFile = privateKeyPath ; //System.getProperty("jive.private.key");
            if(keyFile == null)
                log.fatal("Property jive.private.key java env property not specified!");
            init(new FileReader(keyFile));
        }
        catch(Exception e)
        {
            log.fatal("Loading private key for license factory failed.", e);
            throw new LicenseException(e);
        }
    }

    public LicenseSigner(Reader privateKey)
    {
        try
        {
            init(privateKey);
        }
        catch(Exception e)
        {
            log.fatal("Loading private key for license factory failed.", e);
            throw new LicenseException(e);
        }
    }

    protected void init(Reader keyReader)
        throws IOException, NoSuchAlgorithmException, DecoderException, InvalidKeySpecException, InvalidKeyException
    {
    	
        BufferedReader in = new BufferedReader(keyReader);
        String privateKey = in.readLine();
        in.close();
        
        
        KeyFactory keyFactory = KeyFactory.getInstance("DSA");
        sig = Signature.getInstance("SHA1withDSA");
        PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(Hex.decodeHex(privateKey.toCharArray()));
        java.security.PrivateKey privKey = keyFactory.generatePrivate(privKeySpec);
        
        sig.initSign(privKey);
    }

    public synchronized void sign(License license)
        throws Exception
    {
        sig.update(license.getFingerprint());
        String signature = new String(Hex.encodeHex(sig.sign()));
        license.setSignature(signature);
    }


}

