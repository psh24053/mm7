<%@page import="cn.server.bean.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
<%
	String id = request.getParameter("id");
	
	String indexStr = "";
	String createSendTaskStr = "";
	String showSendTaskStr = "";
	String showFailListStr = "";
	String modifyPasswordStr = "";
	String userManagerStr = "";
	String createSmcTaskStr = "";
	String showSmcTaskStr = "";
	String showSmcFailListStr = "";
	if(id == null){
		indexStr = "data-theme=\"b\"";
	}else if(id.equals("index")){
		indexStr = "data-theme=\"b\"";
	}else if(id.equals("createsendtask")){
		createSendTaskStr = "data-theme=\"b\"";
	}else if(id.equals("showsendtask")){
		showSendTaskStr = "data-theme=\"b\"";
	}else if(id.equals("showfaillist")){
		showFailListStr = "data-theme=\"b\"";
	}else if(id.equals("modifypassword")){
		modifyPasswordStr = "data-theme=\"b\"";
	}else if(id.equals("usermanager")){
		userManagerStr = "data-theme=\"b\"";
	}else if(id.equals("createsmctask")){
		createSmcTaskStr = "data-theme=\"b\"";
	}else if(id.equals("showsmctask")){
		showSmcTaskStr = "data-theme=\"b\"";
	}else if(id.equals("showsmcfaillist")){
		showSmcFailListStr = "data-theme=\"b\"";
	}

%>	
	
<div class="content_menu">
	<div data-role="collapsible" data-collapsed="false">
		<h3>操作菜单</h3>
		<ul data-role="listview" data-theme="c" data-dividertheme="d">
			<li <%=indexStr %>><a href="index.jsp">首页</a></li>
			<li data-role="list-divider" role="heading">彩信功能</li>
			<li <%=createSendTaskStr %>><a href="createsendtask.jsp">创建彩信任务</a></li>
			<li <%=showSendTaskStr %>><a href="showsendtask.jsp">查看彩信任务</a></li>
			<%-- <li <%=showFailListStr %>><a href="showfaillist.jsp">查看失败号码</a></li> --%>
			<li data-role="list-divider" role="heading">短信功能</li>
			<li <%=createSmcTaskStr %>><a href="createsmctask.jsp">创建短信任务</a></li>
			<li <%=showSmcTaskStr %>><a href="showsmctask.jsp">查看短信任务</a></li>
			<%-- <li <%=showSmcFailListStr %>><a href="showsmcfaillist.jsp">查看失败号码</a></li> --%>
			<li data-role="list-divider" role="heading">系统功能</li>
			<% 
				User user = (User) session.getAttribute("User");
				if(user != null && user.getGrade() == 3){
					%>
					<li <%=userManagerStr %>><a href="usermanager.jsp">用户管理</a></li>	
					<%
				}
			%>
			
			<li <%=modifyPasswordStr %>><a href="modifypassword.jsp">修改密码</a></li>
		</ul>
	</div>
</div>