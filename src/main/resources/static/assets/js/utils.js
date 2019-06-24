var utils = new Object();
utils.msg = function(msg , fn){
	$.gritter.add({
		title: '',
		text: msg,
		time : '2000',
		class_name: 'gritter-info gritter-center  gritter-light'
	});
	setTimeout(function(){
		if(typeof(fn) == 'function'){
			fn();
		}
	},2000);
}

utils.alert = function(msg , fn){
	var text = "<div style='padding: 20px 20px;'>"+msg+"</div>";
	$(text).dialog({
		resizable: false,
		width: '320',
		modal: true,
		title: '<div class="widget-header"><h4 class="smaller">提示</h4></div>',
		title_html: true,
		buttons: [
			{
				html: "确定",
				"class" : "btn btn-primary btn-minier",
				click: function() {
					$( this ).dialog( "close" );
					if(typeof(fn) == 'function'){
						fn();
					}
				}
			}
		]
	});
}


utils.confirm = function(msg , fn , cfn){
	var text = "<div style='padding: 20px 20px;'>"+msg+"</div>";
	$(text).dialog({
		resizable: false,
		width: '320',
		modal: true,
		title: '<div class="widget-header"><h4 class="smaller">提示</h4></div>',
		title_html: true,
		dialogClass: "no-close",
		buttons: [
			{
				html: "确定",
				"class" : "btn btn-primary btn-minier",
				click: function() {
					$( this ).dialog( "close" );
					if(typeof(fn) == 'function'){
						fn();
					}
				}
			},
			{
				html: "取消",
				"class" : "btn btn-minier",
				click: function() {
					$( this ).dialog( "close" );
					if(typeof(cfn) == 'function'){
						cfn();
					}
				}
			}
		]
	});
}


utils.gritter = function(title , msg , fn){
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

