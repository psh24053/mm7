package cn.server.listener;

import java.io.IOException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import spApi.Bind;
import spApi.Deliver;
import spApi.Report;
import spApi.Unbind;

import cn.panshihao.mm7send.ReceiverSMC;
import cn.panshihao.mm7send.ReceiverSMC.receiverListener;

/**
 * Application Lifecycle Listener implementation class ServerStartListener
 *
 */
@WebListener
public class ServerStartListener implements ServletContextListener {

	private ReceiverSMC receiverSMC;
	
    /**
     * Default constructor. 
     */
    public ServerStartListener() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0) {
        // TODO Auto-generated method stub
    	receiverSMC = new ReceiverSMC(new receiverListener() {
			
			@Override
			public void onUnBind(Unbind unbind) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public int onReport(Report report) {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public int onDeliver(Deliver deliver) {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public int onBind(Bind bind) {
				// TODO Auto-generated method stub
				return 0;
			}
		});
    	
    	new Thread(new Runnable() {
			public void run() {
				
				try {
					receiverSMC.startReceiver();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
    	
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0) {
        // TODO Auto-generated method stub
    	if(receiverSMC != null){
    		try {
				receiverSMC.closeReceiver();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    	
    }
	
}
