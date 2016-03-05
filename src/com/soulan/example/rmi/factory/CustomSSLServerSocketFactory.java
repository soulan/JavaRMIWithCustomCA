package com.soulan.example.rmi.factory;

import java.io.*;
import java.net.*;
import java.rmi.server.*;
import javax.net.ssl.*;

import com.soulan.example.rmi.ssl.CustomSSLContext;

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
            ssf = CustomSSLContext.getSSLContext(caPath, keyPath, certPath).getServerSocketFactory();
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