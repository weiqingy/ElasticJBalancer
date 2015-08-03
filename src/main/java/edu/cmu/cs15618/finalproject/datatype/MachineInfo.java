package edu.cmu.cs15618.finalproject.datatype;

import java.io.Serializable;

public interface MachineInfo extends Serializable {
	
	public String getIP();

	public int getPort();
}
