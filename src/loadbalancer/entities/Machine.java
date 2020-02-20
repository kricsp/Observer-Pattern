package loadbalancer.entities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Machine {
	private String hostname;
	// Service name to hosted services.
	private Map<String, String> hostedServicesNames=new HashMap<String, String>();
	private Map<String, Service> hostedServices= new HashMap<String, Service>();
	
	// Rest of the code.
	public String getHostname() {
		return hostname;
	}
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	public Map<String, Service> getHostedServices() {
		return hostedServices;
	}
	public Map<String, String> getHostedServicesNames() {
		return hostedServicesNames;
	}		
}
