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
