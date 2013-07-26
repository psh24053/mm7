/**
 * 初始化页面
 * @param event
 * @param Object
 */
function PageInit(event, Object){
	
	// 如果当前浏览器不支持，则停止执行
	if(!browserSupport()){
		alert('请使用以下浏览器进行浏览（Chrome20+,FireFox10+,IE9+,360浏览器6.0+）');
		return false;
	}
	
	//判断是否登录
	if(isLogin()){
		$.mobile.changePage($('#main'));
	}else{
		$.mobile.changePage($('#login'));
	}
	
	initEvent();
}
/**
 * 初始化事件
 */
function initEvent(){
	$('#login_button').bind('tap', function(event){
		$.mobile.loading( 'show', {
			text: 'foo',
			textVisible: true,
			theme: 'z',
			html: ""
		});
	});
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
	
	var user = localStorage.User;
	if(user == undefined){
		return false;
	}else{
		return true;
	}
	
}