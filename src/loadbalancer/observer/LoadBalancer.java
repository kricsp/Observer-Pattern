package loadbalancer.observer;

import loadbalancer.entities.Service;
import loadbalancer.entities.ServiceOpData;
import loadbalancer.entities.Trie;
import loadbalancer.subject.Cluster;
import loadbalancer.subject.Cluster.OperationType;

public class LoadBalancer implements ObserverI{
	// Index to find the URL and hostname for a given service name.
	//
	// Trie is optional for under-graduate students.
	// Graduate students have to use a Trie datastructure.
	private Trie serviceURLAndHostnameFetcher = new Trie();
	
	private Cluster cluster=null;
		
	// Rest of the code.
	public LoadBalancer(Cluster clusterIn){
		cluster=clusterIn;
	}
	public String processRequest(String serviceName) {
		if(cluster.getServiceStatuses().containsKey(serviceName)) {
			if(cluster.getServiceStatuses().get(serviceName).equals("inactive"))
				return "Service Inactive - Service::"+serviceName;
			else
				return null;
		}else {
			if(serviceURLAndHostnameFetcher.get(serviceName)==null)
				return "Invalid Service";
			else {
				return "Processed Request - Service_URL::"+serviceURLAndHostnameFetcher.get(serviceName).getURL()+" Host::"+
				serviceURLAndHostnameFetcher.get(serviceName).getNextHost().get(serviceURLAndHostnameFetcher.get(serviceName).getURL());
			}
		}
	}
	
	public void update(OperationType operation, ServiceOpData data, ServiceManager serviceManagerIn) {
		  // rest of the code.
		if(operation.toString().equals("SERVICE_OP__ADD_SERVICE")) {
			for(String name: data.getHostnames()) {
				serviceManagerIn.getHostnames().add(name);
			}
			serviceURLAndHostnameFetcher.put(data.getServiceName(), serviceManagerIn);
		}else if(operation.toString().equals("SERVICE_OP__REMOVE_SERVICE"))
			serviceURLAndHostnameFetcher.delete(data.getServiceName());
	}
	
	public void update(OperationType operation, String hostname) {
		// rest of the code.
		
	}
	
	public void setCluster(Cluster clusterIn) {
		cluster = clusterIn;
	}
	
	public String getType() {
		return "LoadBalancer";
	}
	public Service getService() {
		return null;
	}
	public void setService(Service serviceIn) {
		
	}
	
}
