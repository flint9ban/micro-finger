$(function(){
	insertRow();
});
function onClickCell(index,field,value){
	if('send'==field){
		sendArticle(index)
	}
	if('update'==field){
		toUpdatePG(index)
	}
	if('qrcode'==field){
		alert("下载二维码");
	}
}
function sendArticle(index){
 // $.messager.defaults = {ok: "确定", cancel: "取消" };
  $.messager.confirm(
  '发送文章','你确定想发送改文章吗?',function(r){
            if (r){
					updateRow(index);
            }
        });
}

function toUpdatePG(index){
	location.href="www.baidu.com";
}

function getCurrDate(){
   var mydate = new Date();
   var str = "" + mydate.getFullYear() + "-";
   str += (mydate.getMonth()+1) + "-";
   str += mydate.getDate() ;
   return str;
}
function updateRow(index){
	$('#tagTable').datagrid('updateRow',{
	index:index,
	row: {
		sendTime: getCurrDate()
	}
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
			 title :'贺新年',
			 context:'2016年到了你准备好了吗。。。，',
				createTime :'2015-09-12',
				sendTime:'2016-02-12'
			});
	 }