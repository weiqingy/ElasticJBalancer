package edu.cmu.cs15618.finalproject.worker;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

import org.hyperic.sigar.cmd.Time;

import edu.cmu.cs15618.finalproject.config.ServerConfigurations;
import edu.cmu.cs15618.finalproject.datatype.MachineInfo;
import edu.cmu.cs15618.finalproject.datatype.MessageType;
import edu.cmu.cs15618.finalproject.datatype.RequestMessage;
import edu.cmu.cs15618.finalproject.datatype.ServerAddress;
import edu.cmu.cs15618.finalproject.datatype.WorkerStatus;
import edu.cmu.cs15618.finalproject.master.Master;
import edu.cmu.cs15618.finalproject.monitor.WorkerMonitor;
import edu.cmu.cs15618.finalproject.monitor.WorkerUsageMonitor;

public class WorkerDaemon implements Runnable, MachineInfo {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3567000012557169504L;
	private ServerSocket daemonSocket;

	private WorkerMonitor workerMonitor;

	private long workerStartTime;

	private Thread workerThread;
	private ServerAddress workerAddress;

	private boolean isWorkerAlive = false;
	private int port;

	public WorkerDaemon() {
		this.port = ServerConfigurations.DAEMON_DEFAULT_PORT;
		workerMonitor = new WorkerUsageMonitor();
		try {
			daemonSocket = new ServerSocket(
					ServerConfigurations.DAEMON_DEFAULT_PORT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public WorkerDaemon(int port) {
		this.port = port;
		workerMonitor = new WorkerUsageMonitor();
		try {
			daemonSocket = new ServerSocket(port);
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public void handleBootWorkerRequest(Socket masterSocket) {
		try {
			OutputStream out = masterSocket.getOutputStream();

			ObjectOutputStream objOut = new ObjectOutputStream(out);

			ServerAddress newWorkerAddress = this.bootWorker();

			objOut.writeObject(newWorkerAddress);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private ServerAddress bootWorker() {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (workerThread == null && isWorkerAlive == false) {
			this.workerStartTime = System.currentTimeMillis();
			WorkerImpl newWorker = new WorkerImpl(this.getPort() + 1);
			workerThread = new Thread(newWorker);
			workerThread.start();
			workerAddress = new ServerAddress(newWorker.getIP(),
					newWorker.getPort());
		}
		isWorkerAlive = true;
		return workerAddress;
	}

	public void handleKillWorkerRequest(Master master) {

	}

	private void killWorker() {
		isWorkerAlive = false;
	}

	public WorkerStatus getCurrentStatus() {
		WorkerStatus result = workerMonitor.getWorkerStatus();
		result.setStartTime(this.workerStartTime);
		result.setAlive(workerThread != null && isWorkerAlive);
		return result;
	}

	@Override
	public void run() {
		new Timer().schedule(new TimerTask() {

			@Override
			public void run() {
				System.out.println(" " + getCurrentStatus().toString());
			}
		}, 0, 1000);
		while (true) {
			try {

				Socket masterRequestSocket = this.daemonSocket.accept();
				ObjectInputStream in = new ObjectInputStream(
						masterRequestSocket.getInputStream());
				RequestMessage requestMessage = (RequestMessage) in
						.readObject();

				MessageType type = requestMessage.getMessageType();
				if (type == MessageType.BOOT_WORKER) {
					this.handleBootWorkerRequest(masterRequestSocket);
					this.workerStartTime = System.currentTimeMillis();
				} else if (type == MessageType.KILL_WORKER) {
					this.killWorker();
				} else if (type == MessageType.GET_STATUS) {
					ObjectOutputStream statusOut = new ObjectOutputStream(
							masterRequestSocket.getOutputStream());
					WorkerStatus currentStatus = this.getCurrentStatus();

					statusOut.writeObject(currentStatus);
					statusOut.close();
				}
				in.close();

				masterRequestSocket.close();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	@Override
	public String getIP() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getPort() {
		return this.port;
	}

}
