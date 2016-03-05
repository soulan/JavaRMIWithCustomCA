package com.soulan.example.rmi.server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import com.soulan.example.rmi.factory.CustomSSLClientSocketFactory;
import com.soulan.example.rmi.factory.CustomSSLServerSocketFactory;
import com.soulan.example.rmi.object.HelloImpl;

public class RMIRegistry {
    public static void main(String[] args) throws Exception {
    	Registry registry = LocateRegistry.createRegistry(3000,new CustomSSLClientSocketFactory(System.getenv("CA_CERT"),System.getenv("SERVER_KEY"),System.getenv("SERVER_CERT")), new CustomSSLServerSocketFactory(System.getenv("CA_CERT"),System.getenv("SERVER_KEY"),System.getenv("SERVER_CERT")));
    	
        // Get reference to the RMI registry running on port 3000 in the local host
        //Registry registry = LocateRegistry.getRegistry(null, 3000, new CustomSSLClientSocketFactory("L:\\easyrsa\\pki\\ca.crt","L:\\easyrsa\\pki\\private\\test-object-host.key","L:\\easyrsa\\pki\\issued\\test-object-host.crt"));
    	
        // Bind this object instance to the name "HelloServer"
        HelloImpl obj = new HelloImpl();
        registry.bind("HelloServer", obj);
        System.out.println("HelloServer bound in registry");
    }
}
