$(function(){
	insertRow();
	//doSearch();
});

function doSearch(){
	$('#tagTable').datagrid('load',{
		title:$('#search-title').val(),
		startDate:$('#beginDate').datebox('getValue')?$('#beginDate').datebox('getValue'):null,
		endDate:$('#endDate').datebox('getValue')?$('#endDate').datebox('getValue'):null
	})
}

function onClickCell(index,field,value){
	var clickData = getClickData(index);
	if('send'==field){
		sendArticle(clickData,index)
	}
	if('update'==field){
		toUpdatePG(clickData,index)
	}
	if('qrcode'==field){
		getQrcode(clickData);
		//alert("下载二维码");
	}
}

function getClickData(index){
	return $('#tagTable').datagrid('getRows')[index];
}

function getQrcode(data){
	if(data.qrcodeTicket){
		window.open("https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket="+data.qrcodeTicket);
	}else{
		var param = {};
		param.articleId=data.id;
		postAjax("getQrcodeTicket",param,function(returnData){window.open("https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket="+returnData.qrcodeTicket);})
	}
}

function sendArticle(data,index){
 // $.messager.defaults = {ok: "确定", cancel: "取消" };
  $.messager.confirm(
  '发送文章','你确定想发送改文章吗?',function(r){
            if (r){
					updateRow(data,index);
            }
        });
}

function toUpdatePG(data,index){
	window.open("editArticle.html?id="+data.id);
}

function getCurrDate(){
   var mydate = new Date();
   var str = "" + mydate.getFullYear() + "-";
   str += (mydate.getMonth()+1) + "-";
   str += mydate.getDate() ;
   return str;
}
function updateRow(data,index){
	var param = {};
	param.articleId = data.id;
	param.mediaId = data.mediaId;
	postAjax("send",param,function(){
		$('#tagTable').datagrid('updateRow',{
			index:index,
			row: {
				sendTime: getCurrDate()
			}
	})

});
}

function fmtQrcodeIcon(){
	return "<span  style='cursor: pointer;font-size:24px;' class=\"icon-print\";\">&nbsp;&nbsp;&nbsp;</span>";
}
function fmtUpdateIcon(){
	return "<span  style='cursor: pointer;font-size:24px;' class=\"icon-edit\";\">&nbsp;&nbsp;&nbsp;</span>";
}
function fmtSendIcon(){
	return "<span  style='cursor: pointer;font-size:24px;' class=\"icon-redo\";\">&nbsp;&nbsp;&nbsp;</span>";
}


function insertRow(){
		 $('#tagTable').datagrid('appendRow',{
			 id:1,
			 title :'贺新年',
			 context:'2016年到了你准备好了吗。。。，',
				createTime :'2015-09-12',
				sendTime:'2016-02-12'
			});
	 }

function postAjax(postUrl, postData, successCallback) {
	$.ajax({
		type: "POST",
		url: postUrl,
		dataType: "json",
		data: postData,
		success: function (data) {
			if (data.error) {
				showError(data.error);
				//$.messager.alert('错误',data.error,'error');
			} else {
				successCallback(data)
			}
		},
		error: function () {
			$.messager.alert('错误', '系统错误，请联系管理员', 'error');
		}
	});
}