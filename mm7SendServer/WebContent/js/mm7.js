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
	initEvent();
	
	//判断是否登录
	if(isLogin()){
		$.mobile.changePage($('#main'));
	}else{
		$.mobile.changePage($('#login'));
	}
	
}
/**
 * 初始化事件
 */
function initEvent(){
	$('#login_button').bind('tap', function(event){
		loginValidation();
	});
	$('#logout_button').bind('tap', function(event){
		logout();
	});
	$('#main').on('pagecreate', main_onPageCreate);
	$('#login').on('pagecreate', login_onPageCreate);
	
	$('#login input').keydown(function(event){
		if(event.keyCode == 13){
			loginValidation();
		}
	});
}
/**
 * main页面show事件
 * @param event
 * @param object
 */
function main_onPageCreate(event, object){
}
/**
 * login页面show事件
 * @param event
 * @param object
 */
function login_onPageCreate(event, object){
	
}
/**
 * 退出登录
 */
function logout(){
	localStorage.removeItem('User');
	$.mobile.changePage($('#login'));
}
/**
 * 登录验证
 */
function loginValidation(){
	// 显示loading动画
	$.mobile.loading( 'show', {
		  text: '正在加载...',
		  textVisible: true,
		  theme: 'a',
		  textonly: false,
		  html: ''
	});
	
	var username = $('#login_username').val();
	var password = $('#login_password').val();
	
	if(username == undefined || username.length < 4){
		$.mobile.loading('hide');
		alert('用户名不能为空，并且长度不能少于4个字符.');
		return;
	}else if(password == undefined || password.length < 4){
		$.mobile.loading('hide');
		alert('密码不能为空，并且长度不能少于4个字符.');
		return;
	}
	
	// 模拟登录
	if(username == 'admin' && password == 'admin'){
		$.mobile.loading('hide');
		// 登录成功后调用
		var user = new Object();
		user.username = 'admin';
		user.grade = 3;
		
		loginSuccess(user);
	}else{
		$.mobile.loading('hide');
		alert('用户名或密码错误');
		return;
	}
	
	
	
	
}
/**
 * 登录成功
 * @param user
 */
function loginSuccess(user){
	
	localStorage.setItem('User', user);
	
	$.mobile.changePage($('#main'));
	
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