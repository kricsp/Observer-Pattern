package loadbalancer.subject;

import java.util.List;

import loadbalancer.observer.ObserverI;


public interface SubjectI {
	
	public String addHost(String hostname);
	public String removeHost(String hostname);
	public String addService(String serviceName, String url, List<String> hostNames);
	public String removeService(String serviceNameIn);
	public String addServiceInstance(String serviceName, String hostName);
	public String removeServiceInstance(String serviceNameIn, String hostName);
	//methods to register and unregister observers
	public void register(ObserverI obj);
	public void deRegister(ObserverI obj);
		
	//method to notify observers of change
	public void notifyObservers();
}
