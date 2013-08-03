<%@page import="cn.server.bean.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ include file="header.jsp"%>

<%
User user = (User) session.getAttribute("User");
if(user == null || user.getGrade() != 3){
	%>
		<script type="text/javascript">
		alert('权限不够无法访问');
		</script>
	<%
	response.sendRedirect("index.jsp");
	
}else{
%>
	



<div data-role="page" id="user_manager" data-theme="b">
	<script type="text/javascript">
$('#user_manager').bind('pagecreate', function(){
	// 如果当前浏览器不支持，则停止执行
	if(!browserSupport()){
		alert('请使用以下浏览器进行浏览（Chrome20+,FireFox10+,IE9+,360浏览器6.0+）');
	}else{
		
		//判断是否登录
		if(isLogin()){
			initEvent_UserManager();
			loadUserList();
		}else{
			$.mobile.changePage('login.jsp');
			
		}
		
	}
});
/**
 * 加载用户列表
 */
function loadUserList(){
	$('#userlist').find('.task_item').remove();
	// 显示loading动画
	$.mobile.loading( 'show', {
		  text: '正在加载...',
		  textVisible: true,
		  theme: 'a',
		  textonly: false,
		  html: ''
	});
	ajax.action_104_getuserlist({
		success: function(data){
			$.mobile.loading('hide');
			console.debug(data);
			if(data.res){
				changeUsetList(data.pld.UserList);			
			}else{
				alert(data.pld.errorMsg);
			}
		},
		error: function(data){
			$.mobile.loading('hide');
			alert('请求失败.');
			console.debug(data);
		}
	});
}
/**
 * 用户列表
 */
function changeUsetList(userarray){
	
	for(var i = 0 ; i < userarray.length ; i ++){
		var user = userarray[i];
		var li = $('<li class="task_item" data-theme="c" />');
		li.data('user', user);
		var a = $('<a class="ui-link-inherit" />').append('<h3 style="color:'+(user.grade == 3 ? 'red':'black')+'">'+user.UserName+'</h3>');
		var p = $('<p/>').text('用户ID：'+user.UserID+' , 用户权限：'+(user.grade == 3 ? '管理员':'普通用户')+' , 上次登录时间：'+user.LastLoginTime);
		
		a.append(p);
		li.append(a);
		li.on('tap', userItemClick);
		$('#userlist').append(li);
		$('#userlist').listview('refresh');
	}
	
}
/**
 * user点击事件
 */
function userItemClick(event){
	
	var pthis = this;
	
	$('<div>').simpledialog2({
	    mode: 'button',
	    width: '60%',
	    headerText: '编辑用户',
	    headerClose: true,
	    buttonPrompt: '请选择操作',
	    buttons : {
		  '删除': {
		        click: function () { 
		        	$.mobile.loading( 'show', {
		      		  text: '正在加载...',
		      		  textVisible: true,
		      		  theme: 'a',
		      		  textonly: false,
		      		  html: ''
		      		});
		        	var user = $(pthis).data('user');
		        	
		       		ajax.action_106_deleteuser({
		       			UserID: user.UserID,
		       			success: function(data){
		       				$.mobile.loading('hide');
		       				
		       				if(data.res){
		       					alert('删除成功');
		        				loadUserList();
								$('#userlist').listview('refresh');
		       				}else{
		       					alert(data.pld.errorMsg);
		       				}
		       				
		       			},
		       			error: function(data){
		       				$.mobile.loading('hide');
		       				alert('请求失败.');
		       				
		       			}
		       		});
		        },
		        icon: "delete",
		        theme: "c"
		  },
	      '取消': {
	        click: function () { 
	          
	        },
	        icon: "delete",
	        theme: "c"
	      }
	    }
	  });
	
}

/**
 * 初始化事件
 */
function initEvent_UserManager(){
	$('.logout_button').on('tap', function(event){
		logout();
	});
	$('#adduser').on('tap', function(event){
		addUser();		
	});
	
}
/**
 * 增加用户
 */
function addUser(){
	
	var content = '<form id="adduser_form" style="padding:10px;"><div data-role="fieldcontain"><label for="username">用户名</label><input name="username" id="username" placeholder="" value="" type="text"></div><div data-role="fieldcontain"><label for="password">密码</label><input name="password" id="password" placeholder="" value="" type="password"></div><div data-role="fieldcontain"><label for="grade">用户角色</label><select name="toggleswitch1" id="grade" data-theme="c" data-role="slider"><option value="off">用户</option><option value="on">管理员</option></select></div></form>';
	
	
	$('<div>').simpledialog2({
	    mode: 'button',
	    width: '60%',
	    headerText: '增加用户',
	    headerClose: true,
	    buttonPrompt: content,
	    buttons : {
	      '增加': {
	        click: function () {
	        	// 显示loading动画
	        	$.mobile.loading( 'show', {
	        		  text: '正在加载...',
	        		  textVisible: true,
	        		  theme: 'a',
	        		  textonly: false,
	        		  html: ''
	        	});
	        	var username = $('#adduser_form').find('#username').val();;
	        	var password = $('#adduser_form').find('#password').val();;
	        	var grade = $('#adduser_form').find('#grade').val();;
	        	
	        	if(username == undefined || username.length == 0){
	        		$.mobile.loading('hide');
	        		alert('用户名不能为空');
	        		return;
	        	}
	        	if(password == undefined || password.length == 0){
	        		$.mobile.loading('hide');
	        		alert('密码不能为空');
	        		return;
	        	}
	        	
	        	ajax.action_105_adduser({
	        		UserName: username,
	        		PassWord: $.md5(password),
	        		grade: grade == 'off' ? 1 : 3,
	        		success: function(data){
	        			$.mobile.loading('hide');
	        			if(data.res){
	        				alert('增加成功');
	        				loadUserList();
							$('#userlist').listview('refresh');
							
	        			}else{
	        				alert(data.pld.errorMsg);
	        			}
	        		},
	        		error: function(){
	        			$.mobile.loading('hide');
	        			alert('请求失败.');
	        		}
	        	});
	        	
	        	console.debug(username,password,grade);
	        	
	        },
	        icon: "minus",
		  },
	      '取消': {
	        click: function () { 
	          
	        },
	        icon: "delete",
	        theme: "c"
	      }
	    }
	  });
	
}

</script>
	<div data-role="header">
		<h1>用户管理</h1>
		<a data-role="button" class="logout_button" data-icon="back"
			href="login.jsp" data-iconpos="left" class="ui-btn-left">退出 </a>
	</div>


	<div data-role="content">

		<jsp:include page="menu.jsp">
			<jsp:param value="usermanager" name="id"/>
		</jsp:include>

		<div class="content_main ">
			<a id="adduser" data-icon="plus" data-role="button"
				data-inline="true"> 增加用户 </a> 

			<ul data-role="listview" id="userlist" data-divider-theme="b"
				data-inset="true">
				<li data-role="list-divider" role="heading">用户列表</li>
				
			</ul>

		</div>



	</div>

	<div data-role="footer" data-theme="c">
		<p style="text-align: center">By Panshihao.Cn</p>
	</div>


</div>

<%
}
%>

<%@ include file="footer.jsp"%>