package com.soulan.example.rmi.object;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class HelloImpl extends UnicastRemoteObject implements Hello {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public HelloImpl() throws RemoteException {
	}

	 public String sayHello() {
		 return "Hello World!";
	 }
	 
	 public Long getDelay(Long requestTime) {
		 System.out.println(requestTime);
		 return System.currentTimeMillis()-requestTime;
	 }
}
