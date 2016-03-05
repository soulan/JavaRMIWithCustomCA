package de.soulan.example.rmi.object;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Hello extends Remote {
	public String sayHello() throws RemoteException;
	public Long getDelay(Long requestTime) throws RemoteException;
}
