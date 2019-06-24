if(typeof(FA) == 'undefined'){
	var FA = new Object();
}
FA.utils = new Object();
FA.ui = new Object();
FA.cache = new Object();

/**
 * FA模板渲染
 * @Param	templete:模板    data:数据
 * @return  渲染后的html
 */
FA.utils.tpl = function(templete , data){
	if(typeof(laytpl) == 'undefined'){
		alert("无法找到FATPL类，请先加载fatpl.js");
		return;
	}
	var body = templete.replace(/<script\b[^<]*(?:(?!<\/script>)<[^<]*)*<\/script>/gi, "");
	body = laytpl(body).render(data);
	var patt1=new RegExp(/<script\b[^<]*(?:(?!<\/script>)<[^<]*)*<\/script>/gi);
	var script = patt1.exec(templete);
	return body + (FA.utils.isEmpty(script) ? "" : script);
	//return laytpl(templete).render(data);
}

FA.utils.objIs = function(obj,type) { 
	return (type === "Null" && obj === null) || 
	(type === "Undefined" && obj === void 0 ) || 
	(type === "Number" && FA.utils.isNumber(obj)) || 
	Object.prototype.toString.call(obj).slice(8,-1) === type; 
}

/**
 * FA去除空格
 */
FA.utils.trim = function(v){
	return typeof(v) == "string" ? v.replace(/^\s*|\s*$/g, "") : v;
}

/**
 * FA判断是否为邮箱
 */
FA.utils.isEmail = function(v){
	return FA.utils.isEmpty(v) ? true : /([\w-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([\w-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\]?)/.test(v);
}

/**
 * FA判断是否为整数
 */
FA.utils.isInt = function(v){
	return FA.utils.isEmpty(v) ? true : /^-?[1-9]\d*$/.test(v);
}

FA.utils.isNumber = function(v){
	return FA.utils.isEmpty(v) ? true : /^[-+]?\d+(\.\d+)?$/.test(v);
}

/**
 * FA判断是否为电话
 * 只允许使用数字-空格等
 */
FA.utils.isTel = function(v){
	return FA.utils.isEmpty(v) ? true : /^[\d|\-|\s|\_]+$/.test(v);
}

/**
 * FA判断手机号格式
 */
FA.utils.isMobile = function(v){
	return FA.utils.isEmpty(v) ? true : /^1[34578][0-9]{9}$|14[0-9]{9}|15[0-9]{9}$|18[0-9]{9}$/.test(v);
}

/**
 * FA判断身份证号
 */
FA.utils.isIdCard = function(gets){
	if(FA.utils.isEmpty(gets)) return true;
	if(gets.length != 15 && gets.length != 18) return false;
	if(gets.length==18){
        var idCardWi=new Array( 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 ); 
        var idCardY=new Array( 1, 0, 10, 9, 8, 7, 6, 5, 4, 3, 2 ); 
        var idCardWiSum=0; 
        for(var i=0;i<17;i++){
            idCardWiSum+=gets.substring(i,i+1)*idCardWi[i];
        }
        var idCardMod=idCardWiSum%11;
        var idCardLast=gets.substring(17);
        if(idCardMod==2){
        	return idCardLast=="X"||idCardLast=="x" ? true : false;
        }else{
        	return idCardLast==idCardY[idCardMod] ? true : false;
        }
    }
    if(gets.length==15){
        var year =  gets.substring(6,8);
        var month = gets.substring(8,10);
        var day = gets.substring(10,12);
        var temp_date = new Date(year,parseFloat(month)-1,parseFloat(day));
        return temp_date.getYear()!=parseFloat(year)||temp_date.getMonth()!=parseFloat(month)-1||temp_date.getDate()!=parseFloat(day) ? false : true;
    }
}

/**
 * FA 创建UUID
 */
FA.utils.uuid = function(){
	return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
        var r = Math.random()*16|0, v = c == 'x' ? r : (r&0x3|0x8);
        return v.toString(16);
    });
}


/**
 * FA获取最高zindex
 */
FA.utils.maxZindex = function(){
	return Math.max.apply(null,$.map($('body  *'), function(e,n){
             return parseInt($(e).css('z-index'))||1 ;
        })
	);
}

/**
 * 获取Cookie
 */
FA.utils.getCookie = function(name){
	var arr = document.cookie.match(new RegExp("(^| )"+name+"=([^;]*)(;|$)"));  
    if(arr != null){  
     return unescape(arr[2]);   
    }else{  
     return null;  
    }  
}

/**
 * 设置cookie
 */
FA.utils.setCookie = function(name , value , Days , path){
	if(FA.utils.isEmpty(Days) && Days != 0){
		Days = 30*12;   //cookie 将被保存一年 
	}
	var exp  = new Date();  //获得当前时间  
    exp.setTime(exp.getTime() + Days*24*60*60*1000);  //换成毫秒 
    var expires = exp.toGMTString();
	if(Days <= 0){
		expires = 'Session';
	}
     
	var cookies = name + "="+ escape (value) + ";expires=" + expires; 
	if(!FA.utils.isEmpty(path)){
		cookies += "; path=" + path;
	}
    document.cookie = cookies;
}

FA.utils.delCookie = function(name){
	var exp = new Date();  //当前时间  
    exp.setTime(exp.getTime() - 1);  
    var cval=FA.utils.getCookie(name);  
    if(cval!=null) document.cookie= name + "="+cval+";expires="+exp.toGMTString();  
}

/**
 * 判断对象是否为数组
 * @author zhong
 * @param	object:对象
 */
FA.utils.isArray = function(object){
	return object && typeof object==='object' && Array == object.constructor;
}

/**
 * FA 判断对象是否为空
 * @author zhong
 * @param	obj:对象
 */
FA.utils.isEmpty = function(obj){
	if(typeof(obj) == 'undefined' || obj == null || obj === ''){
		return true;
	}
	if(FA.utils.isArray(obj) && obj.length == 0){
		return true;
	}
}

FA.utils.getCurrentUrl = function(){
	var locationstring = location.href.replace(location.host , '').replace(location.protocol + '//' , '').replace(contextPath , '');
	var locationstring = locationstring.substr(0 , locationstring.indexOf('?') > 0 ? locationstring.indexOf('?') : locationstring.length);
	return 	locationstring;
}

