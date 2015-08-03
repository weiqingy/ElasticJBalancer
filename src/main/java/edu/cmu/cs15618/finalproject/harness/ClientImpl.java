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

public class ClientImpl implements Client {

	private String masterIP;
	private int masterPort;
	private Socket socket;
	private long timer;

	private ExecutorService mService;

	private String tracePath;

	public ClientImpl(String path) {
		timer = 0;
		tracePath = path;
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

	public ClientImpl(String pMasterIP, int pMasterPort) {
		this.masterIP = pMasterIP;
		this.masterPort = pMasterPort;
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
				objOut.close();
				objIn.close();
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

		new Timer().schedule(new TimerTask() {

			@Override
			public void run() {
				timer++;
			}
		}, 0, 1000);

		try {
			BufferedReader br = new BufferedReader(new FileReader(tracePath));

			String line;

			while ((line = br.readLine()) != null) {
				String[] data = line.split(" ");
				String time = data[3];
				time = time.substring(1, time.length());
				int bytes;
				try {
					bytes = Integer.parseInt(data[data.length - 1]);
				} catch (NumberFormatException e) {
					// e.printStackTrace();
					bytes = 0;
				}

				String[] tmp = time.split("/");

				int tmpDay = Integer.parseInt(tmp[0]);
				if (tmp.length < 3) {
					continue;
				}
				String[] tmp2 = tmp[2].split(":");
				int tmpHour = Integer.parseInt(tmp2[1]);
				int tmpMin = Integer.parseInt(tmp2[2]);
				int tmpSec = Integer.parseInt(tmp2[3]);

				int currentMinute = (tmpDay - 1) * 24 * 60 + tmpHour * 60
						+ tmpMin;

				while (currentMinute > timer) {
					Thread.sleep(5);
				}
				System.out.println("request sent, size:" + bytes);
				mService.execute(new SendRequest(line, bytes));
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}