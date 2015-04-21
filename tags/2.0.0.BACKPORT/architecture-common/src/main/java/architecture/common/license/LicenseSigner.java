/*
 * Copyright 2012 Donghyuck, Son
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
import architecture.common.util.L10NUtils;

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
                log.fatal(L10NUtils.format("002113"));
            init(new FileReader(keyFile));
        }
        catch(Exception e)
        {
            log.fatal(L10NUtils.format("002114"), e);
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
            log.fatal(L10NUtils.format("002114"), e);
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