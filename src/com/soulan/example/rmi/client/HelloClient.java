package com.soulan.example.rmi.client;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import com.soulan.example.rmi.factory.CustomSSLClientSocketFactory;
import com.soulan.example.rmi.object.Hello;

public class HelloClient {
	
	public static void main(String args[]) throws Exception {
        // Get reference to the RMI registry running on port 3000 in the local host
        Registry registry = LocateRegistry.getRegistry(null, 3000, new CustomSSLClientSocketFactory(System.getenv("CA_CERT"),System.getenv("CLIENT_KEY"),System.getenv("CLIENT_CERT")));
        // Lookup the remote reference bound to the name "HelloServer"
        Hello obj = (Hello) registry.lookup("HelloServer");
        
        System.out.println(obj.sayHello());
        
        Long received = obj.getDelay(System.currentTimeMillis());
        Long send = System.currentTimeMillis();
        for(int i = 0; i < 10; ++i) {
        	send = System.currentTimeMillis();
        	received = obj.getDelay(send);
        }
        System.out.println(System.currentTimeMillis());
        System.out.println("Received timing "+received);
        System.out.println(send);
    }
}
