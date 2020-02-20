package loadbalancer.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import loadbalancer.observer.LoadBalancer;
import loadbalancer.subject.Cluster;
import loadbalancer.subject.Cluster.OperationType;

import java.io.BufferedReader;
import java.io.EOFException;

public class FileProcessor {
	
	Cluster cluster=null;
	LoadBalancer loadBalancer=null;
	
	public FileProcessor(Cluster clusterIn, LoadBalancer loadBalancerIn){
		cluster=clusterIn;
		loadBalancer=loadBalancerIn;
	}
	
	public void readFile(String fileName) {
		File file = new File(fileName);
		FileReader fr;
		try {
			if(file.length()==0) {
				throw new EOFException();
			}
			fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line=null;
			while((line = br.readLine()) != null){
				String output=null;
				String[] instructions=line.split(" ");
				String command=instructions[0];
				
				switch(command){
					case "SERVICE_OP__ADD_SERVICE" : 
					{	
						if(instructions.length==4) {
							String[] hosts=instructions[3].split(",");
							List<String> hostNames= new ArrayList<String>();
							for(int iterator=0;iterator<hosts.length;iterator++) {
								hostNames.add(hosts[iterator]);
							}
							cluster.setOperationType(OperationType.SERVICE_OP__ADD_SERVICE);
							output=cluster.addService(instructions[1], instructions[2], hostNames);
						}
					}
					break;
					
					case "SERVICE_OP__REMOVE_SERVICE" : 
						output=cluster.removeService(instructions[1]);
						break;
						
					case "SERVICE_OP__ADD_INSTANCE" :
						output=cluster.addServiceInstance(instructions[1], instructions[2]);
						break;
						
					case "SERVICE_OP__REMOVE_INSTANCE" :
						output=cluster.removeServiceInstance(instructions[1], instructions[2]);
						break;
						
					case "CLUSTER_OP__SCALE_UP" :
						output=cluster.addHost(instructions[1]);
						break;
						
					case "CLUSTER_OP__SCALE_DOWN" :
						output=cluster.removeHost(instructions[1]);
						break;
						
					case "REQUEST" :
						output=loadBalancer.processRequest(instructions[1]);
						break;
					default : output="Error"; 
				}
				
				cluster.getOutput().add(output);
			}
			br.close();
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (EOFException eof) {
			System.err.println("Error: The file is empty.");
			eof.printStackTrace();
			System.exit(0);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}		
	}	
}