/**
 * FA消息提示框，2秒后自动消失
 * @Param	msg:消息    fn:消息消失后回调方法
 * @return
 */
FA.utils.msg = function(msg , fn){
	layer.msg(msg ,{
		time : 3000
	} , function(){
		if(typeof(fn) == 'function'){
			fn();
		}
	})
}



/**
 * FA的alert弹出框
 * @Param	msg:消息    fn:点击确定的回调方法
 * @return
 */
FA.utils.alert = function(msg , fn){
	var layindex = layer.alert(msg, function(index){
	  if(typeof(fn) == 'function'){
		fn();
	  }
	  layer.close(index);
	});
	setTimeout(function(){
		$("#layui-layer"+layindex+" .layui-layer-btn0").focus();
	},300);
}



/**
 * FA的confirm弹出框
 * @Param	msg:消息    fn:点击确定的回调方法    cfn:点取消的回调方法
 * @return
 */
FA.utils.confirm = function(msg , fn , cfn){
	var layindex = layer.confirm(msg, function(index){
	  if(typeof(fn) == 'function'){
		fn();
	  }
	  layer.close(index);
	},function(index){
		if(typeof(cfn) == 'function'){
			cfn();
		}
	}); 
	setTimeout(function(){
		$("#layui-layer"+layindex+" .layui-layer-btn0").focus();
	},300);
}


/**
 * FA的消息通知
 * @Param	title:消息标题    msg:消息内容    fn:显示消息时的回调方法
 * @return
 */
FA.utils.gritter = function(title , msg , fn){
	var unique_id = $.gritter.add({
		title: title,
		text: msg,
		sticky: false,
		time: '3000',
		class_name: 'gritter-info'
	});
	if(typeof(fn) == 'function'){
		fn();
	}
}


/**
 * FA 异步加载select框
 * @param options
 * 			render				渲染对象
 * 			allowClear			是否可以清空
 * 			id					控件ID
 * 			name				控制name
 * 			class				控件样式
 * 			placeholder			控件空值的提示文案
 * 			url					检索URL
 * 			tplfn				option的样式回调方法
 * 			selectfn			选择某个对象时的样式回调方法
 * 			onchange			控件值改变时触发的方法
 * 
 * 
 */
FA.utils.ajaxSelect = function(options){
	
	var settings = {
			'render' : '',
			'allowClear' : true ,
			'allowEmpty' : true,
			'id' : '',
			'name' : '',
			'css' : 'width-100',
			'placeholder' : '',
			'url':'',
			'multiple': false,
			'keymap' : {'id':'id','text':'text'},
			'tplfn' : function(repo){ return repo.text; },
			'selectfn' : function(repo) { return repo.text; },
			'onchange' : '',
			'onclear' : '',
			'data-edit-auth' : null,
			'data-unedit-auth' : null,
			'data-user-auth' : null
	};
	
	var obj = new Object();
	
	options =  $.extend( settings, options);
	if(FA.utils.isEmpty(options['render']))
		alert('render不能为空');
	
	if(FA.utils.isEmpty(options['id']))
		options['id'] = FA.utils.uuid();
	
	var multipletemp = options['multiple'] ? 'multiple="multiple"' : '';
	
	var selecttpl = '<select  id="{{d.id}}" name="{{d.name}}" class="select2 {{d.css}}" '+multipletemp+' data-placeholder="{{d.placeholder}}">{{#if(d.allowEmpty){ }} <option value=""></option> {{#} }}</select>';
	var selecthtml = FA.utils.tpl(selecttpl, options);
	
	$("#"+options['render']).html(selecthtml);
	
	var dom = $("#" + options['id']);
	if(options['data-edit-auth']){
		dom.attr('data-edit-auth',options['data-edit-auth']);
	}
	if(options['data-user-auth']){
		dom.attr('data-user-auth',options['data-user-auth']);
	}
	if(options['data-unedit-auth']){
		dom.attr('data-unedit-auth',options['data-unedit-auth']);
	}
	
	dom.select2({
		ajax: {
		    url: options['url'],
		    type : 'post',
		    dataType: 'json',
		    data: function (params) {
		      return {
		    	keyword: params.term,
		        page: params.page
		      };
		    },
		    processResults: function (data, params) {
		      params.page = params.page || 1;
		      if(typeof(options['keymap']) != 'undefined' && typeof(options['keymap']) != null ){
					var resultdata = data.result;
					var selectData = $.map(resultdata , function (obj) {
						  obj.id = obj[options.keymap['id']] ||  obj.id; 
						  obj.text = obj[options.keymap['text']] || obj.text; 
						  return obj;
						});
				}
		      return {
		        results: selectData
		      };
		    },
		    cache: true
		  },
		  allowClear : options['allowClear'],
		  escapeMarkup: function (markup) { return markup; },
		  minimumInputLength: 1,
		  templateResult: options['tplfn'],
		  templateSelection: options['selectfn'] 
	});
	if(typeof(options['onchange']) == 'function'){
		dom.on('select2:select',function(a,b){
			options['onchange'](FA.utils.isEmpty(a.params) ? b : a.params.data);
		})
	}
	if(typeof(options['onclear']) == 'function'){
		dom.on('select2:unselect',function(a,b){
			dom.trigger('select2:select',{});
		})
	}
	
	return dom;
}



/**
 * FA 异步加载select框
 * @param options
 * 			render				渲染对象
 * 			allowClear			是否可以清空
 * 			id					控件ID
 * 			name				控制name
 * 			class				控件样式
 * 			placeholder			控件空值的提示文案
 * 			data				下拉选择选项
 * 			tplfn				option的样式回调方法
 * 			selectfn			选择某个对象时的样式回调方法
 * 			onchange			控件值改变时触发的方法
 * 
 * 
 */
