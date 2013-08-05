<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ include file="header.jsp"%>

<div data-role="page" id="showsendtask" data-theme="b">
	<script type="text/javascript">
$('#showsendtask').bind('pagecreate', function(){
	// 如果当前浏览器不支持，则停止执行
	if(!browserSupport()){
		alert('请使用以下浏览器进行浏览（Chrome20+,FireFox10+,IE9+,360浏览器6.0+）');
	}else{
		
		//判断是否登录
		if(isLogin()){
			initEvent_ShowSendTask();
		}else{
			$.mobile.changePage('login.jsp');
			
		}
		
	}
});
/**
 * 初始化事件
 */
function initEvent_ShowSendTask(){
	$('.logout_button').on('tap', function(event){
		logout();
	});
	$('.task_item').on('tap', function(event){
		openTaskInfo(this);
	});
}
/**
 * 打开任务详情
 */
function openTaskInfo(obj){
	var str = $('<div/>');
	str.append('<p style="padding:5px">任务ID：2 , 创建时间：2013-07-28 18:38:22 , 接收号码数量：182730 , 任务状态：正在发送 , 成功数量：0 , 	失败数量: 0 ,完成时间：未完</p>');
	
	$('<div>').simpledialog2({
		mode: 'button',
		width: '360px',
	    headerText: '任务a',
	    headerClose: true ,
	    buttonPrompt: str.html(),
	    buttons:{}
	});
}
</script>
	<div data-role="header">
		<h1>查看短信任务</h1>
		<a data-role="button" class="logout_button" data-icon="back"
			href="login.jsp" data-iconpos="left" class="ui-btn-left">退出 </a>
	</div>


	<div data-role="content">

		<jsp:include page="menu.jsp">
			<jsp:param value="showsmctask" name="id"/>
		</jsp:include>

		<div class="content_main">

			<ul data-role="listview" id="sending" data-divider-theme="b"
				data-inset="true">
				<li data-role="list-divider" role="heading">正在发送的任务</li>
				<li class="task_item" data-theme="c"><a class="ui-link-inherit">
						<h3>任务a</h3>
						<p>任务ID：2 , 创建时间：2013-07-28 18:38:22 , 接收号码数量：182730 ,
							任务状态：正在发送 , 成功数量：0 , 失败数量: 0 , 完成时间：未完</p>
				</a></li>
			</ul>
			<ul data-role="listview" id="resending" data-divider-theme="b"
				data-inset="true">
				<li data-role="list-divider" role="heading">正在重发的任务</li>
				<li class="task_item" data-theme="b"><a class="ui-link-inherit">
						<h3>任务b</h3>
						<p>任务ID：3 , 创建时间：2013-07-28 18:38:22 , 接收号码数量：182730 ,
							任务状态：正在重发 , 成功数量：180030 , 失败数量: 0 , 完成时间：未完</p>
				</a></li>
			</ul>

			<ul data-role="listview" id="sended" data-divider-theme="b"
				data-inset="true">
				<li data-role="list-divider" role="heading">已发送的任务</li>
				<li class="task_item" data-theme="f"><a class="ui-link-inherit">
						<h3>任务c</h3>
						<p>任务ID：4 , 创建时间：2013-07-28 18:38:22 , 接收号码数量：182730 ,
							任务状态：已发送 , 成功数量：180030 , 失败数量: 2700 , 完成时间：2013-07-28 19:22:12</p>
				</a></li>
			</ul>
			<ul data-role="listview" id="faild" data-divider-theme="b"
				data-inset="true">
				<li data-role="list-divider" role="heading">发送失败的任务</li>
				<li class="task_item" data-theme="e"><a class="ui-link-inherit">
						<h3>任务d</h3>
						<p>任务ID：5 , 创建时间：2013-07-28 18:38:22 , 接收号码数量：182730 ,
							任务状态：发送失败 , 成功数量：0 , 失败数量: 182730 , 完成时间：2013-07-28 19:22:12</p>
				</a></li>
			</ul>


		</div>



	</div>

	<div data-role="footer" data-theme="c">
		<p style="text-align: center">By Panshihao.Cn</p>
	</div>


</div>


<div data-role="page" id="task_info">
	<div data-role="header">
		<h1>任务a</h1>
	</div>
	<div data-role="content">asdasdasdads</div>
</div>

<%@ include file="footer.jsp"%>