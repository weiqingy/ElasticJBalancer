package edu.cmu.cs15618.finalproject.datatype;

import java.io.Serializable;

public interface Message extends Serializable {

	MessageType getMessageType();

	void setMessageType();

	String getContent();

	void setContent();

	MachineInfo getSourceMachineInfo();

	MachineInfo getDestMachineInfo();
}