FA.utils.select2 = function(options){
	
	var settings = {
			'render' : '',
			'allowClear' : true ,
			'allowEmpty' : true,
			'id' : '',
			'name' : '',
			'css' : 'width-100',
			'placeholder' : '',
			'data':'',
			'multiple': false,
			'defaultValue':'',
			'matcher' : '',
			'tplfn' : function(repo){ return repo.text; },
			'selectfn' : function(repo) { return repo.text; },
			'onchange' : '',
			'onclear' : '',
			'readonly':false,
			'data-edit-auth' : null,
			'data-unedit-auth' : null,
			'data-user-auth' : null
	};
	
	var obj = new Object();
	
	options =  $.extend( settings, options);
	if(FA.utils.isEmpty(options['render']))
		alert('render不能为空');
	
	if(FA.utils.isEmpty(options['id']))
		options['id'] = FA.utils.uuid();
	
	var multipletemp = options['multiple'] ? 'multiple="multiple"' : '';
	
	var selecttpl = '<select  id="{{d.id}}" name="{{d.name}}" class="select2 {{d.css}}" ' + multipletemp + ' data-placeholder="{{d.placeholder}}">{{#if(d.allowEmpty){ }} <option value=""></option> {{#} }}</select>';
	var selecthtml = FA.utils.tpl(selecttpl, options);
	
	$("#"+options['render']).html(selecthtml);
	var dom = $("#" + options['id']);
	if(options['data-edit-auth']){
		dom.attr('data-edit-auth',options['data-edit-auth']);
	}
	if(options['data-user-auth']){
		dom.attr('data-user-auth',options['data-user-auth']);
	}
	if(options['data-unedit-auth']){
		dom.attr('data-unedit-auth',options['data-unedit-auth']);
	}
	//判断是否有值
	if(!FA.utils.isEmpty(options['data']) && !FA.utils.isEmpty(options['defaultValue']) && FA.utils.objIs(options['defaultValue'],'Object')){
		var hasvalue = false;
		$.each(options['data'],function(){
			if(this.id == options['defaultValue']['id']){
				hasvalue = true;
				return false;
			}
		})
		if(!hasvalue)
			options['data'].unshift(options['defaultValue']);
	}
	if(typeof(options['matcher']) == 'function'){
		$.fn.select2.amd.require(['select2/compat/matcher'], function (oldMatcher) {
			dom.select2({
			  data : options['data'],	
			  allowClear : options['allowClear'],
			  escapeMarkup: function (markup) { return markup; },
			  templateResult: options['tplfn'],
			  templateSelection: options['selectfn'] ,
		      matcher: oldMatcher(options['matcher'])
		  })
		});
	}else{
		dom.select2({
			  data : options['data'],	
			  allowClear : options['allowClear'],
			  escapeMarkup: function (markup) { return markup; },
			  templateResult: options['tplfn'],
			  templateSelection: options['selectfn'] 
		});
	}
	if(options['readonly']){
		FA.utils.setReadonly(dom[0]);
	}
	
	
	if(typeof(options['onchange']) == 'function'){
		dom.on('select2:select',function(a,b){
			options['onchange'](FA.utils.isEmpty(a.params) ? b : a.params.data);
		})
	}
	
	//如果没有设默认值
	if(!FA.utils.isEmpty(options['defaultValue'])){
		if(options['multiple']){
			dom.val(options['defaultValue'].split(','));
		}else{
			if(FA.utils.objIs(options['defaultValue'],'Object')){
				dom.val(options['defaultValue']['id']);
			}else{
				dom.val(options['defaultValue']);
			}
		}
		dom.trigger('change');
		var selectdom ;
		$.each(options['data'],function(index , value){
			if(value.id == options['defaultValue']){
				selectdom = value;
			}
		})
		if(FA.utils.isEmpty(selectdom)){
			dom.trigger('select2:select',{});
		}else{
			dom.trigger('select2:select',selectdom);
		}
	}else{
		if(!options['allowEmpty']){
			if(!FA.utils.isEmpty(options['data'])){
				dom.val(options['data'][0]['id']);
				dom.trigger('change');
				dom.trigger('select2:select',options['data'][0]);
			}else{
				dom.trigger('select2:select',{});
			}
		}else{
			dom.trigger('select2:select',{});
		}
	}
	
	dom.on('select2:unselect',function(a,b){
		dom.trigger('select2:select',{});
	})
	
	
	return dom;
}

FA.utils.select2.clear = function(id){
	$("#" + id).val('').trigger('change')
}


/**
 * FA 异步加载select框
 * @param options
 * 			render				渲染对象
 * 			allowClear			是否可以清空
 * 			id					控件ID
 * 			name				控制name
 * 			class				控件样式
 * 			placeholder			控件空值的提示文案
 * 			url					获取数据地址
 * 			keymap				返回值所对应的ID，TEXT的字段名
 * 			tplfn				option的样式回调方法
 * 			selectfn			选择某个对象时的样式回调方法
 * 			onchange			控件值改变时触发的方法
 * 
 * 
 */
FA.utils.syncSelect2 = function(options){
	
	var settings = {
			'render' : '',
			'allowClear' : true ,
			'allowEmpty' : true,
			'id' : '',
			'name' : '',
			'css' : 'width-100',
			'placeholder' : '',
			'url':'',
			'multiple': false,
			'param':"",
			'defaultValue':'',
			'keymap' : {'id':'ID','text':'text'},
			'tplfn' : function(repo){ return repo.text; },
			'selectfn' : function(repo) { return repo.text; },
			'onchange' : '',
			'readonly':false,
			'data-edit-auth' : null,
			'data-unedit-auth' : null,
			'data-user-auth' : null
	};
	
	var obj = new Object();
	
	options =  $.extend( settings, options);
	if(FA.utils.isEmpty(options['render']))
		alert('render不能为空');
	
	
	$.ajax({
		url : options['url'],
		type : 'post' , 
		dataType : 'json',
		data : options['param'],
		success : function(data){
			if(typeof(options['keymap']) != 'undefined' && typeof(options['keymap']) != null ){
				var resultdata ;
				try{
					resultdata = $.parseJSON(data.result)
				}catch(e){
					resultdata = data.result;
				}
				var selectData = $.map(resultdata , function (obj) {
					  obj.id = obj[options.keymap['id']] ||  obj.id; 
					  obj.text = obj[options.keymap['text']] || obj.text; 
					  return obj;
					});
			}
			options['data'] = selectData;
			FA.utils.select2(options);
			FA.utils.initComponentAuth($("#" + options['id'])[0]);
		}
	})
	
}


