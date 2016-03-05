package com.soulan.example.rmi.ssl;

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

public class CustomSSLContext {

	public static SSLContext getSSLContext(String caPath, String keyPath, String certPath) 
			throws KeyStoreException, CertificateException, IOException, NoSuchAlgorithmException, 
				InvalidKeySpecException, UnrecoverableKeyException, KeyManagementException {
		// set up key manager to do server authentication
    	SSLContext ctx;
        KeyManagerFactory kmf;
        KeyStore ks,ts;

        ks = KeyStore.getInstance("JKS");
        ts = KeyStore.getInstance("JKS");
        
        //System.out.println(new String(key,"UTF-8"));
        //key = Base64.getDecoder().decode(key);

        Certificate root = EasyRSAHelper.getCertificateFromFile(caPath)[0];
        Certificate myCert = EasyRSAHelper.getCertificateFromFile(certPath)[0];
        
        ts.load(null,null);
        ks.load(null,null);
        
        Certificate[] chain = {myCert,root};
        ks.setKeyEntry("0", EasyRSAHelper.getPrivateKeyFromFile(keyPath), "".toCharArray(), chain);
        
        KeyStore.Entry newEntry = new KeyStore.TrustedCertificateEntry(root);
//        System.out.println(((KeyStore.TrustedCertificateEntry)newEntry).getTrustedCertificate());
        //ks.setCertificateEntry("0", ((KeyStore.TrustedCertificateEntry)newEntry).getTrustedCertificate());
        ts.setCertificateEntry("0", ((KeyStore.TrustedCertificateEntry)newEntry).getTrustedCertificate());
        //ts.setEntry("0", newEntry, null);
     
        kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, "".toCharArray());

        ctx = SSLContext.getInstance("TLS");

        TrustManagerFactory trustManagerFactory = null;
        trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(ts);
        ctx.init(kmf.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);
        
        return ctx;
	}

}
