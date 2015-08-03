package edu.cmu.cs15618.finalproject.master;

import edu.cmu.cs15618.finalproject.datatype.MachineInfo;
import edu.cmu.cs15618.finalproject.datatype.RequestMessage;
import edu.cmu.cs15618.finalproject.datatype.ResponseMessage;
import edu.cmu.cs15618.finalproject.datatype.ServerAddress;
import edu.cmu.cs15618.finalproject.harness.Client;
import edu.cmu.cs15618.finalproject.worker.Worker;

public interface Master extends Runnable, MachineInfo {

	boolean bootWorker(ServerAddress daemonAddress);

	boolean killWorker(ServerAddress daemonAddress);

	void handleWorkerOnline(ServerAddress workerAddress);

	void handleClientRequest(Client client, RequestMessage request);

	void handleWorkerResponse(Worker worker, ResponseMessage response);

}
