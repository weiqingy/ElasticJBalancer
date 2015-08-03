package edu.cmu.cs15618.finalproject.datatype;

import java.io.Serializable;

public class RequestMessage implements Message {

	private MessageType type;
	private String content;

	public RequestMessage(MessageType pType, String pContent) {
		type = pType;
		content = pContent;
	}

	@Override
	public String getContent() {
		return this.content;
	}

	@Override
	public MachineInfo getSourceMachineInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MachineInfo getDestMachineInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MessageType getMessageType() {
		return this.type;
	}

	@Override
	public void setMessageType() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setContent() {
		// TODO Auto-generated method stub

	}

}
