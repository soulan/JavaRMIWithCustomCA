package com.soulan.example.rmi.factory;

import java.io.*;
import java.net.*;
import java.rmi.server.*;
import java.security.KeyStore;
import java.security.cert.Certificate;

import javax.net.ssl.*;

public class CustomSSLClientSocketFactory implements RMIClientSocketFactory, Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*
     * Create one SSLServerSocketFactory, so we can reuse sessions
     * created by previous sessions of this SSLContext.
     */
    private SSLSocketFactory ssf = null;

    public CustomSSLClientSocketFactory(String caPath, String keyPath, String certPath) {
        try {
        	System.out.println("Setting Up Client Socket");
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
//            System.out.println(((KeyStore.TrustedCertificateEntry)newEntry).getTrustedCertificate());
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
            
//            System.out.println(ks.size());
//            System.out.println("THRUSTSTORE "+ts.size());
            
            ssf = ctx.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Socket createSocket(String host, int port) throws IOException {
    	SSLSocket sslSock = (SSLSocket) ssf.createSocket(host,port);
    	return sslSock;
    }
    
    public int hashCode() {
        return getClass().hashCode();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return true;
    }
}
