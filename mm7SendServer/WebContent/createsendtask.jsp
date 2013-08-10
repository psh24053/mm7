<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ include file="header.jsp"%>

<div data-role="page" id="createsendtask" data-theme="b">
	<script type="text/javascript">
$('#createsendtask').bind('pagecreate', function(){
	// 如果当前浏览器不支持，则停止执行
	if(!browserSupport()){
		alert('请使用以下浏览器进行浏览（Chrome20+,FireFox10+,IE9+,360浏览器6.0+）');
	}else{
		
		//判断是否登录
		if(isLogin()){
			initEvent_CreateSendTask();
			initUI();
		}else{
			$.mobile.changePage('login.jsp');
			
		}
		
	}
});
function initUI(){
	$('#content').listview( {countTheme:'b'});

}
/**
 * 初始化事件
 */
function initEvent_CreateSendTask(){
	$('.logout_button').on('tap', function(event){
		logout();
	});
	$('#create_addtext').on('tap', function(event){
		$('<div>').simpledialog2({
		    mode: 'button',
		    width: '50%',
		    headerText: '增加文本内容',
		    headerClose: true,
		    buttonPrompt: '请输入文本的内容',
		    buttonInput: true,
		    buttons : {
		      '增加': {
		        click: function () { 
		          var inputContent = $.mobile.sdLastInput;
		          if(inputContent == undefined || inputContent.length == 0){
		        	  alert('文本内容不能为空');
		        	  return;
		          }else{
		        	  addText(inputContent);
		          }
		          
		        }
		      },
		      '取消': {
		        click: function () { 
		          
		        },
		        icon: "delete",
		        theme: "c"
		      }
		    }
		  });
	});
	
	$('#create_addimage').on('tap', openUploadImage);
	
	
	$('#mms_preview').on('tap', function(){
		var value = $(this).find('span .ui-btn-text').text();
		if(value == '编辑模式'){
			$(this).find('span .ui-btn-text').text('预览模式');
			$('#content').css('width','');
		}else{
			$(this).find('span .ui-btn-text').text('编辑模式');
			$('#content').css('width','360px');
		}
	});
	
	
	$('#create_button').on('tap', createTask);
	
}

/**
 * 上传完成响应
 */
function uploadImageComplete(idx, filename){
	$.mobile.loading('hide');
	addImage('FileDownload?filename='+filename,filename);		 
}
/**
 * 打开上传image窗口
 */
function openUploadImage(event){
	
	
	var uploadDIV = $('#upload_div');
	
	var idx = random_char(8);
	
	uploadDIV.find('form').attr('action','FileUpload?idx='+idx);
	
	
	
	$('<div>').simpledialog2({
	    mode: 'button',
	    width: '50%',
	    headerText: '增加图片内容',
	    headerClose: true,
	    buttonPrompt: uploadDIV.html(),
	    buttons : {
	      '取消': {
	        click: function () { 
	          
	        },
	        icon: "delete",
	        theme: "c"
	      }
	    }
	  });
}

function upload_submit(){
	if($('#upload_div').find('form').find('input[type=file]').val() == undefined){
		alert('请选择文件');
	}else{
		// 显示loading动画
		$.mobile.loading( 'show', {
			  text: '正在加载...',
			  textVisible: true,
			  theme: 'a',
			  textonly: false,
			  html: ''
		});
		$.mobile.sdCurrentDialog.close();
	}
}
/**
 * 增加文本内容
 */
function addText(Str){
	
	var content = $('#content');
	
	var item = $('<li/>');
	item.attr('data-theme', 'c');
	item.addClass('task_text_item');
	item.data('text', Str);
	var itemA = $('<a/>');
	itemA.css('white-space', 'normal');
	itemA.css('word-break', 'break-all');
	itemA.css('word-wrap', 'break-word');
	itemA.append('<span class="content_Str">'+Str+'</span>');
	
	item.append(itemA);
	
	content.append(item);
	content.listview( "refresh" );

	item.on('tap', function(){
		openTextItemDialog(this, content, itemA.find('.content_Str').text());
	});
	
}
/**
 * 打开text内容的dialog
 */
