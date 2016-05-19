package com.josh;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import burp.IContextMenuInvocation;
import burp.IHttpRequestResponse;

public class ActionJackson implements ActionListener{
	private IContextMenuInvocation inv;
	private ysoserialFrame ysoframe;
	private String opt;
	public ActionJackson(IContextMenuInvocation inv, ysoserialFrame ysoframe, String opt){
		this.inv = inv;
		this.ysoframe = ysoframe;
		this.opt = opt;
	}

	public void actionPerformed(ActionEvent e) {
		try {
			updateIfCMD();
			replace(ysoframe.getPayloadText(this.opt));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}
	private boolean isRequest(){
		if(inv.getInvocationContext() == inv.CONTEXT_MESSAGE_EDITOR_REQUEST || inv.getInvocationContext() == inv.CONTEXT_MESSAGE_VIEWER_REQUEST)
			return true;
		else 
			return false;
		
	}
	private String getMessage(IHttpRequestResponse o){
		return (new String(isRequest()? o.getRequest(): o.getResponse()));
	}
	private byte[] getMsgBytes(IHttpRequestResponse o){
		return isRequest()? o.getRequest(): o.getResponse();
	}
	private void setMessage(IHttpRequestResponse o, String update){
		if(isRequest()){
			o.setRequest(update.getBytes());
			
		}else{
			o.setResponse(update.getBytes());
		}
	}
	private void setMsgBytes(IHttpRequestResponse o, byte [] update){
		if(isRequest()){
			o.setRequest(update);
			
		}else{
			o.setResponse(update);
		}
	}
	
	
	
	/*private void replace-bk(Object replace){
		if(replace == null){
			return;
		}
			
		System.out.println(replace.getClass().getName());
		if(replace.getClass().getName().equals("java.lang.String")){
			int start = inv.getSelectionBounds()[0];
			int stop = inv.getSelectionBounds()[1];
			for(IHttpRequestResponse o : inv.getSelectedMessages()){
				String all = getMessage(o);
				String Selected = all.substring(start, stop);
				if(Selected.startsWith("$(")){
					String cmd = Selected.substring(2, Selected.length()-1);
					
					String type = cmd.substring(0, cmd.indexOf("|"));
					String oscmd = cmd.substring(cmd.indexOf("|"));
					System.out.println("Running with the following Library: " + type);
					System.out.println("Running with the following Command: " + oscmd);
					// this will update the other getters and setters used later
					this.ysoframe.executeYsoSerial(type, oscmd);	
				}
				String begin = all.substring(0, start);
				String end = all.substring(stop);
				all = begin + replace + end;
				setMessage(o, all);
				break;
			}
		}
		
		
		
	}*/
	private void updateIfCMD(){
		int start = inv.getSelectionBounds()[0];
		int stop = inv.getSelectionBounds()[1];
		for(IHttpRequestResponse o : inv.getSelectedMessages()){
			String all = getMessage(o);
			String Selected = all.substring(start, stop);
			if(Selected.startsWith("$(")){
				String cmd = Selected.substring(2, Selected.length()-1);
				String type = cmd.substring(0, cmd.indexOf("|"));
				String oscmd = cmd.substring(cmd.indexOf("|")+1);
				System.out.println("Running with the following Library: " + type);
				System.out.println("Running with the following Command: " + oscmd);
				if(type.equals("CC1"))
					type = "CommonsCollections1";
				else if(type.equals("CC2"))
					type =  "CommonsCollections2";
				else if(type.equals("GV1"))
					type =  "Groovy1";
				else if (type.equals("SP1"))
					type = "Spring1";
				// this will update the other getters and setters used later
				this.ysoframe.executeYsoSerial(type, oscmd);	
			}
			break;
		}
		
	}
	
	private void replace(Object replace){
		if(replace == null)
			return;
		int start = inv.getSelectionBounds()[0];
		int stop = inv.getSelectionBounds()[1];
		if(replace.getClass().getName().equals("java.lang.String")){
			for(IHttpRequestResponse o : inv.getSelectedMessages()){
				String all = getMessage(o);
				String Selected = all.substring(start, stop);
				String begin = all.substring(0, start);
				String end = all.substring(stop);
				all = begin + replace + end;
				setMessage(o, all);
				break;
			}
		}else{
			for(IHttpRequestResponse o : inv.getSelectedMessages()){
				byte[] all = getMsgBytes(o);
				byte[] begin = Arrays.copyOfRange(all, 0, start);
				byte[] end = Arrays.copyOfRange(all, stop, all.length);
				byte [] r = (byte[])replace;
				byte [] out = new byte[begin.length + end.length + r.length];
				System.arraycopy(begin, 0, out, 0, begin.length);
				System.arraycopy(r, 0, out, begin.length, r.length);
				System.arraycopy(end, 0, out, r.length+begin.length, end.length);
				setMsgBytes(o,out);
				break;
			}
			
		}
	
	}

}
