package edu.cmu.cs15618.finalproject.monitor;

import java.lang.management.ManagementFactory;

import com.sun.management.OperatingSystemMXBean;

import edu.cmu.cs15618.finalproject.datatype.WorkerStatus;

public class WorkerUsageMonitor implements WorkerMonitor {

	private OperatingSystemMXBean osBean;
	private Runtime runtime;

	public WorkerUsageMonitor() {
		osBean = ManagementFactory
				.getPlatformMXBean(OperatingSystemMXBean.class);
		runtime = Runtime.getRuntime();
	}

	@Override
	public WorkerStatus getWorkerStatus() {
		double jvmCPUPerc = osBean.getProcessCpuLoad();
		long jvmFreeMem = runtime.freeMemory();
		long jvmTotalMem = runtime.totalMemory();
		WorkerStatus currentStatus = new WorkerStatus(jvmFreeMem, jvmCPUPerc);
		currentStatus.setTotalMem(jvmTotalMem);
		return currentStatus;
	}
}
