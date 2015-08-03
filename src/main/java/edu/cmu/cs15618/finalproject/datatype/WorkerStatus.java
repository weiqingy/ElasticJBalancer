package edu.cmu.cs15618.finalproject.datatype;

import java.io.Serializable;

public class WorkerStatus implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6062814602825894440L;
	private long startTime;
	private long totalMem;
	private long freeMem;
	private double cpuPerc;
	private boolean isAlive;

	public WorkerStatus(long pFreeMem, double pCpuPerc) {
		this.freeMem = pFreeMem;
		this.cpuPerc = pCpuPerc;
	}

	public WorkerStatus(long pFreeMem, double pCpuPerc, long startTime) {
		this.freeMem = pFreeMem;
		this.cpuPerc = pCpuPerc;
		this.startTime = startTime;
	}

	public long getFreeMem() {
		return freeMem;
	}

	public void setFreeMem(int freeMem) {
		this.freeMem = freeMem;
	}

	public double getCpuPerc() {
		return cpuPerc;
	}

	public void setCpuPerc(double cpuPerc) {
		this.cpuPerc = cpuPerc;
	}

	public long getUptime() {
		if (this.isAlive) {
			return System.currentTimeMillis() - startTime;
		}
		return 0;
	}

	public void setAlive(boolean status) {
		this.isAlive = status;
	}

	public boolean isAlive() {
		return this.isAlive;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getTotalMem() {
		return totalMem;
	}

	public void setTotalMem(long totalMem) {
		this.totalMem = totalMem;
	}

	@Override
	public String toString() {
		return "CPU Usage: " + this.cpuPerc + " Free Mem: " + freeMem
				+ " Up Time(s): " + this.getUptime() / 1000;
	}

}
