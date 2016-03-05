package de.soulan.example.rmi.ssl;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

/**
 * The custom ssl context creates an ssl context with a specific key store and
 * trust store that is exclusively used for our own CA and Certificates
 * 
 * @author Soulan
 */
public class CustomSSLContext {

	public static SSLContext getSSLContext(String caPath, String keyPath, String certPath) 
			throws KeyStoreException, CertificateException, IOException, NoSuchAlgorithmException, 
				InvalidKeySpecException, UnrecoverableKeyException, KeyManagementException {
		
        KeyStore ks,ts;

        ks = KeyStore.getInstance("JKS"); //this is the keystore of our ssl context
        ts = KeyStore.getInstance("JKS"); //this is our trust store
        
        //load the root CA
        Certificate root = EasyRSAHelper.getCertificateFromFile(caPath)[0];
        
        //load client/server cert
        Certificate myCert = EasyRSAHelper.getCertificateFromFile(certPath)[0];
        
        ts.load(null,null);
        ks.load(null,null);

        //create the chain server/client cert -> root cert
        Certificate[] chain = {myCert,root};
        //Put it into our keystore
        ks.setKeyEntry("0", EasyRSAHelper.getPrivateKeyFromFile(keyPath), "".toCharArray(), chain);
        
        //the root certificate goes into the trust store
        KeyStore.Entry newEntry = new KeyStore.TrustedCertificateEntry(root);
        ts.setCertificateEntry("0", ((KeyStore.TrustedCertificateEntry)newEntry).getTrustedCertificate());
     
        KeyManagerFactory kmf;
        kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, "".toCharArray());

        //create a trust manager that includes only our root ca
        TrustManagerFactory trustManagerFactory = null;
        trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(ts);
        
        //init ssl context and attach our key store and trust store
        SSLContext ctx = SSLContext.getInstance("TLS");
        ctx.init(kmf.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);
        
        return ctx;
	}

}
