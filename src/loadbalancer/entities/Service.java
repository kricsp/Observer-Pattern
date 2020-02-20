package loadbalancer.entities;

public class Service {
	// Service URL.
	private String URL;
	// Service name.
	private String name;
	
	// Rest of the code.
	public String getURL() {
		return URL;
	}
	public void setURL(String uRL) {
		URL = uRL;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}	
}
