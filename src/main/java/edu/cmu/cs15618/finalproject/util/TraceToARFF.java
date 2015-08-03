package edu.cmu.cs15618.finalproject.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class TraceToARFF {
	public static void main(String[] args) throws IOException {
		String traceFile = "data/NASA_access_log_Aug95";
		String arffFile = traceFile + ".arff";
		BufferedReader br = new BufferedReader(new FileReader(traceFile));
		BufferedWriter bw = new BufferedWriter(new FileWriter(arffFile));

		bw.write("@relation trace\n");
//		bw.write("@attribute tracedate date dd:HH:mm\n");
		 bw.write("@attribute day numeric\n");
		 bw.write("@attribute hour numeric\n");
		 bw.write("@attribute minute numeric\n");
		// bw.write("@attribute size numeric\n");
		bw.write("@attribute tracenum numeric\n");
		bw.write("@data\n");

		String line;

		int traceCounter = 0;
		int currentHour = 0;
		int currentMin = 0;
		int currentDay = 0;
		String date = "";
		while ((line = br.readLine()) != null) {
			String[] data = line.split(" ");
			// String time = data[1];

			String time = data[3];
			time = time.substring(1, time.length());
			int bytes;
			try {
				bytes = Integer.parseInt(data[data.length - 1]);
			} catch (NumberFormatException e) {
				// e.printStackTrace();
				bytes = 0;
			}

			// time = time.substring(1, time.length() - 1);

			String[] tmp = time.split("/");

			int tmpDay = Integer.parseInt(tmp[0]);
			if (tmp.length < 3) {
				continue;
			}
			String[] tmp2 = tmp[2].split(":");
			int tmpHour = Integer.parseInt(tmp2[1]);
			int tmpMin = Integer.parseInt(tmp2[2]);
			int tmpSec = Integer.parseInt(tmp2[3]);
			if (tmpMin + tmpHour * 60 + tmpDay * 24 * 60 >= currentHour * 60
					+ currentMin + 10 + currentDay * 24 * 60) {
				// bw.write(currentHour + "," + currentMin + "," + bytes + ","
//				// + traceCounter + "\n");
				bw.write(tmpDay + "," + tmpHour + "," + tmpMin + ","
						+ traceCounter + "\n");
//				bw.write(tmpDay + ":" + tmpHour + ":" + tmpMin + ","
//						+ traceCounter + "\n");
				// bw.write(date + "," + traceCounter + "\n");
				traceCounter = 0;
				currentHour = tmpHour;
				currentMin = tmpMin;
				currentDay = tmpDay;
				date = time;
			} else {
				traceCounter++;
			}
		}
		br.close();
		bw.close();

	}
}
