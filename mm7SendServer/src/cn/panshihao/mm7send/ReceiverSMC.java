package cn.panshihao.mm7send;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;

import spApi.Bind;
import spApi.BindResp;
import spApi.Deliver;
import spApi.DeliverResp;
import spApi.Report;
import spApi.ReportResp;
import spApi.SGIP_Command;
import spApi.Unbind;
import spApi.UnbindResp;

/**
 * 监听短信消息
 * @author Panshihao
 *
 */
public class ReceiverSMC {

	private ServerSocket serverSocket;
	private receiverListener listener;
	
	
	/**
	 * 传入接收事件监听器，通过监听器来进行操作
	 * @param listener
	 */
	public ReceiverSMC(receiverListener listener){
		this.listener = listener;
	}
	
	/**
	 * 开始监听
	 * @throws IOException 
	 */
	public void startReceiver() throws IOException{
		
		System.out.println("Receiver SMC Start!");
		
		serverSocket = new ServerSocket(SPINFO.SGIP_SP_PORT);
		
		while(!serverSocket.isClosed()){
			
			Socket socket = serverSocket.accept();
			System.out.println(socket);
			// 启动处理线程
			new receiverThread(socket, socket.getRemoteSocketAddress().toString()).start();
			
		}
		
		
		
	}
	/**
	 * 关闭监听
	 * @throws IOException 
	 */
	public void closeReceiver() throws IOException{
		if(serverSocket != null && !serverSocket.isClosed()){
			System.out.println("Receiver SMC Close!");
			serverSocket.close();
		}
		
	}
	/**
	 * 接收信息监听器
	 * @author Panshihao
	 *
	 */
	public interface receiverListener{
		
		/**
		 * bind事件来临时触发，返回resultcode
		 * @param bind 
		 * @return resultCode
		 */
		public int onBind(Bind bind);
		/**
		 * unbind事件来临时触发
		 * @param unbind
		 */
		public void onUnBind(Unbind unbind);
		/**
		 * deliver事件来临时触发，返回resultcode
		 * @param deliver
		 * @return resultCode
		 */
		public int onDeliver(Deliver deliver);
		/**
		 * report事件来临时触发，返回resultcode
		 * @param report
		 * @return resultcode
		 */
		public int onReport(Report report);
	}
	/**
	 * 监听线程
	 * @author Panshihao
	 *
	 */
	private class receiverThread extends Thread{
		
		private Socket socket;
		private OutputStream out;
		private InputStream input;
		private boolean run = true;
		
		public receiverThread(Socket socket, String threadName) throws IOException {
			setName(threadName);
			this.socket = socket;
			this.out = new DataOutputStream(socket.getOutputStream());
			this.input = new DataInputStream(socket.getInputStream());
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			
			SGIP_Command command = new SGIP_Command(SPINFO.SGIP_NODEID);
			SGIP_Command temp = null;
			
			// 无限循环读取
			while(run){
				try {
					temp = command.read(input);
				} catch (IOException e) {
					run = false;
					e.printStackTrace();
				} catch (Exception e) {
					run = false;
					e.printStackTrace();
				}
				int result = ResultCode.success;
 
				switch (temp.getCommandID()) {
				case SGIP_Command.ID_SGIP_BIND:
					System.out.println("------------------Thread["+getName()+"]------------------------------");
					System.out.println("SGIP Receiver -> SGIP_BIND");
					System.out.println("SGIP Receiver -> SEQNUMBER -> "+temp.getSeqno_1() + "," + temp.getSeqno_2() + "," + temp.getSeqno_3());
					
					Bind bind = (Bind) temp;
					bind.readbody();
					String n = null;
					try {
						n = new String(bind.GetLoginName().getBytes("GBK"), "UTF-8");
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
					System.out.println("name   -> "+n);
					
					
					if(bind.GetLoginName().equals(SPINFO.SGIP_SMG_NAME) &&
							bind.GetLoginPassword().equals(SPINFO.SGIP_SMG_PASSWORD)){
						
						result = ResultCode.success;
						
						System.out.println("SGIP_BIND -> Login Success");
						
					}else{
						result = ResultCode.faillogin;
						
						System.out.println("SGIP_BIND -> Login Fail");
					}
					
					
					if(listener != null){
						listener.onBind(bind);
					}
					
					BindResp bindResp = new BindResp(SPINFO.SGIP_NODEID);
					// 设置result结果码
					bindResp.SetResult(result);
					bindResp.write(out);
					
					break;
				case SGIP_Command.ID_SGIP_UNBIND:
					System.out.println("------------------Thread["+getName()+"]------------------------------");
					System.out.println("SGIP Receiver -> SGIP_UNBIND");
					System.out.println("SGIP Receiver -> SEQNUMBER -> "+temp.getSeqno_1() + "," + temp.getSeqno_2() + "," + temp.getSeqno_3());
					
					if(listener != null){
						listener.onUnBind((Unbind)temp);
					}	
					
					// 发送unBind请求
					UnbindResp unbindresp = new UnbindResp(SPINFO.SGIP_NODEID);
					unbindresp.write(out);
					
					run = false;
					break;
				case SGIP_Command.ID_SGIP_DELIVER:
					System.out.println("------------------Thread["+getName()+"]------------------------------");
					System.out.println("SGIP Receiver -> SGIP_DELIVER");
					System.out.println("SGIP Receiver -> SEQNUMBER -> "+temp.getSeqno_1() + "," + temp.getSeqno_2() + "," + temp.getSeqno_3());

					Deliver deliver = (Deliver) temp;
					deliver.readbody();
					
					if(listener != null){
						result = listener.onDeliver(deliver);
					}
					
					DeliverResp deliverresp = new DeliverResp(result, (int) SPINFO.SGIP_NODEID);
					// 设置result结果码
					deliverresp.SetResult(result);
					deliverresp.write(out);
					
					break;
				case SGIP_Command.ID_SGIP_REPORT:
					System.out.println("------------------Thread["+getName()+"]------------------------------");
					System.out.println("SGIP Receiver -> SGIP_REPORT");
					System.out.println("SGIP Receiver -> SEQNUMBER -> "+temp.getSeqno_1() + "," + temp.getSeqno_2() + "," + temp.getSeqno_3());

					
					Report report = (Report) temp;
					report.readbody();
					
					if(listener != null){
						result = listener.onReport(report);
					}
					
					ReportResp reportResp = new ReportResp(result, (int) SPINFO.SGIP_NODEID);
					// 设置result结果码
					reportResp.SetResult(result);
					reportResp.write(out);
					
					break;
				default:
					break;
				}
				
			}
			
			
		}
		
		
	}
	/**
	 * result结果码
	 * @author Panshihao
	 *
	 */
	public class ResultCode{
	
		public static final int success = 1;
		public static final int faillogin = 2;
	}
	public static void main(String[] args) {
		ReceiverSMC smc = new ReceiverSMC(new receiverListener() {
			
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
		
		try {
			smc.startReceiver();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
