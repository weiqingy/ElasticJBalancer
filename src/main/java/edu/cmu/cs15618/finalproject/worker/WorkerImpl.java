package edu.cmu.cs15618.finalproject.worker;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.cmu.cs15618.finalproject.config.ServerConfigurations;
import edu.cmu.cs15618.finalproject.datatype.MachineInfo;
import edu.cmu.cs15618.finalproject.datatype.MessageType;
import edu.cmu.cs15618.finalproject.datatype.RequestMessage;
import edu.cmu.cs15618.finalproject.datatype.ResponseMessage;

public class WorkerImpl implements Worker {

	private ServerSocket workerSocket;

	private ExecutorService executor;

	public WorkerImpl() {
		try {
			this.workerSocket = new ServerSocket(
					ServerConfigurations.WORKER_DEFAULT_PORT);
			// executor = Executors
			// .newFixedThreadPool(Runtime.getRuntime().availableProcessors()*2);
			executor = Executors.newCachedThreadPool();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public WorkerImpl(int port) {

		try {
			this.workerSocket = new ServerSocket(port);
			// executor = Executors
			// .newFixedThreadPool(Runtime.getRuntime().availableProcessors()*2);
			executor = Executors.newCachedThreadPool();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while (true) {
			try {
				Socket socket = workerSocket.accept();

				ObjectInputStream in = new ObjectInputStream(
						socket.getInputStream());
				RequestMessage requestMessage = (RequestMessage) in
						.readObject();

				if (requestMessage.getMessageType() == MessageType.WORK) {
					this.processRequest(socket, requestMessage);
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private class WorkerThread implements Runnable {
		private Socket socket;
		private RequestMessage requestMessage;

		public WorkerThread(Socket pSocket, RequestMessage pRequestMessage) {
			this.socket = pSocket;
			this.requestMessage = pRequestMessage;
		}

		@Override
		public void run() {
			try {
				// Thread.sleep(10);

				int responseSize = Integer.parseInt(requestMessage.getContent()
						.split(" ")[0]);

				StringBuilder sb = new StringBuilder();

				for (int i = 0; i < responseSize; i++) {
					sb.append(i);
				}

				int[] allocateMem = new int[10 * 1024 * 1024];

				Thread.sleep(1000);
				ObjectOutputStream out = new ObjectOutputStream(
						socket.getOutputStream());
				out.writeObject(new ResponseMessage(MessageType.ACTION_SUCCESS,
						"success"));
				
				out.close();
				socket.close();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	@Override
	public void processRequest(Socket socket, RequestMessage request) {
		executor.execute(new WorkerThread(socket, request));
	}

	@Override
	public String getIP() {
		return workerSocket.getInetAddress().getHostAddress();
	}

	@Override
	public int getPort() {
		return workerSocket.getLocalPort();
	}

}
