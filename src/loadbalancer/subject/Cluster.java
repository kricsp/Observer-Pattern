package loadbalancer.subject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import loadbalancer.entities.Data;
import loadbalancer.entities.Machine;
import loadbalancer.entities.Service;
import loadbalancer.entities.ServiceOpData;
import loadbalancer.observer.ObserverI;
import loadbalancer.observer.ServiceManager;

public class Cluster implements SubjectI{
	// Hostnames to corresponding machine instances.
	private Map<String, Machine> machines = null;
	// Status of a service.
	private Map<String, String> serviceStatuses = new HashMap<String, String>();

	private String hostnameRemoved;

	public String getHostnameRemoved() {
		return hostnameRemoved;
	}
	public void setHostnameRemoved(String hostnameRemoved) {
		this.hostnameRemoved = hostnameRemoved;
	}

	//List of observers.
	private List<ObserverI> observers=new ArrayList<ObserverI>();

	private OperationType operationType;
	private ServiceOpData serviceOpData;

	public ServiceOpData getServiceOpData() {
		return serviceOpData;
	}
	public void setServiceOpData(ServiceOpData serviceOpData) {
		this.serviceOpData = serviceOpData;
	}

	public enum OperationType{
		CLUSTER_OP__SCALE_UP, CLUSTER_OP__SCALE_DOWN, SERVICE_OP__ADD_SERVICE, SERVICE_OP__REMOVE_SERVICE, SERVICE_OP__ADD_INSTANCE, SERVICE_OP__REMOVE_INSTANCE, REQUEST
	}

	public OperationType getOperationType() {
		return operationType;
	}
	public void setOperationType(OperationType operationType) {
		this.operationType = operationType;
	}

	// Rest of the code.
	private List<String> output=new ArrayList<String>();

	public List<String> getOutput() {
		return output;
	}

	public Map<String, String> getServiceStatuses() {
		return serviceStatuses;
	}

	@Override
	public void register(ObserverI obj) {
		obj.setCluster(this);
		observers.add(obj);
	}

	@Override
	public void deRegister(ObserverI obj) {
		obj.setService(null);
		observers.remove(obj);
	}

	public void notifyObservers() {
		for (ObserverI obs : observers) {
			if(getOperationType().toString().equals("SERVICE_OP__ADD_SERVICE")||getOperationType().toString().equals("SERVICE_OP__REMOVE_SERVICE")) {
				if(obs.getType().equals("LoadBalancer")) {
					obs.update(getOperationType(), getServiceOpData(), (ServiceManager)observers.get(observers.size()-1));
				}
			} else if(getOperationType().toString().equals("CLUSTER_OP__SCALE_DOWN")) {
				if(obs.getType().equals("ServiceManager"))
					obs.update(getOperationType(), getHostnameRemoved());
			}else if(getOperationType().toString().equals("SERVICE_OP__ADD_INSTANCE")) {
				if(obs.getType().equals("ServiceManager"))
					obs.update(getOperationType(), getServiceOpData(),null);
			}else if(getOperationType().toString().equals("SERVICE_OP__REMOVE_INSTANCE")) {
				if(obs.getType().equals("ServiceManager"))
					obs.update(getOperationType(), getServiceOpData(),null);
			}
		}
	}


	public String addHost(String hostname) {
		if(machines!=null) {
			if(machines.containsKey(hostname))
				return "Hostname already exists.";
			else {
				Machine machine = new Machine();
				machine.setHostname(hostname);
				machines.put(hostname, machine);
				return "Cluster Scaled Up.";
			}
		}else {
			Machine machine = new Machine();
			machine.setHostname(hostname);
			machines=new HashMap<String, Machine>();
			machines.put(hostname, machine);
			return "Cluster Scaled Up.";
		}
	}	

	public String removeHost(String hostname) {
		if(!machines.containsKey(hostname))
			return "Hostname doesn't exists.";
		else {
			machines.remove(hostname);
			setHostnameRemoved(hostname);
			notifyObservers();
			return "Cluster Scaled Down.";
		}
	}

