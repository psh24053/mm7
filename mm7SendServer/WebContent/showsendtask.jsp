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
			loadSendTaskList();
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
	
}
/**
 * 打开任务详情
 */
function openTaskInfo(obj){
	// 显示loading动画
	$.mobile.loading( 'show', {
		  text: '正在加载...',
		  textVisible: true,
		  theme: 'a',
		  textonly: false,
		  html: ''
	});
	
	var taskItem = $(obj).data('taskitem');
	ajax.action_110_getcontentbytaskid({
		ID: taskItem.sendTaskId,
		success: function(data){
			$.mobile.loading('hide');
			
			if(data.res){
				
				var str = $('<div>');
				
				var content = $('<ul style="padding:10px" data-role="listview" id="content" data-divider-theme="b" data-inset="true" />');
				for(var i = 0 ; i < data.pld.content.length ; i ++){
					var con = data.pld.content[i];
					var item = $('<li/>');
					item.attr('data-theme', 'c');
					var itemA = $('<a/>');
					itemA.css('white-space', 'normal');
					itemA.css('word-break', 'break-all');
					itemA.css('word-wrap', 'break-word');
					
					if(con.Type == 1){
						
						itemA.append('<span class="content_Str">'+con.content+'</span>');
						
					}else if(con.Type == 2){
						itemA.append('<img class="content_img" src="FileDownload?filename='+con.content+'" style="border: 1px #666666 solid;max-width: 500px;" />');
					}
					
					item.append(itemA);
					content.append(item);
				}
				
				str.append(content);
				
				
				$('<div>').simpledialog2({
					themeHeader: 'b',
					mode: 'button',
					width: '380px',
				    headerText: taskItem.name,
				    headerClose: true ,
				    buttonPrompt: str.html(),
				    buttons:{}
				});
			}else{
				alert(data.pld.errorMsg);
			}
			
			
		},
		error: function(){
			$.mobile.loading('hide');
			alert('请求失败');
		}
	});
	
	
	
}
/**
 * 加载内容
 */
function loadSendTaskList(){
	// 显示loading动画
	$.mobile.loading( 'show', {
		  text: '正在加载...',
		  textVisible: true,
		  theme: 'a',
		  textonly: false,
		  html: ''
	});
	
	ajax.action_107_getsendtasklist({
		PageNum: 1,
		CountLimit: 1000,
		success: function(data){
			$.mobile.loading('hide');
			if(data.res){
				changeListView(data);			
			}else{
				alert(data.pld.errorMsg);
			}
		},
		error: function(data){
			$.mobile.loading('hide');
			alert('请求失败.');
		}
	});
}
/**
 * 改变listview内容
 */
function changeListView(data){
	var sending = $('#sending');
	var sended = $('#sended');
	var faild = $('#faild');
	
	sending.find('.task_item').remove();
	sended.find('.task_item').remove();
	faild.find('.task_item').remove();
	
	var list = data.pld.TaskList;
	for(var i = 0 ; i < list.length ; i ++){
		var taskItem = list[i];
		var li = $('<li class="task_item" data-theme="c" />');
		li.data('taskitem', taskItem);
		li.on('tap', function(event){
			openTaskInfo(this);
		});
		var a = $('<a class="ui-link-inherit" />');
		var h3 = $('<h3 />').text(taskItem.name);
	
		var stateStr = null;
		var completeTime = null;
		
		switch (taskItem.state) {
		case 1:
			// 1代表正在发送
			stateStr = "正在发送";
			completeTime = "未完成";
			break;
		case 2:
			// 3代表发送完成
			stateStr = "发送完成";
			completeTime = taskItem.completeTime;
			break;
		case 3:
			// 4代表发送失败
			stateStr = "发送失败";
			completeTime = "失败";
			break;
		default:
			break;
		}
		
		
		
		var p = $('<p />').text(
						'任务ID：' + taskItem.sendTaskId + ' , 创建时间：'
								+ taskItem.createTime + ' , 接收号码数量：'
								+ taskItem.toCount + ' , 任务状态：'
								+ stateStr + ' , 完成时间：'+completeTime);

		
		a.append(h3).append(p).appendTo(li);
		
		
		switch (taskItem.state) {
		case 1:
			// 1代表正在发送
			sending.append(li);
			break;
		case 2:
			// 3代表发送完成
			sended.append(li);
			break;
		case 3:
			// 4代表发送失败
			faild.append(li);
			break;
		default:
			break;
		}
		
		
	}
			sending.listview('refresh');
			sended.listview('refresh');
			faild.listview('refresh');

			/* <li class="task_item" data-theme="c"><a class="ui-link-inherit">
			<h3>任务a</h3>
			<p>任务ID：2 , 创建时间：2013-07-28 18:38:22 , 接收号码数量：182730 ,
				任务状态：正在发送 , 成功数量：0 , 失败数量: 0 , 完成时间：未完</p>
			</a></li> */

			/* 	temp.put("sendTaskId", sendtask.getSendTaskId());
			 temp.put("name", sendtask.getName());
			 temp.put("createTime", sendtask.getCreateTime());
			 temp.put("toCount", sendtask.getToCount());
			 temp.put("state", sendtask.getState());
			 temp.put("successCount", sendtask.getSuccessCount());
			 temp.put("failCount", sendtask.getFailCount());
			 temp.put("completeTime", sendtask.getCompleteTime());
			 String customNumber = sendtask.getCustomTo();
			 String[] strArray = null;   
			 strArray = customNumber.split(",");
			 JSONArray tempja = new JSONArray();
			 for (int j = 0; j < strArray.length; j++) {
			 tempja.put(strArray[i]);
			 }
			 temp.put("CustomNumber", tempja); */

}
	</script>
	<div data-role="header">
		<h1>查看彩信任务</h1>
		<a data-role="button" class="logout_button" data-icon="back"
			href="login.jsp" data-iconpos="left" class="ui-btn-left">退出 </a>
	</div>


	<div data-role="content">

		<jsp:include page="menu.jsp">
			<jsp:param value="showsendtask" name="id"/>
		</jsp:include>

		<div class="content_main">

			<ul data-role="listview" id="sending" data-divider-theme="b"
				data-inset="true">
				<li data-role="list-divider" role="heading">正在发送的任务</li>
				
			</ul>
			<!-- <ul data-role="listview" id="resending" data-divider-theme="b"
				data-inset="true">
				<li data-role="list-divider" role="heading">正在重发的任务</li>
				<li class="task_item" data-theme="b"><a class="ui-link-inherit">
						<h3>任务b</h3>
						<p>任务ID：3 , 创建时间：2013-07-28 18:38:22 , 接收号码数量：182730 ,
							任务状态：正在重发 , 成功数量：180030 , 失败数量: 0 , 完成时间：未完</p>
				</a></li>
			</ul> -->

			<ul data-role="listview" id="sended" data-divider-theme="b"
				data-inset="true">
				<li data-role="list-divider" role="heading">已发送的任务</li>
			</ul>
			<ul data-role="listview" id="faild" data-divider-theme="b"
				data-inset="true">
				<li data-role="list-divider" role="heading">发送失败的任务</li>
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