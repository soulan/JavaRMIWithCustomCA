package de.soulan.example.rmi.factory;

import java.io.*;
import java.net.*;
import java.rmi.server.*;

import javax.net.ssl.*;

import de.soulan.example.rmi.ssl.CustomSSLContext;

/**
 * This socket factory uses the {@link de.soulan.example.rmi.ssl.CustomSSLContext CustomSSLContext} to create a regular ssl socket
 * wich is able to use our client certificate
 * 
 * @author Soulan
 */
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
            ssf = CustomSSLContext.getSSLContext(caPath, keyPath, certPath).getSocketFactory();
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
