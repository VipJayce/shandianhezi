/**
 * FA 表单输入合法性验证
 * 例子：
 * 		FA.valid({
 * 			list : [
 * 					{'target':'#xxxxx' , type : 'm' , errormsg : '手机格式不正确'},
 * 					{'target':'#xxxxxxxx' , type : '*|e' , errormsg : '邮箱格式不正确' , nullmsg : '邮箱必须输入'},
 * 				]
 * 		})
 * 
 * 
 */


if(typeof(FA) == 'undefined' || typeof(FA.utils) == 'undefined'){
	alert('请先引入fautils.js文件');
}

FA.valid = new Object();
FA.cache.valid = new Object();
FA.cache.valid.error = new Array();

FA.valid = function(options){
	if(!FA.utils.isEmpty(FA.cache.valid.error)){
		$.each(FA.cache.valid.error , function(key , value){
			$(value['target']).removeClass('data-error');
			if($(value['target']).hasClass('select2')){
				$(value['target']).next('span.select2').find('span.select2-selection').removeClass('data-error');
			}
			if($(value['target']).nextAll('.btn-group').find('button.multiselect').length > 0){
				$(value['target']).nextAll('.btn-group').find('button.multiselect').removeClass('data-error');
			}
		})
	}
	FA.cache.valid.error = new Array();
	/**
	 * list
	 * 		dom 对象 , type 判断类型 , msg 错误揭示信息 , dom2 比较对象
	 * 		type包括 
	 * 			eq 		: target = totarget
	 * 			*		: 必须输入
	 * 			m		: 手机格式
	 * 			e		: 邮箱格式
	 * 			n		: 数字格式
	 * 			tel 	: 电话格式
	 * 			idcard	: 身份证
	 * 			maxlength
	 */
	var settings = {
			automsg : true , //自动显示错误消息
			list : []
	}
	
	options =  $.extend( settings, options);
	//判断是否有超长的
	$('[data-max-length]').each(function(){
		if(!FA.utils.isEmpty($(this).attr('data-max-length'))){
			var obj = {
					'dom' : $(this),
					'type' : 'maxlength',
					'maxLength' : $(this).attr('data-max-length'),
					'errormsg' : $(this).attr('data-max-length-error')
			};
			options.list.push(obj);
		}
	})
	$.each(options.list , function(index , value){
		var dom = $(value['dom']);
		var domvalue = dom.val();
		var typelist = value['type'].split('|');
		$.each(typelist , function(i , v){
			if(value['iserror'] == 1)
				return false;
			
			switch(v){
				case '*' :
					if(FA.utils.isEmpty(domvalue)){
						value['iserror'] = 1;
						value['msg'] = value['nullmsg'];
					}
					break;
				case 'm' : 
					if(!FA.utils.isMobile(domvalue)){
						value['iserror'] = 1;
						value['msg'] = value['errormsg'];
					}
					break;
				case 'e' : 
					if(!FA.utils.isEmail(domvalue)){
						value['iserror'] = 1;
						value['msg'] = value['errormsg'];
					}
					break;
				case 'n' :
					if(!FA.utils.isNumber(domvalue)){
						value['iserror'] = 1;
						value['msg'] = value['errormsg'];
					}
					break;
				case 'tel' :
					if(!FA.utils.isTel(domvalue)){
						value['iserror'] = 1;
						value['msg'] = value['errormsg'];
					}
					break;
				case 'idcard' :
					if(!FA.utils.isIdCard(domvalue)){
						value['iserror'] = 1;
						value['msg'] = value['errormsg'];
					}
					break;
				case 'eq' :
					var tov = $(value['dom2']).val();
					if(domvalue != tov){
						value['iserror'] = 1;
						value['msg'] = value['errormsg'];
					}
					break;
				case 'maxlength':
					if(!FA.utils.isEmpty(value['maxLength']) && !FA.utils.isEmpty(domvalue)){
						if(domvalue.length > Number(value['maxLength'])){
							value['iserror'] = 1;
							value['msg'] = value['errormsg'];
						}
					}
					break;
				default :
					break;
			}
		})
		
		if(value['iserror'] == 1){
			value['target'] = value['dom'];
			FA.cache.valid.error.push(value);
		}
		
	});
	
	//如果设置自动显示错误信息
	if(options['automsg'] && !FA.utils.isEmpty(FA.cache.valid.error)){
		FA.valid.showerror(FA.cache.valid.error);
	}
	
	return FA.cache.valid;
}

FA.valid.addError = function(errormsg,select){
	//{'target':'#xxxxxxxx' , type : '*|e' , errormsg : '邮箱格式不正确' , nullmsg : '邮箱必须输入'}
	var errordata = {'target' : select , 'type' : '' , 'errormsg' : errormsg , 'nullmsg' : errormsg , 'msg' : errormsg , 'iserror' : 1 };
	FA.cache.valid.error.push(errordata);
}

FA.valid.showerror = function(data){
	var errordata = data;
	var templete = '<div class="faui-error-msg">\
	{{# for(var i = 0 ; i < d.length ; i++){ }}\
		<div class="faui-error-msg-li">{{d[i].msg }}</div>\
	{{#} }}	\
		</div>';
	var content = '';
	if(FA.utils.isEmpty(data)){
		errordata = FA.cache.valid.error;
	}
	if(FA.utils.objIs(errordata , 'String')){
		errordata = [{'msg':errordata}];
		content = FA.utils.tpl(templete , errordata);
	}
	if(FA.utils.objIs(errordata , 'Object')){
		errordata = [{'msg':errordata['msg']}];
		content = FA.utils.tpl(templete , errordata);
	}
	if(FA.utils.objIs(errordata , 'Array')){
		content = FA.utils.tpl(templete , errordata);
		$.each(errordata , function(key , value){
			$(value['target']).addClass('data-error');
			if($(value['target']).hasClass('select2')){
				$(value['target']).next('span.select2').find('span.select2-selection').addClass('data-error');
			}
			if($(value['target']).nextAll('.btn-group').find('button.multiselect').length > 0){
				$(value['target']).nextAll('.btn-group').find('button.multiselect').addClass('data-error');
			}
		})
	}
	
	
	layer.msg(content , {
		offset : 't' , 
		time : 4000 ,
		anim : 6
	})
	
}