FA.utils.layer = function(options){
	if(typeof(layer) == 'undefined'){
		FA.utils.alert("请先引用Leyer");
		return;
	}
	var settings = {
		'width' : '300px',
		'height' : '100%',
		'speed' : 100,
		'btns' : [],
		'title' : '',
		'shade' : 0.3 ,
		'close' : true ,
		'content' : '' ,
		'move' : false ,
		'id' : '',
		'offset' : 'auto',
		'scrollbar' : true,
		'fn1' : '',
		'fn2' : '',
		'url' : '',
		'data' : '',
	};
	
	options =  $.extend( settings, options);
	
	if(FA.utils.isEmpty(options['id']))
		options['id'] = FA.utils.uuid();
	//判断窗口是否已打开
	if($("#" + options['id']).length > 0){
		return;
	}
	if(FA.utils.isEmpty(FA.cache.layer)){
		FA.cache.layer = new Object();
	}
	FA.cache.layer[options['id']] = options;
	options['anim'] = 0;
	if(options['position'] == 'right'){
		options['anim'] = 5;
	}
	
	if(!FA.utils.isEmpty(options['url'])){
		$.ajax({
			url : options['url'],
			type : 'post',
			dataType : 'html',
			data : options['data'],
			success : function(result){
				options['content'] = result;
				FA.utils.layer.open(options);
			}
		})
	}else{
		FA.utils.layer.open(options);
	}
	return options['id'];
}

FA.utils.layer.open = function(options){
	var layindex = layer.open({
		  title: options['title']
	      ,id : options['id']
		  ,content: options['content']
		  ,shade :  options['shade']
		  ,offset: options['offset']
		  ,anim : options['anim']
		  ,area: [options['width'], options['height']]
	      ,btn : options['btns']
		  ,scrollbar : options['scrollbar']
	});   
	var falayer = $("#layui-layer"+layindex);
	options['layindex'] = layindex;
	FA.utils.layer.resetContentSize(options['id']);
	return options['id']; 
}

/**
 * @param key: 弹出窗的ID
 * fnname : 对应的方法名（fn1,fn2）
 */
FA.utils.layer.dofn = function(key , fnname , a1, a2 ,a3 , a4){
	var options;
	if(typeof(FA.cache.layer) != 'undefined' && FA.cache.layer != null){
		options = FA.cache.layer[key];
	}
	if(FA.utils.isEmpty(options[fnname])){
		FA.utils.alert(key + '窗口没有找到' + fnname + "的方法");
		return;
	}
	options[fnname](a1, a2 ,a3 , a4);
}


FA.utils.layer.loadcontent = function(key,options){
	var settings = {
		'url' : '',
		'type' : 'post',
		'data' : '',
		'dataType' : 'json',
		'callback' : ''
	};
	
	var url = '';
	if(typeof(options) == 'String'){
		settings['url'] = options;
	}else{
		settings =  $.extend( settings, options);
	}
	
	//FA.utils.showloading(key);
	$.ajax({
		url : settings['url'],
		type : settings['type'],
		dataType : settings['dataType'],
		data : settings['data'],
		success : function(result){
			//FA.utils.hideloading();
			if(typeof(settings['callback']) == 'function'){
				settings['callback'](result);
			}else{
				$("#" + key).html(result);
				FA.utils.layer.resetContentSize(key);
			}
		}
	})
	
	
}

FA.utils.layer.close = function(key){
	var options;
	if(typeof(FA.cache.layer) != 'undefined' && FA.cache.layer != null){
		options = FA.cache.layer[key];
	}
	layer.close(options['layindex']);
}

FA.utils.layer.resetContentSize = function(key){
	setTimeout(function(){
		try{
			var ttlh = Number($("#"+key).css('height').replace('px',''));
			var footbtns = $("#"+key).find(".fa-layer-foot-btns");
			var footheight = !footbtns.css('height') ? 0 : Number(footbtns.css('height').replace('px',''));
			var contenth = ttlh - footheight - 25;
			var falayercontent = $("#"+key).find('.fa-layer-content').length > 0 ? Number($("#"+key).find('.fa-layer-content').css('height').replace('px','')) : 0 ;
			//if(falayercontent > contenth ){
				$("#"+key).find('.fa-layer-content').css({
					height : contenth + 'px'
				});
			//}
		}catch(e){}
	},100);
}


/**
 * 显示加载层
 * 如果带有container，则会在container里显示加载层，否刚在body上
 */
FA.utils.showloading = function(container){
	if(FA.utils.isEmpty(container)){
		container = 'body';
	}
	if(container == 'body'){
		$('body').addClass('overflow-hidden');
	}else{
		container = "#" + container;
	}
	var tpl = "<div class='fa-loading'><div class='fa-loading-back'></div><i class='fa-loading-content'></i></div>";
	$(container).append(tpl);
	$(".fa-loading").css({
		'zIndex' : FA.utils.maxZindex() + 1
	});
}
FA.utils.hideloading = function(){
	$(".fa-loading").remove();
	$('body').removeClass('overflow-hidden');
}


