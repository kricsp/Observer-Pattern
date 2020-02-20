package loadbalancer.util;

import loadbalancer.subject.Cluster;

public interface FileDisplayInterface {
	public void writeToFile(String pathToFile, Cluster cluster);	
}