	public String addService(String serviceName, String url, List<String> hostNames) {
		Boolean flag=false;
		ObserverI serviceObserver=null;
		for(String hostName: hostNames) {
			Machine machine = machines.get(hostName);
			if (machine != null) {
				if(!machine.getHostedServices().containsKey(serviceName)) {
					serviceObserver=new ServiceManager(url, serviceName, hostName);
					register(serviceObserver);
					Service service=new Service();
					service.setName(serviceName);
					service.setURL(url);
					machine.getHostedServices().put(serviceName, service);
					machine.getHostedServicesNames().put(serviceName, url);
					flag=true;					
					
				}else
					return "A service with the given name already exists on the cluster.";
			}
		}
		if(!flag)
			return "A machine does not exist for any of the given hostnames.";
		else {
			setOperationType(OperationType.SERVICE_OP__ADD_SERVICE);
			serviceOpData = new Data();
			serviceOpData.setServiceName(serviceName);
			serviceOpData.setHostnames(hostNames);
			serviceOpData.setURL(url);
			setServiceOpData(serviceOpData);
			notifyObservers();
			return "Service Added.";
		}

	}

	public String removeService(String serviceNameIn) {

		Boolean flag=false;

		for(String machineName: machines.keySet()) {
			Machine machine= machines.get(machineName);
			if (machine != null) {
				if(machine.getHostedServicesNames().containsKey(serviceNameIn)) {
					for(int iterator=0;iterator<observers.size();iterator++) {
						if(observers.get(iterator).getService()!=null) {
							if(observers.get(iterator).getType().equals("ServiceManager") 
									&& observers.get(iterator).getService().getName().equals(serviceNameIn)) {
								deRegister(observers.get(iterator));
							}
						}
					}					
					machine.getHostedServicesNames().remove(serviceNameIn);
					machine.getHostedServices().remove(serviceNameIn);
					flag=true;
				}	
			}
		}

		if(!flag)
			return "The service name is invalid.";
		else {
			setOperationType(OperationType.SERVICE_OP__REMOVE_SERVICE);
			serviceOpData = new Data();
			serviceOpData.setServiceName(serviceNameIn);
			setServiceOpData(serviceOpData);
			notifyObservers();
			return "Service Removed.";
		}
	}

	public String addServiceInstance(String serviceNameIn, String hostName) {

		Service service = null;
		if(machines.containsKey(hostName)) {
			Machine machine= machines.get(hostName);
			if(machine.getHostedServicesNames().containsKey(serviceNameIn)) {
				if(machine.getHostedServices().containsKey(serviceNameIn))
					return "An instance of the service already exists on the machine.";
				else {
					service = new Service();
					service.setName(serviceNameIn);
					service.setURL(machine.getHostedServicesNames().get(serviceNameIn));
					machine.getHostedServices().put(serviceNameIn, service);
					setOperationType(OperationType.SERVICE_OP__ADD_INSTANCE);
					serviceOpData = new Data();
					serviceOpData.setServiceName(serviceNameIn);
					List<String> hostNames=new ArrayList<String>();
					hostNames.add(hostName);
					serviceOpData.setHostnames(hostNames);
					serviceOpData.setURL(machine.getHostedServicesNames().get(serviceNameIn));
					setServiceOpData(serviceOpData);
					notifyObservers();
				}
			}else
				return "The service has not been previously added to the cluster using SERVICE_OP__ADD_SERVICE operation.";
		}

		return "Instance Added.";
	}

	public String removeServiceInstance(String serviceNameIn, String hostName) {

		int count=0;
		Machine machine = machines.get(hostName);
		if(machine.getHostedServices().containsKey(serviceNameIn)) {
			machine.getHostedServices().remove(serviceNameIn);
			setOperationType(OperationType.SERVICE_OP__REMOVE_INSTANCE);
			serviceOpData = new Data();
			serviceOpData.setServiceName(serviceNameIn);
			List<String> hostNames=new ArrayList<String>();
			hostNames.add(hostName);
			serviceOpData.setHostnames(hostNames);
			serviceOpData.setURL(machine.getHostedServicesNames().get(serviceNameIn));
			setServiceOpData(serviceOpData);
			notifyObservers();

			for(String machineName: machines.keySet()) {
				Machine machineSelected= machines.get(machineName);
				if(machineSelected.getHostedServices().containsKey(serviceNameIn))
					count++;
			}
			if(count<1)
				getServiceStatuses().put(serviceNameIn, "inactive");

		}else
			return "No instance of service is present on the machine.";

		return "Instance Removed";
	}

}
