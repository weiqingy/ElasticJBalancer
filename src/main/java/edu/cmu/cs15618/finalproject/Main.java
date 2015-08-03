package edu.cmu.cs15618.finalproject;

import edu.cmu.cs15618.finalproject.harness.Client;
import edu.cmu.cs15618.finalproject.harness.ClientImpl;
import edu.cmu.cs15618.finalproject.harness.RequestRateTestClient;
import edu.cmu.cs15618.finalproject.master.Master;
import edu.cmu.cs15618.finalproject.master.MasterImpl;
import edu.cmu.cs15618.finalproject.worker.WorkerDaemon;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		if (args.length < 1) {
			System.out.println("Usage: client/master/worker port");
		}

		String type = args[0];

		if (type.equals("client")) {
			if (args[1].matches("^-?\\d+$")) {
				Client client = new RequestRateTestClient(
						Integer.parseInt(args[1]));
				new Thread(client).start();
			} else {
				Client client = new ClientImpl(args[1]);
				new Thread(client).start();
			}

		} else if (type.equals("master")) {
			int port = Integer.parseInt(args[1]);
			Master master = new MasterImpl(port);
			new Thread(master).start();
		} else if (type.equals("worker")) {
			int port = Integer.parseInt(args[1]);
			WorkerDaemon workerDaemon = new WorkerDaemon(port);
			new Thread(workerDaemon).start();
		}
	}
}
