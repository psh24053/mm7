/**
 * Ajax工具类
 */
function Ajax(Settings){
	
	this.Servlet = "Main";
	this.ActionCode = " ";
	this.Method = "post";
	this.prm = {};
	this.Success = function(){};
	this.Error = function(){};
	this.Complete = function(){};
	this.dataType = "json";
	this.data = null;
	this.async = true;
	
	if(Settings){
		for(var key in Settings){
			if(this[key]){
				this[key] = Settings[key];
			}
		}
	}
	for(var key in this.prm){
		if(this.prm[key] == 'null'){
			delete this.prm.key;
		}
	}
	this.RequestJson = {
		cod: this.ActionCode,
		prm: this.prm
	};
	
	var p_this = this;
	
	this.send = function(run){
		if(run != undefined){
			run();
		}
		$.ajax({
			type: this.Method,
			async: this.async,
			url: this.Servlet,
			data: JSON.stringify(this.RequestJson),
			success: function(data){
				this.data = data;

				p_this.Success(data);
				
			},
			dataType: this.dataType
		}).error(this.Error).complete(this.Complete);
	};
	this.set = function(key,value){
		if(this[key]){
			this[key] = value;
		}
	};
	
	
}
var ajax = new Object();
/**
 * 用户登录
 * @cod 101
 * @param UserName
 * @param PassWord
 * @param success
 * @param error
 * @param before
 */
ajax.action_101_userlogin = function(opts){
	
	new Ajax({
		ActionCode: 101,
		Success: opts.success,
		Error: opts.error,
		prm: {
			UserName : opts.UserName,
			PassWord : opts.PassWord
		}
	}).send(opts.before);
};
/**
 * 用户退出
 * @cod 102
 * @param success
 * @param error
 * @param before
 */
ajax.action_102_userlogin = function(opts){
	
	new Ajax({
		ActionCode: 102,
		Success: opts.success,
		Error: opts.error
	}).send(opts.before);
};
/**
 * 修改密码
 * @cod 103
 * @param PassWord
 * @param NewPassWord
 * @param success
 * @param error
 * @param before
 */
ajax.action_103_modifypassword = function(opts){
	
	new Ajax({
		ActionCode: 103,
		Success: opts.success,
		Error: opts.error,
		prm: {
			PassWord: opts.PassWord,
			NewPassWord: opts.NewPassWord
		}
	}).send(opts.before);
};
/**
 * 获取用户列表
 * @cod 104
 * @param success
 * @param error
 * @param before
 */
ajax.action_104_getuserlist = function(opts){
	
	new Ajax({
		ActionCode: 104,
		Success: opts.success,
		Error: opts.error
	}).send(opts.before);
};

/**
 * 增加用户
 * @cod 105
 * @param UserName
 * @param PassWord
 * @param grade
 * @param success
 * @param error
 * @param before
 */
ajax.action_105_adduser = function(opts){
	
	new Ajax({
		ActionCode: 105,
		Success: opts.success,
		Error: opts.error,
		prm: {
			UserName: opts.UserName,
			PassWord: opts.PassWord,
			grade: opts.grade
		}
	}).send(opts.before);
};
/**
 * 删除用户
 * @cod 106
 * @param UserID
 * @param success
 * @param error
 * @param before
 */
ajax.action_106_deleteuser = function(opts){
	
	new Ajax({
		ActionCode: 106,
		Success: opts.success,
		Error: opts.error,
		prm: {
			UserID: opts.UserID
		}
	}).send(opts.before);
};
/**
 * 获取发送任务列表
 * @cod 107
 * @param PageNum
 * @param CountLimit
 * @param success
 * @param error
 * @param before
 */
ajax.action_107_getsendtasklist = function(opts){
	
	new Ajax({
		ActionCode: 107,
		Success: opts.success,
		Error: opts.error,
		prm: {
			PageNum: opts.PageNum,
			CountLimit: opts.CountLimit
		}
	}).send(opts.before);
};
/**
 * 创建彩信任务
 * @cod 108
 * @param name
 * @param subject
 * @param NumberCount
 * @param CustomNumber
 * @param content
 * @param success
 * @param error
 * @param before
 */
ajax.action_108_newsendtask = function(opts){
	
	new Ajax({
		ActionCode: 108,
		Success: opts.success,
		Error: opts.error,
		prm: {
			name: opts.name,
			subject: opts.subject,
			NumberCount: opts.NumberCount,
			CustomNumber: opts.CustomNumber,
			content: opts.content
		}
	
	}).send(opts.before);
};
/**
 * 获取彩信失败号码列表
 * @cod 109
 * @param PageNum
 * @param CountLimit
 * @param ID
 * @param success
 * @param error
 * @param before
 */
ajax.action_109_getfailnumberlist = function(opts){
	
	new Ajax({
		ActionCode: 109,
		Success: opts.success,
		Error: opts.error,
		prm: {
			PageNum: opts.PageNum,
			CountLimit: opts.CountLimit,
			ID: opts.ID
		}
	}).send(opts.before);
};
/**
 * 获取彩信任务内容
 * @cod 110
 * @param ID
 * @param success
 * @param error
 * @param before
 */
ajax.action_110_getcontentbytaskid = function(opts){
	
	new Ajax({
		ActionCode: 110,
		Success: opts.success,
		Error: opts.error,
		prm: {
			ID: opts.ID
		}
	}).send(opts.before);
};
/**
 * 获取短信任务列表
 * @cod 111
 * @param ID
 * @param success
 * @param error
 * @param before
 */
ajax.action_111_getsmctasklist = function(opts){
	
	new Ajax({
		ActionCode: 111,
		Success: opts.success,
		Error: opts.error,
		prm: {
		}
	}).send(opts.before);
};