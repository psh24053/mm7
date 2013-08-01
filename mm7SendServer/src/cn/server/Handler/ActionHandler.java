package cn.server.Handler;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.common.MyException;
import cn.server.bean.User;
import cn.server.dao.PhoneNumDAO;
import cn.server.dao.SendTaskDAO;
import cn.server.dao.UserDAO;



public class ActionHandler {
	private static ActionHandler instance = null;
	
	public static synchronized ActionHandler getInstance(){

    	if(instance == null){
	
    		instance = new ActionHandler();
	
    	}
    	return instance;
    }

	public ActionHandler() {
		// TODO Auto-generated constructor stub
	}
	
	public String allHandler(String requestStr,
			HttpServletRequest request, HttpServletResponse response){
		JSONObject requestJSON = null;
		String responseStr = "";
		int cod = 0;
		try {
			requestJSON = new JSONObject(requestStr);
		} catch (JSONException e) {
			
			e.printStackTrace();
			
			return getErrorJson("请求字符串转化为JSON对象出现异常！！！").toString();
		}
		try {
			cod = requestJSON.getInt("cod");
		} catch (JSONException e) {
			
			e.printStackTrace();
			return getErrorJson("handler解析cod出现异常！！！").toString();
		}
		
//		if(cod != 101){
//			if(request.getSession().getAttribute("User") == null){
//				return getErrorJson("用户未登录请登陆！！！").toString();
//			}
//		}
		
		try {
			switch (cod) {
			case 101:
				
				break;
			case 102:
				responseStr = this.addUser(requestJSON);
				break;
			default:
				break;
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return null;
	}
	
	private static JSONObject getErrorJson(String errorMsg){
		JSONObject errorjson = new JSONObject();
		JSONObject pld = new JSONObject();
		try {
			errorjson.put("cod", 100);
			errorjson.put("res", false);
			pld.put("errorMsg", errorMsg);
			errorjson.put("pld", pld);
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
		return errorjson;
	}
	private static JSONObject getResponseJson(int cod,boolean res,JSONObject pld){
		JSONObject responseJson = new JSONObject();
		try {
			responseJson.put("cod", cod);
			responseJson.put("res", res);
			responseJson.put("pld", pld);
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
		return responseJson;
	}
	
	public String addUser(JSONObject requestJSON)throws Exception{
		JSONObject prm = null;
		int cod = 0;
		UserDAO userDao = new UserDAO();
		try {
			cod = requestJSON.getInt("cod");
			prm = requestJSON.getJSONObject("prm");
			String userName = prm.getString("UserName");
			String passWord = prm.getString("PassWord");
			int grade = prm.getInt("grade");
			if(userDao.checkUserExist(userName)){
				throw new MyException("该用户已存在！！！");
			}else{
				userDao.adduser(userName, passWord, grade);
			}
		} catch (JSONException e) {
			
			e.printStackTrace();
			throw e;
		}
		
		return getResponseJson(cod, true, null).toString();
	}
	
	public String userLogin(JSONObject requestJSON,
			HttpServletRequest request, HttpServletResponse response)throws Exception{
		int cod = 0;
		JSONObject pld = null;
		try {
			UserDAO userDao = new UserDAO();
			cod = requestJSON.getInt("cod");
			JSONObject prm = requestJSON.getJSONObject("prm");
			String userName = prm.getString("UserName");
			String passWord = prm.getString("PassWord");
			User user = userDao.getUserInfoByUserName(userName);
			if( user == null){
				return getErrorJson("用户不存在").toString();
			}else{
				if(0!=user.getPassword().compareTo(passWord)){
					return getErrorJson("密码错误").toString();
				}
			}
			userDao.updateLoginTime(user.getId());
			request.getSession().setAttribute("User", userName);
			pld = new JSONObject();
			pld.put("LastLoginTime", user.getLastLoginTime());
		} catch (Exception e) {
			
			e.printStackTrace();
			throw new MyException("用户登陆异常", e);
		}
		return getResponseJson(cod,true, pld).toString();
	}
	
	public String userLogout(JSONObject requestJSON,
			HttpServletRequest request, HttpServletResponse response)throws Exception{
		int cod = requestJSON.getInt("cod");
		request.getSession().removeAttribute("User");
		return getResponseJson(cod, true, null).toString();
	}
	
	public String deleteUser(JSONObject requestJSON,
			HttpServletRequest request, HttpServletResponse response)throws Exception{
		UserDAO userDao = new UserDAO();
		int cod = 0 ;
		try {
			cod = requestJSON.getInt("cod");
			JSONObject prm = requestJSON.getJSONObject("prm");
			int id = prm.getInt("UserID");
			userDao.removeUser(id);
		} catch (Exception e) {
			
			e.printStackTrace();
			throw new MyException("删除用户异常！", e);
		}
		return getResponseJson(cod, true, null).toString();
	}
	
	public String editPassWord(JSONObject requestJSON,
			HttpServletRequest request, HttpServletResponse response)throws Exception{
		int cod = 0;
		UserDAO userDao = new UserDAO();
		String newPass = "";
		User user = new User();
		try {
			cod = requestJSON.getInt("cod");
			JSONObject prm = requestJSON.getJSONObject("prm");
			String pass = prm.getString("PassWord");
			newPass = prm.getString("NewPassWord");
			String username = request.getSession().getAttribute("User").toString();
			user = userDao.getUserInfoByUserName(username);
			if(0!=pass.compareTo(user.getPassword())){
				return getErrorJson("原密码错误").toString();
			}
		} catch (Exception e) {
			
			e.printStackTrace();
			throw new MyException("修改密码异常！", e);
			
		}
		userDao.editPassWord(user.getId(), newPass);
		return getResponseJson(cod, true, null).toString();
	}
	
	public String getUserList(JSONObject requestJSON,
			HttpServletRequest request, HttpServletResponse response)throws Exception{
		int cod = 0;
		List<User> list = null;
		UserDAO userDao = new UserDAO();
		JSONObject pld = new JSONObject();
		JSONArray ja = new JSONArray();
		cod = requestJSON.getInt("cod");
		list = userDao.getUserList();
		for (int i = 0; i < list.size(); i++) {
			User user = list.get(i);
			JSONObject temp = new JSONObject();
			temp.put("UserID", user.getId());
			temp.put("UserName", user.getUsername());
			temp.put("grade", user.getGrade());
			temp.put("LastLoginTime", user.getLastLoginTime());
			ja.put(temp);
		}
		pld.put("UserList", ja);
		return getResponseJson(cod, true, pld).toString();
	}
	
	public String newSendTask(JSONObject requestJSON,
			HttpServletRequest request, HttpServletResponse response)throws Exception{
		int cod = 0;
		JSONObject prm = new JSONObject();
		SendTaskDAO sendTask = new SendTaskDAO();
		PhoneNumDAO phoneNum = new PhoneNumDAO();
		
		cod = requestJSON.getInt("cod");
		prm = requestJSON.getJSONObject("prm");
		
		return null;
	}
	
}
