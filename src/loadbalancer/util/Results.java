package loadbalancer.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import loadbalancer.subject.Cluster;

public class Results implements FileDisplayInterface, StdoutDisplayInterface {

	public void writeToFile(String pathToFile, Cluster cluster) {

		File file=new File(pathToFile);

		FileWriter fileWriter=null;
		try {
			fileWriter=new FileWriter(file);
			Iterator<String> iterator=cluster.getOutput().iterator();
			while (iterator.hasNext()) {
				fileWriter.write(iterator.next());
				fileWriter.write("\n");
			}

		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}}
	}

	public void writeToConsole(Cluster cluster) {
		Iterator<String> iterator=cluster.getOutput().iterator();
		while (iterator.hasNext()) {
			System.out.println(iterator.next());
		}

	}
}
