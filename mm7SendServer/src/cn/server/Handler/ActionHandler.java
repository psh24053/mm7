package cn.server.Handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import cn.common.MyException;
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
		int cod = 0;
		try {
			requestJSON = new JSONObject(requestStr);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return getErrorJson("请求字符串转化为JSON对象出现异常！！！").toString();
		}
		try {
			cod = requestJSON.getInt("cod");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return getErrorJson("handler解析cod出现异常！！！").toString();
		}
		try {
			switch (cod) {
			case 101:
				
				break;
			case 102:
				return this.addUser(requestJSON);

			default:
				break;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
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
			// TODO Auto-generated catch block
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
			// TODO Auto-generated catch block
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
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		
		return getResponseJson(cod, true, null).toString();
	}

}