/**
* 加载操作历史
*/
FA.utils.loadOptHistory = function(options){
	var settings = {
		'url' : '',
		'type' : 'post',
		'data' : '',
		'dataType' : 'html',
		'render' : '',
		'callback' : ''
	};
	settings =  $.extend( settings, options);
	
	FA.utils.loadOptHistory.settings = settings;
	
	if(FA.utils.isEmpty(settings['render']) || $(settings['render']).length == 0){
		FA.utils.msg('没有找到相应的渲染器');
		return;
	}
	var contentid = "timeline_" + FA.utils.uuid();
	var htmp = '<div class="btn btn-app btn-info btn-xs ace-settings-btn"><i class="ace-icon fa fa-history bigger-130"></i></div>\
		<div class="ace-settings-box clearfix" id="'+contentid+'">\
			<div class="fa-panel-head"><i class="ace-icon fa fa-history bigger-130"></i> 历史记录</div>\
			<div class="fa-panel-body"></div>\
		</div>\
		';
	
	$(settings['render']).append(htmp);
	var content = $(settings['render']).find('.ace-settings-box');
	FA.utils.showloading("contentid");
	$.ajax({
		url : settings['url'],
		type : settings['type'],
		dataType : settings['dataType'],
		data : settings['data'],
		success : function(result){
			FA.utils.hideloading();
			content.find('.fa-panel-body').append(result);
		}
	})
	 $(settings['render']).find('.ace-settings-btn').on(ace.click_event, function(e){
		e.preventDefault();
	
		$(this).toggleClass('open');
		content.toggleClass('open');
	 })
}
FA.utils.loadOptHistory.reload = function(){
	if(!FA.utils.isEmpty(FA.utils.loadOptHistory.settings)){
		var settings = FA.utils.loadOptHistory.settings;
		var content = $(settings['render']).find('.ace-settings-box');
		FA.utils.showloading("contentid");
		$.ajax({
			url : settings['url'],
			type : settings['type'],
			dataType : settings['dataType'],
			data : settings['data'],
			success : function(result){
				FA.utils.hideloading();
				content.find('.fa-panel-body').html(result);
			}
		})
	}
}
FA.utils.getFormData = function($form){
	var unindexed_array = $form.serializeArray();
    var indexed_array = {};

    $.map(unindexed_array, function(n, i){
        indexed_array[n['name']] = n['value'];
    });

    return indexed_array;
}


/**
 * 设置只读
 */
FA.utils.setAllReadonly = function(container){
	var $container = $(container).find('*');
	$.each($container , function(key , dom){
		FA.utils.setReadonly(dom);
	})
}

//转为JSON数据
;(function($){  
    $.fn.serializeJson=function(){  
        var serializeObj={};  
        var array=this.serializeArray();  
        var str=this.serialize();  
        $(array).each(function(){  
            if(serializeObj[this.name]){  
                if($.isArray(serializeObj[this.name])){  
                    serializeObj[this.name].push(this.value);  
                }else{  
                    serializeObj[this.name]=[serializeObj[this.name],this.value];  
                }  
            }else{  
                serializeObj[this.name]=this.value;   
            }  
        });  
        return serializeObj;  
    };  
})(jQuery);

//将DOM里的输入内容转为标准的URL参数
(function($){
    $.fn.serializeString=function(){  
        var val= '';  
        val = $(this).find('input, textarea, select').serialize();
        console.log(val);
        return val;  
    };  
})(jQuery);


//多选下拉框
;(function($){  
    $.fn.famulti=function(options){
    	var settings = {
			 enableFiltering: false,
   			 enableHTML: true,
   			 buttonClass: 'btn btn-white',
   			 nonSelectedText: '请选择',
   			 allSelectedText: '',
   			 buttonWidth : '100%', 
   			 numberDisplayed : 9999,
   			 maxHeight : 200,
   			 templates: {
   				button: '<button type="button" class="multiselect dropdown-toggle" data-toggle="dropdown"><span class="multiselect-selected-text"></span> &nbsp;<b class="fa fa-caret-down"></b></button>',
   				ul: '<ul class="multiselect-container dropdown-menu"><li class="hidden bottom-li"></li></ul>',
   				filter: '<li class="multiselect-item filter"><div class="flex-row filter-group" style="padding: 6px;"><div class="flex"><input class="form-control multiselect-search" type="text"></div></div></li>',
   				filterClearBtn: '<div style="width:25px"><span class="input-group-btn"><button class="btn btn-default btn-white btn-grey multiselect-clear-filter" type="button" style="padding: 3px 6px;"><i class="fa fa-times-circle red2"></i></button></span></div>',
   				li: '<li><a tabindex="0"><label></label></a></li>',
   		        divider: '<li class="multiselect-item divider"></li>',
   		        liGroup: '<li class="multiselect-item multiselect-group"><label></label></li>'
   			 }
    	}
    	options =  $.extend( settings, options);
    	this.multiselect(options);
    };  
})(jQuery);

/**
 * 判断是否有修改
 * 使用步骤：
 * 1、在页面打开后先调用 FA.utils.checkFormEdit(id);
 * 2、在需要判断是否有修改的方法调用 FA.utils.checkFormEdit.isEdit(id);
 */
if(FA.utils.isEmpty(FA.cache.formdata)){
	FA.cache.formdata = new Object();
}
FA.utils.checkFormEdit = function(container){
	if($("#" + container).length == 0){
		FA.utils.alert('ID不对，无法取得相应的DOM');
		return ;
	}
	FA.cache.formdata[container] = $("#" + container).serializeString();
}
FA.utils.checkFormEdit.isEdit = function(container){
	if($("#" + container).length == 0){
		FA.utils.alert('ID不对，无法取得相应的DOM');
		return ;
	}
	var newvalue = $("#" + container).serializeString();
	return newvalue != FA.cache.formdata[container] ;
}


/********************************jQgrid 封装************************/
//多选下拉框
jQuery.extend($.fn.fmatter , {
	multiselect : function(cellValue, options, rowdata) {
  	var selectoptions = options.colModel.editoptions['options'];
  	if(FA.utils.isEmpty(selectoptions)){
			FA.utlis.msg('editOptions.options为空。请设置');
			return '';
		}
  	var vals = cellValue.split(',');
  	var text = "";
  	$(vals).each(function(i , v){
  		$(selectoptions).each(function(a , b){
  			if(v == b.id){
  				text += b.text + ",";
  				return false;
  			}
  		})
  	})
  	return '<span data-value="'+cellValue+'">'+text+'</span>';
  }
});
jQuery.extend($.fn.fmatter.multiselect , {
  unformat : function(cellvalue, options ,cell) {
  	return $('span',cell).attr('data-value');
  }
});
var createMultiselect2 = function(value, editOptions){
	var selectoptions = editOptions.options;
	var vals = value.split(',');
	$("ul.multiselect-container").remove();
	var m = '<select class="multiselect" multiple="" style="height:28px">';
	var options = '';
	$(selectoptions).each(function(index , value){
		var isselect = false;
		$(vals).each(function(i,v){
			if(value.id == v){
				isselect = true;
			}
		})
		if(isselect){
			options += '<option value="'+this.id+'" selected=true>'+this.text+'</option>';
		}else{
			options += '<option value="'+this.id+'">'+this.text+'</option>';
		}
	});
	m += options + "</select>";
	setTimeout(function(){
		$('.multiselect').famulti({
			enableFiltering : true
		});
	},100);
	return m;
}
var getMultiselect2 = function(elem, oper, value){
	$("ul.multiselect-container[jqgrid-multiselect]").remove();
	if(FA.utils.isEmpty(elem.val())){
		return '';
	}
	var ids = elem.val().toString();
	return ids;
}

