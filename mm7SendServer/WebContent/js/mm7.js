	
/**
 * 退出登录
 */
function logout(){
	localStorage.removeItem('User');
//	ajax.action_102_userlogin();
}
/**
 * 浏览器支持判断
 * @return boolean 浏览器支持返回true，不支持返回false
 */
function browserSupport(){
	
	if(localStorage == undefined){
		return false;
	}
	
	return true;
	
}
/**
 * 判断当前是否登录
 * @return boolean 已登录返回true，未登录返回false
 */
function isLogin(){
	
	if(!localStorage.User || localStorage.User == undefined){
		return false;
	}else{
		return true;
	}
	
}
/**
 * 生产随机字符串
 * @param l
 * @returns {String}
 */
function  random_char(l)  {
	var  x="0123456789qwertyuioplkjhgfdsazxcvbnm";
	var  tmp="";
	for(var  i=0;i<  l;i++)  {
	  tmp  +=  x.charAt(Math.ceil(Math.random()*100000000)%x.length);
	}
	
	return  tmp;
}