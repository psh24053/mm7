<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ include file="header.jsp"%>
<div data-role="page" id="showfaillist" data-theme="b">
	<script type="text/javascript">
$('#showfaillist').bind('pagecreate', function(){
	// 如果当前浏览器不支持，则停止执行
	if(!browserSupport()){
		alert('请使用以下浏览器进行浏览（Chrome20+,FireFox10+,IE9+,360浏览器6.0+）');
	}else{
		
		//判断是否登录
		if(isLogin()){
			initEvent_ShowFailList();
		}else{
			$.mobile.changePage('login.jsp');
			
		}
		
	}
});
/**
 * 初始化事件
 */
function initEvent_ShowFailList(){
	$('.logout_button').on('tap', function(event){
		logout();
	});
}
</script>
	<div data-role="header">
		<h1>查看短信失败号码</h1>
		<a data-role="button" class="logout_button" data-icon="back"
			href="login.jsp" data-iconpos="left" class="ui-btn-left">退出 </a>
	</div>


	<div data-role="content">

		<jsp:include page="menu.jsp">
			<jsp:param value="showsmcfaillist" name="id"/>
		</jsp:include>

		<div class="content_main">

			<ul data-role="listview" id="faildnumber" data-divider-theme="b"
				data-inset="true">
				<li data-role="list-divider" role="heading">任务a (任务ID：4 , 失败数量:
					2700 , 创建时间：2013-07-28 18:38:22 ,完成时间：2013-07-28 19:22:12)</li>
				<li data-theme="c"><a class="ui-link-inherit"
					style="height: 100%; white-space: normal;">
						13888881111,13888881111,13888881111,13888881111,13888881111,13888881111,13888881111,13888881111,13888881111,13888881111,13888881111,13888881111,13888881111,13888881111,13888881111

				</a></li>
			</ul>

		</div>



	</div>

	<div data-role="footer" data-theme="c">
		<p style="text-align: center">By Panshihao.Cn</p>
	</div>


</div>

<%@ include file="footer.jsp"%>