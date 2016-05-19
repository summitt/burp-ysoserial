package burp;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;

import com.josh.ActionJackson;
import com.josh.ysoserialFrame;


public class BurpExtender implements IBurpExtender, ITab, IContextMenuFactory
{
	private ysoserialFrame exFrame;
	private IBurpExtenderCallbacks cb;
	private IExtensionHelpers helpers;
	
    public void registerExtenderCallbacks(
         IBurpExtenderCallbacks callbacks)
    {
    	
    	cb = callbacks;
    	helpers = cb.getHelpers();
    	cb.setExtensionName("YSOSERIAL");
    	cb.registerContextMenuFactory(this);
    	SwingUtilities.invokeLater(new Runnable(){

			//@Override
			public void run() {
				exFrame = new ysoserialFrame();
				cb.customizeUiComponent(exFrame);
		    	cb.addSuiteTab(BurpExtender.this);
			}
			
    		
    	});

    	
    }
    
    //@Override
    public String getTabCaption(){
    	return "YSOSERIAL";
    }
	//@Override
	public Component getUiComponent() {
		return exFrame;
	}

	public List<JMenuItem> createMenuItems(IContextMenuInvocation inv) {
		if( inv.getInvocationContext() == inv.CONTEXT_MESSAGE_EDITOR_REQUEST || inv.getInvocationContext() == inv.CONTEXT_MESSAGE_EDITOR_RESPONSE){
			JMenuItem raw = new JMenuItem("Java Serialized Payload (Raw)");
			raw.addActionListener(new ActionJackson(inv, exFrame, "raw"));
			JMenuItem b64 = new JMenuItem("Java Serialized Payload (b64)");
			b64.addActionListener(new ActionJackson(inv, exFrame, "b64"));
			JMenuItem eurl = new JMenuItem("Java Serialized Payload (URLEnc)");
			eurl.addActionListener(new ActionJackson(inv, exFrame, "eurl"));
			List<JMenuItem>stuff = new ArrayList<JMenuItem>();
			stuff.add(raw);
			stuff.add(b64);
			stuff.add(eurl);
			
			return stuff;
		}else{
			return null;
		}
	}
    
}
