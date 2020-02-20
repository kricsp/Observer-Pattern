package loadbalancer.entities;

import java.util.List;

public interface ServiceOpData {
	String getServiceName();
	String getURL();
	List<String> getHostnames();
	void setServiceName(String serviceName);
	void setURL(String uRL);
	void setHostnames(List<String> hostnames);
}
