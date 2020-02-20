package loadbalancer.entities;

import java.util.List;

public class Data implements ServiceOpData{
	private String serviceName;
	  private String URL;
	  private List<String> hostnames;
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
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
	public void setHostnames(List<String> hostnames) {
		this.hostnames = hostnames;
	}
}
