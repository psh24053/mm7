<%@page import="cn.server.dao.DBNumber"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ include file="header.jsp"%>

<div data-role="page" id="main" data-theme="b">
	<script type="text/javascript">
		$('#main').bind('pagecreate', function() {
			// 如果当前浏览器不支持，则停止执行
			if (!browserSupport()) {
				alert('请使用以下浏览器进行浏览（Chrome20+,FireFox10+,IE9+,360浏览器6.0+）');
			} else {
				//判断是否登录
				if (isLogin()) {
					initEvent_index();
				} else {
					$.mobile.changePage('login.jsp');
				}

			}

		});

		/**
		 * 初始化Index界面的 事件
		 */
		function initEvent_index() {
			$('.logout_button').on('tap', function(event) {
				logout();
			});
		}
	</script>
	<div data-role="header">
		<h1>首 页</h1>
		<a data-role="button" class="logout_button" data-icon="back"
			href="login.jsp" data-iconpos="left" class="ui-btn-left">退出 </a>
	</div>


	<div data-role="content">

		<jsp:include page="menu.jsp">
			<jsp:param value="index" name="id"/>
		</jsp:include>

		<div class="content_main ui-bar-c ">

			<h2>欢迎使用 联通彩信发送管理系统</h2>
			
			<h4>当前系统中共有：
			<% 
			
			DBNumber db = new DBNumber();
			out.print(db.getAllCount());
			%>
			个号码</h4>

		</div>



	</div>


	<div data-role="footer" data-theme="c">
		<p style="text-align: center">By Panshihao.Cn</p>
	</div>

</div>

<%@ include file="footer.jsp"%>
