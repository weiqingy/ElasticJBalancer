package edu.cmu.cs15618.finalproject.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

public class SendPostRequest {
	public static void sendPost(int time, int requestNum) throws Exception {
		String urlParameters = "name=performance&columns=[\"time\",\"throughput\"]"
				+ "&points=[[" + time + "," + requestNum + "]]";
		byte[] postData = urlParameters.getBytes(Charset.forName("UTF-8"));
		int postDataLength = postData.length;
		String request = "http://52.4.142.123:8086/db/ElasticJBalancerThroughput/series?u=root&p=root";
		URL url = new URL(request);
		HttpURLConnection cox = (HttpURLConnection) url.openConnection();
		cox.setDoOutput(true);
		cox.setDoInput(true);
		cox.setInstanceFollowRedirects(false);
		cox.setRequestMethod("POST");
		cox.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		cox.setRequestProperty("charset", "utf-8");
		cox.setRequestProperty("Content-Length",
				Integer.toString(postDataLength));
		cox.setUseCaches(false);
		try (DataOutputStream wr = new DataOutputStream(cox.getOutputStream())) {
			wr.write(postData);
		}
	}

	public static void sendPost2(int time, int requestNum) throws IOException {

		String url = "http://52.4.142.123:8086/db/collectd/series?u=root&p=root";
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", "");
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

		// String urlParameters =
		// "name=performance&columns=[\"time\",\"throughput\"]"
		// + "&points=[[" + time + "," + requestNum + "]]";
		String urlParameters = "[{\"name\":\"performance\",\"columns\":[\"time\",\"throughput\"],\"points\":[["
				+ time + "," + requestNum + "]]}]";

		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
//		System.out.println("\nSending 'POST' request to URL : " + url);
//		System.out.println("Post parameters : " + urlParameters);
//		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(
				con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		// print result
//		System.out.println(response.toString());
	}
}
