package com.soulan.example.rmi.factory;

import java.io.*;
import java.net.*;
import java.rmi.server.*;

import javax.net.ssl.*;

import com.soulan.example.rmi.ssl.CustomSSLContext;

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
