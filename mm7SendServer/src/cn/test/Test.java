package cn.test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import cn.common.MyException;
import cn.server.bean.MyLimit;
import cn.server.bean.SendTask;
import cn.server.bean.User;
import cn.server.dao.PhoneNumDAO;
import cn.server.dao.SendTaskDAO;

public class Test {
	private static Logger log = Logger.getLogger(Test.class);
	

	public Test() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		TestDB test = new TestDB();
//		try {
//			ArrayList<User> list = new ArrayList<>();
//			list = (ArrayList<User>) test.getUserList();
//			System.out.println(list.size());
//			for (int i = 0; i < list.size(); i++) {
//				User user = (User) list.get(i);
//				if(user.getLastLoginTime()!= null){
//					System.out.println(user.getLastLoginTime());
//				}
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			System.out.println(e.getMessage());
//			e.printStackTrace();
//		}
		
		
//		PropertyConfigurator.configure("e:\\log4j.properties");
//		TestLog test = new TestLog();
//		test.testlog();
//		MyLimit limit = new MyLimit(2, 10);
//		System.out.println(limit.getPageNum());
//		PhoneNumDAO t = new PhoneNumDAO();
//		List<String> list = new ArrayList<String>();
//		list.add("11");
//		list.add("22");
//		list.add("33");
//		list.add("44");
//		list.add("55");
//		list.add("66");
//		list.add("77");
//		list.add("88");
//		
//		try {
//			t.insertFailPhoneNum(2, list);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		SendTask send = new SendTask();
//		send.setName("哈哈");
//		send.setToCount(10000);
//		send.setCustomTo("1,2,3,4,5");
//		send.setState(1);
//		
//		SendTaskDAO s = new SendTaskDAO();
//		try {
//			System.out.println(s.insertSendTask(send));
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		List list = new ArrayList<>();
		list.add("haha");
		list.add("hehe");
		list.add("huhu");
		
		System.out.println(list.toString());
	}

}
