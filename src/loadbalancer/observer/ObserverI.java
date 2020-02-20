package loadbalancer.observer;

import loadbalancer.entities.Service;
import loadbalancer.entities.ServiceOpData;
import loadbalancer.subject.Cluster;
import loadbalancer.subject.Cluster.OperationType;

public interface ObserverI {
	
	public void update(OperationType operation, ServiceOpData data, ServiceManager serviceManager);
	public void update(OperationType operation, String hostname);
	public void setCluster(Cluster clusterIn);
	public String getType();
	public Service getService();
	public void setService(Service serviceIn);
}
