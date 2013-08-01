package cn.test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.common.MyException;
import cn.server.bean.User;

public class Test {

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
		System.out.println(new File("E:\\vmware.log").getName());
	}

}
