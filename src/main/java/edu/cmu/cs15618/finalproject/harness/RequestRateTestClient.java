package edu.cmu.cs15618.finalproject.harness;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.cmu.cs15618.finalproject.datatype.MessageType;
import edu.cmu.cs15618.finalproject.datatype.RequestMessage;
import edu.cmu.cs15618.finalproject.datatype.ResponseMessage;

public class RequestRateTestClient implements Client {

	private String masterIP;
	private int masterPort;
	private Socket socket;
	private long timer;

	private ExecutorService mService;

	private int requestRate;

	public RequestRateTestClient(int requestRate) {
		this.requestRate = requestRate;
		mService = Executors.newCachedThreadPool();
		try {
			BufferedReader br = new BufferedReader(new FileReader(
					"master_addresses"));
			String line = br.readLine();
			String masterInfo[] = line.split(" ");
			masterIP = masterInfo[0];
			masterPort = Integer.parseInt(masterInfo[1]);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private class SendRequest implements Runnable {

		private String request;
		private int requestBytes;

		public SendRequest(String request, int bytes) {
			this.request = request;
			this.requestBytes = bytes;
		}

		@Override
		public void run() {

			ObjectInputStream objIn = null;
			ObjectOutputStream objOut = null;
			Socket tmpsocket;
			try {
				tmpsocket = new Socket(masterIP, masterPort);
				objOut = new ObjectOutputStream(tmpsocket.getOutputStream());

				objOut.writeObject(new RequestMessage(MessageType.WORK,
						requestBytes + " " + timer));

				// System.out.println(requestBytes + " " + timer);

				objIn = new ObjectInputStream(tmpsocket.getInputStream());
				ResponseMessage response = (ResponseMessage) objIn.readObject();
				if (response.getMessageType() == MessageType.ACTION_SUCCESS) {
					System.out.println("Get Response size:"
							+ response.getContent().length());
				}
				tmpsocket.close();
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
	public void run() {

		while (true) {
			mService.execute(new SendRequest("request", 5000));
			try {
				Thread.sleep(1000 / requestRate);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