//多选下拉框
jQuery.extend($.fn.fmatter , {
	multiselect2 : function(cellValue, options, rowdata) {
  	var selectoptions = options.colModel.editoptions['options'];
  	if(FA.utils.isEmpty(selectoptions)){
			FA.utlis.msg('editOptions.options为空。请设置');
			return '';
		}
  	var vals = cellValue.split(',');
  	var text = "";
  	$(vals).each(function(i , v){
  		$(selectoptions).each(function(a , b){
  			if(v == b.id){
  				if(!FA.utils.isEmpty(b.view)){
  					text += b.view + ",";
  				}else{
  					text += b.text + ",";
  				}
  				return false;
  			}
  		})
  	})
  	return '<span data-value="'+cellValue+'">'+text+'</span>';
  }
});
jQuery.extend($.fn.fmatter.multiselect2 , {
  unformat : function(cellvalue, options ,cell) {
  	return $('span',cell).attr('data-value');
  }
});

//单选下拉框
jQuery.extend($.fn.fmatter , {
	select2 : function(cellValue, options, rowdata) {
  	var selectoptions = options.colModel.editoptions['options'];
  	if(FA.utils.isEmpty(selectoptions)){
			FA.utlis.msg('editOptions.options为空。请设置');
			return '';
		}
  	cellValue = FA.utils.isEmpty(cellValue) ? '' : cellValue;
  	var vals = cellValue.split(',');
  	var text = "";
	$(selectoptions).each(function(a , b){
		if(cellValue == b.id){
			text = b.view || b.text;
			return false;
		}
	})
  	return '<span data-value="'+cellValue+'">'+text+'</span>';
  }
});
jQuery.extend($.fn.fmatter.select2 , {
  unformat : function(cellvalue, options ,cell) {
  	return $('span',cell).attr('data-value');
  }
});
var createSelect2 = function(v, editOptions){
	var selectoptions = editOptions.options;
	var m = '<select class="select2 jqgrid-edit-select2" style="height:28px"></select>';
	setTimeout(function(){
		var dom = $('.jqgrid-edit-select2').select2({
			data : selectoptions ,
			placeholder:'',
			templateResult : function(repo){
				return repo.text ; 
			},
			templateSelection: function(repo) { 
				return repo.view || repo.text ; 
			},
		});
		dom.val(v);
		dom.trigger('change');
	},100);
	return m;
}
var getSelect2 = function(elem, oper, value){
	if(FA.utils.isEmpty(elem.val())){
		return '';
	}
	var ids = elem.val().toString();
	return ids;
}