function openTextItemDialog(obj, main, str){
	$('<div>').simpledialog2({
	    mode: 'button',
	    width: '50%',
	    headerText: '编辑内容',
	    headerClose: true,
	    buttonPrompt: '请修改内容',
	    buttonInput: true,
	    buttonInputDefault: str,
	    buttons : {
	      '保存修改': {
	        click: function () { 
	        	 var inputContent = $.mobile.sdLastInput;
		          if(inputContent == undefined || inputContent.length == 0){
		        	  alert('文本内容不能为空');
		        	  return;
		          }else{
		        	  $(obj).find('.content_Str').text(inputContent);
		        	  $(obj).data('text', inputContent);
		          }
	        }
	      },
	      '删除该内容': {
	        click: function () {
	        	$(obj).remove();
	        	main.listview('refresh');
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
/**
 * 打开image内容的dialog
 */
function openImageItemDialog(obj, main, url){
	
	$('<div>').simpledialog2({
	    mode: 'button',
	    width: '50%',
	    headerText: '编辑内容',
	    headerClose: true,
	    buttonPrompt: '请选择操作',
	    buttons : {
	      '删除该内容': {
	        click: function () {
	        	$(obj).remove();
	        	main.listview('refresh');
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
/**
 * 增加图片内容
 */
 
function addImage(Url, fid){
	var content = $('#content');
	
	var item = $('<li/>');
	item.attr('data-theme', 'c');
	item.addClass('task_image_item');
	item.data('url',Url);
	item.data('fid', fid);
	var itemA = $('<a/>');
	itemA.css('white-space', 'normal');
	itemA.css('word-break', 'break-all');
	itemA.css('word-wrap', 'break-word');
	itemA.attr('href', '#');
	itemA.append('<img class="content_img" src="'+Url+'" style="border: 1px #666666 solid;max-width: 500px;" />');
	
	item.append(itemA);
	
	content.append(item);
	content.listview( "refresh" );
	
	item.on('tap', function(){
		openImageItemDialog(this, content, Url);
	});
}
/**
 * 创建任务
 */
function createTask(){
	
	var name = $('#name').val();
	var NumberCount = $('#NumberCount').val();
	var CustomNumber = $('#CustomNumber').val();
	var subject = $('#subject').val();
	
	
	if(name == undefined || name.length == 0){
		alert('任务名称不能为空');
		return;
	}
	if(subject == undefined || subject.length == 0){
		alert('彩信主题不能为空');
		return;
	}
	if(NumberCount == undefined || NumberCount.length == 0){
		alert('接收号码总数不能为空');
		return;
	}
	
	var customlist = CustomNumber.split(',');
	var taskTextList = $('#content .task_text_item');
	var taskImageList = $('#content .task_image_item');
	
	if(taskTextList.size() == 0 && taskImageList.size() == 0){
		alert('至少需要一条内容');
		return;
	}
	var content = new Array();
	
	var index = 0;
	
	taskTextList.each(function(){
		
		var Str = $(this).data('text');
		
		var item = new Object();
		item.Type = 1;
		item.Text = Str;
		item.FilePath = Str;
		item.sort = index;
		index++;
		
		content.push(item);
	});
	taskImageList.each(function(){
		
		var url = $(this).data('url');
		var fid = $(this).data('fid');
		
		var item = new Object();
		item.Type = 2;
		item.Text = fid;
		item.FilePath = fid;
		item.sort = index;
		index++;
		
		content.push(item);
	});
	// 显示loading动画
	$.mobile.loading( 'show', {
		  text: '正在加载...',
		  textVisible: true,
		  theme: 'a',
		  textonly: false,
		  html: ''
	});
	
	ajax.action_108_newsendtask({
		name: name,
		subject: subject,
		NumberCount: NumberCount,
		CustomNumber: customlist,
		content: content,
		success: function(data){
			$.mobile.loading('hide');
			if(data.res){
				console.debug(data);
				alert('创建成功！');				
			}else{
				alert(data.pld.errorMsg);
			}
		},
		error: function(){
			$.mobile.loading('hide');
			alert('请求失败..');
		}
		
	});
	
	
	
}
</script>
	<div data-role="header">
		<h1>创建彩信任务</h1>
		<a data-role="button" class="logout_button" data-icon="back"
			href="login.jsp" data-iconpos="left" class="ui-btn-left">退出 </a>
	</div>


	<div data-role="content">

		<jsp:include page="menu.jsp">
			<jsp:param value="createsendtask" name="id"/>
		</jsp:include>

		<div class="content_main">

			<form action="">
				<div data-role="fieldcontain">
					<label for="name"> 任务名称 </label> <input name="" id="name"
						placeholder="" value="" type="text">
				</div>
				<div data-role="fieldcontain">
					<label for="subject"> 彩信主题 </label> <input name="" id="subject"
						placeholder="" value="" type="text">
				</div>
				<div data-role="fieldcontain">
					<label for="NumberCount"> 接收号码总数 </label> <input name=""
						id="NumberCount" placeholder="" value="1000" type="number">
				</div>
				<div data-role="fieldcontain">
					<label for="CustomNumber"> 自定义号码 </label> <input name=""
						id="CustomNumber" placeholder="" value="123,456,789" type="text">
				</div>


				<a id="create_addtext" data-icon="plus" data-role="button"
					data-inline="true"> 文本内容 </a> <a data-role="button"
					data-inline="true" id="create_addimage" data-icon="plus"
					data-iconpos="left"> 图片内容 </a> <a data-role="button"
					data-inline="true" id="mms_preview" data-icon="star"
					data-iconpos="left"> 预览模式 </a>

				<ul data-role="listview" id="content" data-divider-theme="b"
					data-inset="true">
					<li data-role="list-divider" role="heading">彩信内容</li>
				</ul>





				<a data-role="button" id="create_button">发送任务 </a>
			</form>


		</div>



	</div>

	<div data-role="footer" data-theme="c">
		<p style="text-align: center">By Panshihao.Cn</p>
	</div>


</div>


<!-- <div data-role="page" id="addText_dialog" >
		<div data-role="header">
			<h2>增加文本内容</h2>
		</div>
		<div data-role="content">
			<p>I am a dialog</p>
		</div>
	</div>
	 -->
	 
<div id="upload_div" style="display: none;">
	<form action="FileUpload" enctype="MULTIPART/FORM-DATA" onSubmit="parent.upload_submit();" target="post_frame" method="POST">
			<iframe name="post_frame" id="post_frame" style="display:none;" mce_style="display:none;"></iframe>
			<input name="FilePath" type="file" value="选择文件"/>
			<input id="upload_submit"  type="submit" value="上传"/>
	</form>
</div>	 


<%@ include file="footer.jsp"%>