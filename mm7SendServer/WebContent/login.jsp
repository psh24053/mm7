<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ include file="header.jsp"%>

<div data-role="page" id="login" data-theme="b">
	<script type="text/javascript">
	$('#login').bind('pagecreate', function(){
		if(!browserSupport()){
			alert('请使用以下浏览器进行浏览（Chrome20+,FireFox10+,IE9+,360浏览器6.0+）');
		}else{
			//判断是否登录
			if(isLogin()){
				$.mobile.changePage('index.jsp'); 
			}else{
				initEvent_Login();
			}
			
		}
	}); 
	/**
	 * 初始化事件
	 */
	function initEvent_Login(){
		$('#login_button').bind('tap', function(event){
			loginValidation();
		});
		$('#login input').keydown(function(event){
			if(event.keyCode == 13){
				loginValidation();
			}
		});
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
		
		ajax.action_101_userlogin({
			UserName: username,
			PassWord: $.md5(password),
			success: function(data){
				if(data.res){
					// 登录成功后调用
					var user = new Object();
					user.username = username;
					
					loginSuccess(user);
				}else{
					alert(data.pld.errorMsg);
				}	
				$.mobile.loading('hide');
			},
			error: function(data){
				console.debug(data);
				alert('请求失败.');
				$.mobile.loading('hide');
			}
		});
		
		
		/* // 模拟登录
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
		} */
		
		
		
		
	}
	/**
	 * 登录成功
	 * @param user
	 */
	function loginSuccess(user){
		
		localStorage.setItem('User', user);
		$.mobile.changePage('index.jsp'); 
		
	}
	</script>



	<div data-role="header">
		<h1>登 录</h1>
	</div>

	<div data-role="content">
		<form action="">
			<div data-role="fieldcontain">
				<label for="login_username">用户名</label> <input name=""
					id="login_username" placeholder="" value="" type="text">
			</div>
			<div data-role="fieldcontain">
				<label for="login_password">密码</label> <input name=""
					id="login_password" placeholder="" value="" type="password">
			</div>
			<a data-role="button" id="login_button">登录 </a>
		</form>
	</div>
	<div data-role="footer" data-theme="c">
		<p style="text-align: center">By Panshihao.Cn</p>
	</div>
</div>

<%@ include file="footer.jsp"%>
