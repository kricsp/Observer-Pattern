package loadbalancer.observer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import loadbalancer.entities.Service;
import loadbalancer.entities.ServiceOpData;
import loadbalancer.subject.Cluster;
import loadbalancer.subject.Cluster.OperationType;

public class ServiceManager implements ObserverI{
	private String key;
	
	private Service service;
	//private static int pointer;
	private int pointer;

	// Information pertaining to the service.
	private String URL;
	private List<String> hostnames=new ArrayList<String>();
	
	// Cluster on which the services are hosted.
	private Cluster cluster;

	// Rest of the code.
	public ServiceManager(String URL, String serviceName, String hostname) {
		this.URL=URL;
	}
	public String getURL() {
		return URL;
	}
	public void setURL(String uRL) {
		URL = uRL;
	}
	public List<String> getHostnames() {
		return hostnames;
	}
	public Service getService() {
		return service;
	}
	public void setService(Service serviceIn) {
		service=serviceIn;
	}
	public Map<String, String> getNextHost(){
		
		Map<String, String> details=new HashMap<String, String>();
		if(pointer<hostnames.size()) {
			details.put(URL, hostnames.get(pointer)); 
			pointer++;
		}else {
			pointer=0;
			details.put(URL, hostnames.get(pointer)); 
			pointer++;
		}
		return details;
	}
	
	public void update(OperationType operation, ServiceOpData data, ServiceManager serviceManager) {
		// rest of the code.
		if(operation.toString().equals("SERVICE_OP__ADD_INSTANCE")) {
			service = new Service();
			service.setURL(URL);
			service.setName(data.getServiceName());
			getHostnames().add(data.getHostnames().get(0));
		} else if(operation.toString().equals("SERVICE_OP__REMOVE_INSTANCE")) {
			service=null;
			getHostnames().remove(data.getHostnames().get(0));
		}
		
	}
	
	public void update(OperationType operation, String hostname) {
		getHostnames().remove(hostname);
	}
	
	public void setCluster(Cluster clusterIn) {
		cluster = clusterIn;
	}
	
	public String getType() {
		return "ServiceManager";
	}
	
}