//单选下拉框
jQuery.extend($.fn.fmatter , {
	fadate : function(cellValue, options, rowdata) {
  	return cellValue;
  }
});
jQuery.extend($.fn.fmatter.fadate , {
  unformat : function(cellvalue, options ,cell) {
  	return cellValue;
  }
});
var createFADate = function(v, editOptions){
	var getDateType= "";//1取当月第一天2取当月最后一天 空则正常执行
	if(!FA.utils.isEmpty(editOptions['getDateType'])){
		getDateType=editOptions['getDateType'];
	}
	var html = '<div class="input-group">\
					<input value="'+v+'" class="form-control date-picker form-control grid-cell-date-picker" type="text" readonly="readonly" data-date-format="yyyy-mm-dd" data-date-getDateType="'+getDateType+'">\
					<span class="input-group-addon"> <i class="fa fa-calendar bigger-110"></i> </span>\
				</div>';
	var grid = $(this);
	var rowid = editOptions['rowId'];
	var iCol = 0;
	var colModel = grid.jqGrid("getGridParam", "colModel");
	$(colModel).each(function(key , value){
		if(value['name'] === editOptions['name']){
			iCol = key;
			return false;
		}
	})
	setTimeout(function(){
		$('.grid-cell-date-picker').fadate({
			readonly:editOptions['readonly'],
			datetype:editOptions['datetype'],
			autoclose : true
		}).on('hide',function(){
			grid.jqGrid('saveCell',rowid , iCol);
		});
		$('.grid-cell-date-picker').focus();
	},100);
	return html;
}
var getFADate = function(elem, oper, value){
	$('input',elem).fadate('destroy');
	var getDateType =$('input',elem).attr('data-date-getDateType');
	var dataVal=$('input',elem).val();
	if(getDateType=='1'&&!FA.utils.isEmpty(dataVal)){//获取当月第一天
		var date=new Date(dataVal.replace(/-/,"/"));
		 date.setDate(1);
		 return formatData(date,"yyyy-MM-dd");
	}else if(getDateType=='2'&&!FA.utils.isEmpty(dataVal)){//获取当月最后一天
		var date=new Date(dataVal.replace(/-/,"/"));
		 var currentMonth=date.getMonth();
		 var nextMonth=++currentMonth;
		 var nextMonthFirstDay=new Date(date.getFullYear(),nextMonth,1);
		 var oneDay=1000*60*60*24;
		 return formatData(new Date(nextMonthFirstDay-oneDay),"yyyy-MM-dd");
	}else{
		return dataVal;
	}
	
}
 function formatData(date,fmt) { //格式化时间
    var o = {
        "M+": date.getMonth() + 1, //月份 
        "d+": date.getDate(), //日 
        "h+": date.getHours(), //小时 
        "m+": date.getMinutes(), //分 
        "s+": date.getSeconds(), //秒 
        "q+": Math.floor((date.getMonth() + 3) / 3), //季度 
        "S": date.getMilliseconds() //毫秒 
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}
var resetJqgridMultiselectOptions = function(grid_select , options){
	jQuery(grid_select).setColProp('ship',{editoptions:{ 'options' : options}})
}

//审核修改项变色
jQuery.extend($.fn.fmatter , {
	auditchange : function(cellValue, options, rowdata) {
  	var changelist = rowdata['changelist'];
  	if(changelist.indexOf(options.colModel.name) >= 0){
  		return '<span class="jqgrid-audit-change" data-value="'+cellValue+'">'+cellValue+'</span>';
  	}
  	return cellValue;
  }
});
/********************************jQgrid 封装***END*********************/
FA.utils.setReadonly = function(dom){
	switch(dom.tagName.toUpperCase()){
		case 'INPUT' : 
			dom.readOnly = true;
			//如果是checkbox 或者 是 radio
			if($(dom).attr('type') == 'checkbox' || $(dom).attr('type') == 'radio' ){
				$(dom).attr('onclick','return false');
			}
			if($(dom).hasClass('date-picker')){
				$(dom).datepicker('remove');
				$(dom).attr('data-readonly',1);
			}
			if($(dom).hasClass('fa-city')){
				$(dom).attr('data-readonly',1);
			}
			break;
		case 'TEXTAREA' :
			dom.readOnly = true;
			break;
		case 'SELECT' :
			dom.readOnly = true;
			if($(dom).hasClass('multiselect')){
				if($(dom).nextAll('.btn-group').find('button.multiselect').length > 0){
					$(dom).multiselect('disable');
					$(dom).removeAttr('disabled');
				}
			}else{
				dom.disabled = true;
				if($('[type=hidden][name='+dom.name+']').length == 0){
					$(dom).after('<input type="hidden" name="'+dom.name+'" value="'+dom.value+'" >');
				}
			}
			
			
			break;
		case 'BUTTON' :
			dom.disabled = true;
			break;
	}
}

FA.utils.setDisabled = function(dom){
	switch(dom.tagName.toUpperCase()){
		case 'INPUT' : 
			dom.disabled = true;
			break;
		case 'TEXTAREA' :
			dom.disabled = true;
			break;
		case 'SELECT' :
			dom.disabled = true;
			break;
		case 'BUTTON' :
			dom.disabled = true;
			break;
	}
}

FA.utils.setEnabled = function(dom){
	dom.disabled = false;
	switch(dom.tagName.toUpperCase()){
		case 'INPUT' : 
			dom.readOnly = false;
			if($(dom).attr('type') == 'checkbox' || $(dom).attr('type') == 'radio' ){
				$(dom).removeAttr('onclick');
			}
			if($(dom).hasClass('date-picker')){
				$(dom).removeAttr('data-readonly');
			}
			if($(dom).hasClass('fa-city')){
				$(dom).removeAttr('data-readonly');
			}
			break;
		case 'TEXTAREA' :
			dom.readOnly = false;
			break;
		case 'SELECT' :
			dom.readOnly = false;
			break;
	}
}
/**
 * data-edit-auth : 可编辑用户角色，以,号分开
 * data-unedit-auth ： 不可编辑用户角色，以,号分开
 * data-user-auth : 当前用户角色
 */
FA.utils.initAllComponentAuth = function(){
	
	$('input , select , textarea , button').each(function(){
		FA.utils.initComponentAuth(this);
	})
}

FA.utils.initComponentAuth = function(dom){
	if($(dom).attr('data-edit-auth') || $(dom).attr('data-unedit-auth')){
		var canedit = false;
		var userAuth = $(dom).attr('data-user-auth');
		if(userAuth){
			if($(dom).attr('data-edit-auth')){
				$.each($(dom).attr('data-edit-auth').split(',') , function(i , v){
					if(v == userAuth){
						canedit = true;
						return false;
					}
				})
			}
			if(!canedit && $(dom).attr('data-unedit-auth')){
				canedit = true;
				$.each($(dom).attr('data-unedit-auth').split(',') , function(i , v){
					if(v == userAuth){
						canedit = false;
						return false;
					}
				})
			}
		}
		if(!canedit){
			FA.utils.setReadonly(dom);
		}
	}
}



/*************************************FA 供应商 ***************************/
;(function($){ 
	var FA_AGENT_DATA;
	function FAAGENT( select ,options){
		this.$select = $(select);
		this.options = this.mergeOptions($.extend({}, options, this.$select.data()));
		this.$select.addClass('form-control select2');
		this.buildOptions(this.options.city , this.options.value);
		$this = this;
        this.$select.select2({
        	placeholder: $this.options.placeholder,
        	allowClear : $this.options.allowClear
        });
	};
	
	FAAGENT.prototype = {
		defaults : {
			 city : '',
			 value : '',
			 allowEmpty : true ,
			 placeholder : '',
			 allowClear : false
	   	},
	   	
	   	constructor: FAAGENT,
	   	
	   	mergeOptions: function(options) {
            return $.extend(true, {}, this.defaults, this.options, options);
        },
        
        buildOptions : function(city , value){
    		if(FA.utils.isEmpty(city))city = 'all';
    		var optiondata = FA_AGENT_DATA[city];
    		var optiontpl = '{{# for(var i = 0 ; i < d.length ; i++){ }}<option value="{{d[i].ID }}">{{d[i].AGENT_NAME }}</option> {{#} }}';
    		if(this.options.allowEmpty)
    			optiontpl = "<option></option>" + optiontpl;
    		var optionhtml = FA.utils.tpl(optiontpl , optiondata);
    		this.$select.html(optionhtml);
    		if(!FA.utils.isEmpty(value) && $("option[value="+value+"]",this.$select).length > 0)
    			$("option[value="+value+"]",this.$select)[0].selected = true;
    	},
    	
    	refresh : function(){
    		this.buildOptions(this.options.city);
    	},
    	
    	setCity : function(city){
    		this.options.city = city;
    	},
    	
    	setValue : function(value){
    		this.$select.val(value);
    		this.$select.trigger('change');
    	}
	},
	
    $.fn.faagent=function(option, parameter, extraOptions){
    	if(typeof(FA_AGENT_DATA) == 'undefined'){
			$.ajax({
				url : contextPath + "/agent/faagentPluginData",
				async : false,
				dataType : 'json',
				success : function(data){
					FA_AGENT_DATA = data.result;
				},
				error : function(e){
					console.log(e);
				}
			})
		}
    	return this.each(function() {
            var data = $(this).data('faagent');
            var options = typeof option === 'object' && option;

            // Initialize the multiselect.
            if (!data) {
                data = new FAAGENT(this, options);
                $(this).data('faagent', data);
            }

            // Call multiselect method.
            if (typeof option === 'string') {
                data[option](parameter, extraOptions);
                if (option === 'destroy') {
                    $(this).data('faagent', false);
                }
            }
        });
    };  
    $.fn.faagent.Constructor = FAAGENT;
})(jQuery);

//-----------------------------FABUTTON -------------------------------------
;(function($){ 
	function FABUTTON( button ,options){
		this.$button = $(button);
	};
	FABUTTON.prototype = {
		defaults : {
			
	   	},
	   	constructor: FABUTTON,
	   	loading: function() {
	   		this.$button.attr('disabled','disabled');
	   		this.$button.addClass('btn-disabled');
        },
        reset: function() {
	   		this.$button.removeAttr('disabled');
	   		this.$button.removeClass('btn-disabled');
        }
	};
	$.fn.fabutton=function(option, parameter, extraOptions){
		return this.each(function() {
            var data = $(this).data('fabutton');
            var options = typeof option === 'object' && option;
            if (!data) {
                data = new FABUTTON(this, options);
                $(this).data('fabutton', data);
            }
            if (typeof option === 'string') {
                data[option](parameter, extraOptions);
                if (option === 'destroy') {
                    $(this).data('fabutton', false);
                }
            }
        });
	}
})(jQuery);
//-----------------------------FABUTTON END-------------------------------------

//-----------------------------FAeditable -------------------------------------
;(function($){ 
	function FAEDITABLE( form ,options){
		this.$form = $(form);
		this.options = this.mergeOptions($.extend({}, options, this.$form.data()));
		
	};
	FAEDITABLE.prototype = {
		defaults : {
			type : 'readonly'
	   	},
	   	constructor: FAEDITABLE,

	   	mergeOptions: function(options) {
            return $.extend(true, {}, this.defaults, this.options, options);
        },
        readonly : function(){
        	$('input , select , textarea , button',this.$form).not('[type=hidden]').each(function(){
        		FA.utils.setReadonly(this);
        	})
        },
        disabled : function(){
        	$('input , select , textarea , button',this.$form).not('[type=hidden]').each(function(){
        		FA.utils.setDisabled(this);
        	})
        },
        enabled: function(){
        	$('input , select , textarea , button',this.$form).not('[type=hidden]').each(function(){
        		FA.utils.setEnabled(this);
        	})
        }
	};
	$.fn.faeditable=function(option, parameter, extraOptions){
		return this.each(function() {
            var data = $(this).data('faeditable');
            var options = typeof option === 'object' && option;
            if (!data) {
                data = new FAEDITABLE(this, options);
                $(this).data('faeditable', data);
            }
            if (typeof option === 'string') {
                data[option](parameter, extraOptions);
            }
        });
	}
})(jQuery);
//-----------------------------FAeditable END-------------------------------------

//-----------------------------FADATE -------------------------------------
;(function($){ 
	function FADATE( input ,options){
		this.$input = $(input);
		this.options = this.mergeOptions($.extend({}, options, this.$input.data()));
		this.options['todayHighlight'] = true;
		if(this.options['datetype'] == '2'){
			this.options['format'] = "yyyy-mm";
			this.options['startView'] = 1;
			this.options['minViewMode'] = 1;
		}
		if(this.options['datetype'] == '3'){
			this.options['format'] = "yyyy";
			this.options['startView'] = 2;
			this.options['minViewMode'] = 2;
		}
		this.$input.datepicker(this.options).next().on(ace.click_event, function(){
			$(this).prev().focus();
		});
		if(this.options['readonly']){
			this.$input.attr('readonly','readonly');
		}else{
			this.$input.removeAttr('readonly');
		}
	};
	FADATE.prototype = {
		defaults : {
			readonly : false,
			autoclose : true,
			format : 'yyyy-mm-dd',
			startDate : '',
			datetype : '1' , //1：为到日，2：为到月， 3：为到年     
	   	},
	   	mergeOptions: function(options) {
            return $.extend(true, {}, this.defaults, this.options, options);
        },
	   	constructor: FADATE,
	   	disabled: function() {
	   		this.$input.attr('readonly','readonly');
	   		this.$input.attr('data-readonly','1');
        },
        reset: function() {
        },
        rebuild : function(option){
        	this.$input.datepicker('destroy');
            data = new FADATE(this.$input, option);
            $(this).data('fadate', data);
        },
        destroy : function(){
        	this.$input.datepicker('destroy');
        }
	};
	$.fn.fadate=function(option, parameter, extraOptions){
		return this.each(function() {
            var data = $(this).data('fadate');
            var options = typeof option === 'object' && option;
            if (!data || typeof option === 'object') {
                data = new FADATE(this, options);
                $(this).data('fadate', data);
            }
            if (typeof option === 'string') {
                data[option](parameter, extraOptions);
                if (option === 'destroy') {
                    $(this).data('fadate', false);
                }
            }
        });
	}
})(jQuery);
//-----------------------------FADATE END-------------------------------------

FA.utils.ajaxCreateTpl = function(id , url){
	var tpl = "<xmp id='"+id+"' data-url='"+url+"'></xmp>";
	$('body').append(tpl);
	$.post(url,'',function(html){
		document.getElementById(id).innerHTML = html;
	},'html')
}

FA.utils.getLayerContent = function(id){
	var content = document.getElementById(id).innerHTML;
	if(FA.utils.isEmpty(FA.utils.trim(content))){
		var url = $("#" + id).attr('data-url');
		$.ajax({
			url : url ,
			async : false ,
			dataType : 'html',
			success : function(html){
				content = html;
			}
		})
	}
	return content;
}

FA.utils.activeLevel = function(id){
	$("#"+id).addClass('checked');
}

FA.utils.getCookieUrlName = function(gridid , type , url){
	var baseurl = location.href;
	if(!FA.utils.isEmpty(url)){
		baseurl = url ;
	}
	var locationstring = baseurl.replace(location.host , '').replace(location.protocol + '//' , '');
	var cookid = locationstring.substr(0 , locationstring.indexOf('?') > 0 ? locationstring.indexOf('?') : locationstring.length) + "_" + gridid + "_" + type;
	return cookid;
}