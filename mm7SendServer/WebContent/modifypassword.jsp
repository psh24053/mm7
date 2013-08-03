<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ include file="header.jsp"%>

<div data-role="page" id="modify_password" data-theme="b">
	<script type="text/javascript">
$('#modify_password').bind('pagecreate', function(){
	// 如果当前浏览器不支持，则停止执行
	if(!browserSupport()){
		alert('请使用以下浏览器进行浏览（Chrome20+,FireFox10+,IE9+,360浏览器6.0+）');
	}else{
		
		//判断是否登录
		if(isLogin()){
			initEvent_ModifyPassword();
		}else{
			$.mobile.changePage('login.jsp');
			
		}
		
	}
});
/**
 * 初始化事件
 */
function initEvent_ModifyPassword(){
	$('.logout_button').on('tap', function(event){
		logout();
	});
	$('#modifypassword_button').on('tap', function(event){
		modifyValidation();
	});
}
/**
 * 验证修改
 */
function modifyValidation(){
	// 显示loading动画
	$.mobile.loading( 'show', {
		  text: '正在加载...',
		  textVisible: true,
		  theme: 'a',
		  textonly: false,
		  html: ''
	});
	var oldpassword = $('#oldpassword').val();
	var newpassword = $('#newpassword').val();
	var confirmpassword = $('#confirmpassword').val();
	
	if(oldpassword == undefined || oldpassword.length < 4){
		$.mobile.loading('hide');
		alert('旧密码不能为空，并且长度不能少于4个字符.');
		return;
	}
	if(newpassword == undefined || newpassword.length < 4){
		$.mobile.loading('hide');
		alert('新密码不能为空，并且长度不能少于4个字符.');
		return;
	}
	if(confirmpassword == undefined || confirmpassword.length < 4){
		$.mobile.loading('hide');
		alert('确认密码不能为空，并且长度不能少于4个字符.');
		return;
	}
	
	if(newpassword != confirmpassword){
		$.mobile.loading('hide');
		alert('新密码和确认密码必须一致');
		return;
	}
	
	ajax.action_103_modifypassword({
		PassWord: $.md5(oldpassword),
		NewPassWord: $.md5(newpassword),
		success: function(data){
			$.mobile.loading('hide');
			if(data.res){
				alert('修改密码成功！请重新登录！');
				logout();
				$.mobile.changePage('login.jsp');
			}else{
				alert(data.pld.errorMsg);
			}
			
		},
		error: function(data){
			alert('请求失败.');
			console.debug(data);
			$.mobile.loading('hide');
		}
	});
	
	
	
}
</script>
	<div data-role="header">
		<h1>修改密码</h1>
		<a data-role="button" class="logout_button" data-icon="back"
			href="login.jsp" data-iconpos="left" class="ui-btn-left">退出 </a>
	</div>


	<div data-role="content">

		<jsp:include page="menu.jsp">
			<jsp:param value="modifypassword" name="id"/>
		</jsp:include>

		<div class="content_main ">

			<form action="">
				<div data-role="fieldcontain">
					<label for="oldpassword"> 原密码 </label> <input name=""
						id="oldpassword" placeholder="" value="" type="password">
				</div>
				<div data-role="fieldcontain">
					<label for="newpassword"> 新密码 </label> <input name=""
						id="newpassword" placeholder="" value="" type="password">
				</div>
				<div data-role="fieldcontain">
					<label for="confirmpassword"> 确认新密码 </label> <input name=""
						id="confirmpassword" placeholder="" value="" type="password">
				</div>
				<a data-role="button" id="modifypassword_button">修改密码</a>
			</form>


		</div>



	</div>

	<div data-role="footer" data-theme="c">
		<p style="text-align: center">By Panshihao.Cn</p>
	</div>


</div>

<%@ include file="footer.jsp"%>