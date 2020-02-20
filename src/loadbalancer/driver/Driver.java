package loadbalancer.driver;

import loadbalancer.observer.LoadBalancer;
import loadbalancer.observer.ObserverI;
import loadbalancer.subject.Cluster;
import loadbalancer.util.FileProcessor;
import loadbalancer.util.Results;

/**
 * @author Krishan Pal Singh
 *
 */
public class Driver {
	public static void main(String[] args) {

		if (args.length != 2 || args[0].equals("${arg0}") || args[1].equals("${arg1}")) {

			System.err.println("Error: Incorrect number of arguments. Program accepts 2 arguments.");
			System.exit(0);
		}
		
		Cluster cluster = new Cluster();
		ObserverI lbObserver=new LoadBalancer(cluster);
		
		cluster.register(lbObserver);
		FileProcessor fileProcessor = new FileProcessor(cluster, (LoadBalancer)lbObserver);
		fileProcessor.readFile(args[0]);
		
		
		Results results= new Results();
		results.writeToConsole(cluster);
		results.writeToFile(args[1], cluster);
		cluster.deRegister(lbObserver);
	}
}
