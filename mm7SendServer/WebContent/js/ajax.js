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
 * 获取work列表
 * @cod 100
 * @param start
 * @param count
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
			PassWord : opts.PassWord,
			grade : opts.grade
		}
	}).send(opts.before);
};


