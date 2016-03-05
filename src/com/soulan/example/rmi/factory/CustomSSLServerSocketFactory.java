package com.soulan.example.rmi.factory;

import java.io.*;
import java.net.*;
import java.rmi.server.*;
import javax.net.ssl.*;

import java.security.KeyStore;
import java.security.cert.Certificate;

public class CustomSSLServerSocketFactory implements RMIServerSocketFactory, Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*
     * Create one SSLServerSocketFactory, so we can reuse sessions
     * created by previous sessions of this SSLContext.
     */
    private SSLServerSocketFactory ssf = null;
    
    public CustomSSLServerSocketFactory(String caPath, String keyPath, String certPath) {
        try {
        	System.out.println("Setting Up Server Socket");
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
            
            ssf = ctx.getServerSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ServerSocket createServerSocket(int port) throws IOException {
    	SSLServerSocket sslSock = (SSLServerSocket) ssf.createServerSocket(port);
    	sslSock.setNeedClientAuth(true);
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