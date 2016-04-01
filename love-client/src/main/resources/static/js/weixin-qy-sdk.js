var weixin=(function(){
	var ready=false;
	
	function getRootPath(){
		var curWwwPath = window.document.location.href;
	    var pathName = window.document.location.pathname;
	    var pos = curWwwPath.indexOf(pathName);
	    var localhostPath = curWwwPath.substring(0, pos);
	    return(localhostPath);
	}
	
	
	function getVersion(){
		    var t = navigator.userAgent.toLowerCase();
		    var x = t.match(/micromessenger\/(\d+\.\d+\.\d+)/) || t.match(/micromessenger\/(\d+\.\d+)/);
		    var v = x ? x[1] : "";
		    return v;
	}
	
	function init(){
		var fullUrl=location.href;
		var rootUrl=fullUrl.split("#")[0];
		 $.ajax({
            type: 'POST',
            url: '../weixin/qy/jsSDK/getSign.do',
            data: {
                url:rootUrl
            },
            dataType: 'json',
            success:function (data) {
           	 wxConfig(data);
            }
        });
	}
	
	
	function wxConfig(data){
		wx.config({
		    debug: false,
		    appId: data.appId, 
		    timestamp: data.timestamp , 
		    nonceStr: data.nonceStr, 
		    signature: data.signature,
		    jsApiList: ['onMenuShareTimeline','onMenuShareAppMessage','onMenuShareQQ','onMenuShareWeibo','onMenuShareQZone',
		                'startRecord','onVoiceRecordEnd','playVoice','pauseVoice','onVoicePlayEnd','uploadVoice','downloadVoice',
		                'chooseImage','previewImage','uploadImage','downloadImage','translateVoice','getNetworkType','openLocation',
		                'getLocation','hideOptionMenu','showOptionMenu','hideMenuItems','showMenuItems','hideAllNonBaseMenuItem','showAllNonBaseMenuItem',
		                'closeWindow','scanQRCode','chooseWXPay','openProductSpecificView','addCard','chooseCard','openCard','getBrandWCPayRequest'
		                ] 
		});
	}
	
	
	var childWindow=null;
	window.addEventListener("message",function(e){
		var data=e.data;
		childWindow=e.source;
		if(data.scope!="invokeWeixinFunc"){
			return;
		}
		invokeWxFunc(data.name,data.params);
	},false);
	
	
	function invokeWxFunc(_func,params){
		if(_func=="setShareAppmessageData"||_func=="onMenuShareAppMessage"||_func=="onMenuShareTimeline"||_func=="onMenuShareQQ"||_func=="onMenuShareWeibo"||
				_func=="onMenuShareQZone"||_func=="setShareTimelineData"||_func=="setShareQQData"||_func=="setShareWeiboData"||_func=="setShareQZoneData"){
			packAndcallBridge(_func,params);
			return;
		}
		if(!weixin[_func]){
			return;
		}
		if(_func=="getBrandWCPayRequest"){
			getBrandWCPayRequest(params);
			return;
		}
		if(_func=="addCard"){
			addCard(params);
			return ;
		}
		callBridge(_func, params, null);
	}
	
	
	function packAndcallBridge(func,param){
		var data={};
		data.title=param.title||document.title;
		data.link=param.link||document.documentURI;
		data.desc=param.desc||document.documentURI;
		data.success=param.success;
		data.fail=param.fail;
		data.cancel=param.cancel;
		data.trigger=param.trigger;
		if(param.img_url){
			data.imgUrl=param.img_url;  
			callBridge(func,data,null);
		}else{
		//	data.imgUrl = getRootPath()+"/resources/images/mbklogo.png";
			callBridge(func,data,null);
//			getSharePreviewImage(function(img) {
//				if (img) {
//					data.imgUrl = img.src;
//				}else{
//					data.imgUrl = getRootPath()+"/resources/images/mbklogo.png";
//				}
//				callBridge(func,data,null);
//			});
		}
	}
	
	/** 页面图片算法摘自微信wxjs.js */
	// 获取页面图片算法：
	// 在页面中找到第一个最小边大于290的图片，如果1秒内找不到，则返回空（不带图分享）。
	function getSharePreviewImage(cb) {
		var isCalled = false;
		var callCB = function(_img) {
			if (isCalled)
				return;
			isCalled = true;
			cb(_img);
		};
		var _allImgs = _WXJS('img');
		if (_allImgs.length == 0)
			return callCB();
		// 过滤掉重复的图片
		var _srcs = {};
		var allImgs = [];
		for (var i = 0; i < _allImgs.length; i++) {
			var _img = _allImgs[i];
			// 过滤掉不可以见的图片
			if (_WXJS(_img).css('display') == 'none'
					|| _WXJS(_img).css('visibility') == 'hidden')
				continue;
			if (!_srcs[_img.src]) {
				_srcs[_img.src] = 1; // mark added
				allImgs.push(_img);
			}
		}
		var results = [];
		var img;
		for (i = 0; i < allImgs.length && i < 100; i++) {
			img = allImgs[i];
			var newImg = new Image();
			newImg.onload = function() {
				this.isLoaded = true;
				var loadedCount = 0;
				for (var j = 0; j < results.length; j++) {
					var res = results[j];
					if (!res.isLoaded) {
						break;
					}
					loadedCount++;
					if (res.width > 290 && res.height > 290) {
						callCB(res);
						break;
					}
				}
				if (loadedCount == results.length) {
					// 全部都已经加载完了，但还是没有找到。
					callCB();
				}
			};
			newImg.src = img.src;
			results.push(newImg);
		}
		setTimeout(function() {
			for (var j = 0; j < results.length; j++) {
				var res = results[j];
				if (!res.isLoaded)
					continue;
				if (res.width > 290 && res.height > 290) {
					callCB(res);
					return;
				}
			}
			callCB();
		}, 1000);
	}
	
	wx.ready(function(){
		ready=true;
		packAndcallBridge("setShareTimelineData",{});
		packAndcallBridge("setShareAppmessageData",{});
		packAndcallBridge("setShareQQData",{});
		packAndcallBridge("setShareWeiboData",{});
	//	packAndcallBridge("setShareQZoneData", {});
		onBridgeReady();
	});
	
	
	var bridgeTasks = [];
	
	function onBridgeReady() {
		for (var task = bridgeTasks.shift(); task; task = bridgeTasks.shift()){
			callBridge(task.func, task.params, task.callback);
		}
		if (weixin && weixin.onReady)
			weixin.onReady();
	}
	

	var callbackArrays=[];
	function pushCallbacks(func,callback){
		if(callback instanceof Function){
			callbackArrays.push({
				name : func,
				func : callback
			});
		}
	}
	

	var timelinePushed=false;
	var appMessagePushed=false;
	var QQPushed=false;
	var weiboPushed=false;
	var QZonePushed=false;
	function callBridge(func, params, callback) {
		if(ready){
			if(func=="editAddress"){//收货地址的写法还是微信旧版本的写法
				WeixinJSBridge.call(func, params, callback);
				return;
			}
			if(func=="getBrandWCPayRequest"){
				
			}
			if(func=="setShareTimelineData")	{
				if(!timelinePushed){
					pushCallbacks("afterShareTimeline",weixin.afterShareTimeline);
					pushCallbacks("onShareTimeline",weixin.onShareTimeline);
					timelinePushed=true;
				}
				params.success=function(res){
					executeCallback("afterShareTimeline",res);
					executeCallback("onMenuShareTimeline.success",res);
				};
				params.fail=function(res){
					executeCallback("afterShareTimeline",res);
					executeCallback("onMenuShareTimeline.fail",res);
				};
				params.cancel=function(res){
					executeCallback("afterShareTimeline",res);
					executeCallback("onMenuShareTimeline.cancel",res);
				};
				params.trigger=function(res){
					executeCallback("onShareTimeline",res);
					executeCallback("onMenuShareTimeline.trigger",res);
				};
				wx["onMenuShareTimeline"](params);
				return ;
			}
			if(func=="setShareAppmessageData")	{
				if(!appMessagePushed){
					pushCallbacks("afterShareAppmessage",weixin.afterShareAppmessage);
					pushCallbacks("onShareAppmessage",weixin.onShareAppmessage);
					appMessagePushed=true;
				}
				params.success=function(res){
					executeCallback("afterShareAppmessage",res);
					executeCallback("onMenuShareAppMessage.success",res);
				};
				params.fail=function(res){
					executeCallback("afterShareAppmessage",res);
					executeCallback("onMenuShareAppMessage.fail",res);
				};
				params.cancel=function(res){
					executeCallback("afterShareAppmessage",res);
					executeCallback("onMenuShareAppMessage.cancel",res);
				};
				params.trigger=function(res){
					executeCallback("onShareAppmessage",res);
					executeCallback("onMenuShareAppMessage.trigger",res);
				};
				wx["onMenuShareAppMessage"](params);
				return ;
			}
			if(func=="setShareQQData")	{
				if(!QQPushed){
					pushCallbacks("afterShareQQ",weixin.afterShareQQ);
					pushCallbacks("onShareQQ",weixin.onShareQQ);
					QQPushed=true;
				}
				params.success=function(res){
					executeCallback("afterShareQQ",res);
					executeCallback("onMenuShareQQ.success",res);
				};
				params.fail=function(res){
					executeCallback("afterShareQQ",res);
					executeCallback("onMenuShareQQ.fail",res);
				};
				params.cancel=function(res){
					executeCallback("afterShareQQ",res);
					executeCallback("onMenuShareQQ.cancel",res);
				};
				params.trigger=function(res){
					executeCallback("onShareQQ",res);
					executeCallback("onMenuShareQQ.trigger",res);
				};
				wx["onMenuShareQQ"](params);
				return ;
			}
			if(func=="setShareWeiboData")	{
				if(!weiboPushed){
					pushCallbacks("afterShareWeibo",weixin.afterShareWeibo);
					pushCallbacks("onShareWeibo",weixin.onShareWeibo);
					weiboPushed=true;
				}
				params.success=function(res){
					executeCallback("afterShareWeibo",res);
					executeCallback("onMenuShareQQ.success",res);
				};
				params.fail=function(res){
					executeCallback("afterShareWeibo",res);
					executeCallback("onMenuShareQQ.fail",res);
				};
				params.cancel=function(res){
					executeCallback("afterShareWeibo",res);
					executeCallback("onMenuShareQQ.cancel",res);
				};
				params.trigger=function(res){
					executeCallback("onShareWeibo",res);
					executeCallback("onMenuShareQQ.trigger",res);
				};
				wx["onMenuShareQQ"](params);
				return ;
			}
			if(func=="setShareQZoneData")	{
				if(!QZonePushed){
					pushCallbacks("afterShareQZone",weixin.afterShareQZone);
					pushCallbacks("onShareQZone",weixin.onShareQZone);
					QZonePushed=true;
				}
				params.success=function(res){
					executeCallback("afterShareQZone",res);
					executeCallback("onMenuShareQZone.success",res);
				};
				params.fail=function(res){
					executeCallback("afterShareQZone",res);
					executeCallback("onMenuShareQZone.fail",res);
				};
				params.cancel=function(res){
					executeCallback("afterShareQZone",res);
					executeCallback("onMenuShareQZone.cancel",res);
				};
				params.trigger=function(res){
					executeCallback("onShareQZone",res);
					executeCallback("onMenuShareQZone.trigger",res);
				};
				wx["onMenuShareQZone"](params);
				return ;
			}
			if(func=="onMenuShareTimeline"||func=="onMenuShareAppMessage"||func=="onMenuShareWeibo"||func=="onMenuShareQZone"||func=="onMenuShareQQ")	{
				pushCallbacks(func+".success",params.success);
				pushCallbacks(func+".fail",params.fail);
				pushCallbacks(func+".cancel",params.cancel);
				pushCallbacks(func+".trigger",params.trigger);
				params.success=function(res){
					executeCallback(func+".success",res);
					if(func=="onMenuShareAppMessage"){
						callfunc="afterShareAppmessage";
					}else{
						callfunc=func.replace("onMenu","after")
					}
					executeCallback(callfunc,res);
				};
				params.fail=function(res){
					executeCallback(func+".fail",res);
					if(func=="onMenuShareAppMessage"){
						callfunc="afterShareAppmessage";
					}else{
						callfunc=func.replace("onMenu","after")
					}
					executeCallback(callfunc,res);
				};
				params.cancel=function(res){
					executeCallback(func+".cancel",res);
					if(func=="onMenuShareAppMessage"){
						callfunc="afterShareAppmessage";
					}else{
						callfunc=func.replace("onMenu","after")
					}
					executeCallback(callfunc,res);
				};
				params.trigger=function(res){
					executeCallback(func+".trigger",res);
					if(func=="onMenuShareAppMessage"){
						callfunc="onShareAppmessage";
					}else{
						callfunc=func.replace("onMenu","on")
					}
					executeCallback(callfunc,res);
				};
				wx[func](params);
				return ;
			}
			
			params.success=function(res){
				if(callback){
					callback(res);
				}
				callChildFunc(func,res);
			};
			wx[func](params);
		}
		else{
			bridgeTasks.push({
				func : func,
				params : params,
				callback : callback
			});
		}
		}
	
	function executeCallback(func,res){
		for (var i=0;i<callbackArrays.length;i++){
			if(callbackArrays[i].name==func){
				callbackArrays[i].func(res);
			}
		}
		callChildFunc(func,res);
	}
	
	function callChildFunc(func,res){
		if(childWindow){
			childWindow.postMessage({
				scope :"callChildFunc",
				name : func,
				params : res
			},"*");
		}
	}
	
	function editAddress(param,callback){
		$.ajax({
			type : "POST",
			url :getRootPath() +"/authAddress/getAddrSign.do",
			data : {
				'openId':param.openId,
				'url':param.url
			},
			dataType : "json",
			success : function(data, textStatus, jqXHR) {
				var params={};
				params.appId= data.appId;
				params.scope=   "jsapi_address";
				params.signType=  "sha1";
				params.addrSign=  data.addrSign;
				params.timeStamp=  data.timestamp;
				params.nonceStr= data.noncestr;
				callBridge("editAddress", params,function(res){
					if(callback){
						callback(res);
					}
					callChildFunc("editAddress",res);
				}) ;
			}
		 });
	}
	
	
	function  getBrandWCPayRequest(param,callback){
		if (typeof WeixinJSBridge == "undefined"){
			return ;
		}
		 $.ajax({
				type : "POST",
				url :'../pay/getApiParams.do',
				data : param,
				dataType : "json",  
				success : function(data, textStatus, jqXHR) {
					var params=data.params;
					var payParam={
								"appId" : params.appId,     //公众号名称，由商户传入     
					           "timeStamp": params.timeStamp,     //时间戳，自1970年以来的秒数     
					           "nonceStr" :  params.nonceStr,//随机串     
					           "package" :params['prepayId'],  
					           "signType" : "MD5",         //微信签名方式：     
					           "paySign" :params.sign //微信签名 
					};
					
					   WeixinJSBridge.invoke(
						       'getBrandWCPayRequest',payParam ,
						       function(res){     
						           if(res.err_msg == "get_brand_wcpay_request：ok" ) {
						        	   callback(res);
						           } else{
						        	alert(res.err_msg);
						           }    
						       }
						   ); 
				}
		 });
	}
	
	
	function addCard(cardId){
		 $.ajax({
				type : "POST",
				url:'../weixin/jsSDK/getCardParam.do',
				data : {
					method:'addCard',
					cardId:cardId
				},
				dataType : "json",  
				success : function(data, textStatus, jqXHR) {
					var cardExt={
				        	timestamp:data.timestamp,
				        	nonce_str:data.nonceStr,
				        	signature:data.signature
				        }
					var cardExtStr=JSON.stringify(cardExt);
					var params={
							cardList: [{
						        cardId:cardId ,
						        cardExt: cardExtStr
						    }]
							
					}
					callBridge("addCard", params,null);
				}
		 });
	}
	
	
	function chooseWXPay(param){
		 $.ajax({
				type : "POST",
				url :getRootPath()+'/pay/getApiParams.do',
				data : {
					'openId':param.openId,
					'docServerId':param.docServerId,
					'description' :param.body,
					'numbers':param.numbers,
					'totalFee':param.total_fee
				},
				dataType : "json",  
				success : function(data, textStatus, jqXHR) {
					var params=data.params;
					var payParam={
								"appId" : params.appId,     //公众号名称，由商户传入     
					           "timeStamp": params.timeStamp,     //时间戳，自1970年以来的秒数     
					           "nonceStr" :  params.nonceStr, //随机串     
					           "package" :params['prepayId'],  
					           "signType" : "MD5",         //微信签名方式：     
					           "paySign" :params.sign //微信签名 
					};
					payParam.success=(param.success instanceof Function?param.success:undefined);
					payParam.fail=(param.fail instanceof Function?param.fail:undefined);
					payParam.cancel=(param.cancel instanceof Function?param.cancel:undefined);
					callBridge("chooseWXPay", payParam, null) ;
				}
		 });
	}
	
	function refreshMenuSettings(){
		packAndcallBridge("onMenuShareTimeline",{});
		packAndcallBridge("onMenuShareAppMessage",{});
		packAndcallBridge("onMenuShareQQ",{});
		packAndcallBridge("onMenuShareWeibo",{});
//		packAndcallBridge("onMenuShareQZone",{});
	}

	
	return{
		init:init,
		getVersion:getVersion,
		hideOptionMenu:function() {//隐藏菜单
			callBridge("hideOptionMenu",{},null);
		},
		showOptionMenu:function() {//显示菜单
			callBridge("showOptionMenu",{},null);
		},
		hideAllNonBaseMenuItem:function() {//隐藏所有非基础按钮接口
			callBridge("hideAllNonBaseMenuItem",{},null);
		},
		showAllNonBaseMenuItem:function() {//显示所有非基础按钮接口
			callBridge("showAllNonBaseMenuItem",{},null);
		},
		hideMenuItems:function(param){
			callBridge("hideMenuItems",param,null);
		},
		showMenuItems:function(param){
			callBridge("showMenuItems",param,null);
		},
		closeWindow:function(callback) {//关闭网页
			callBridge("closeWindow",{}, function(e){
				if(callback){
					callback(e);
				}
			});
		},  
		getNetworkType:function(callback) {//获取网络状态
			callBridge("getNetworkType", {},!callback?null: function(res){
				if(callback){
					callback(res);
				}
			});
		},
		getLocation:function(callback) {//获取地理位置
			callBridge("getLocation", {
				 type: 'wgs84'},function(e){
					 if(callback){
						 callback(e);
					 }
				 });
		},
		openLocation:function(param) {//打开位置地图
			callBridge("openLocation", param, null);
		},
		
		/**
		 * 图片
		 */
		chooseImage:function(param,callback) {
			callBridge("chooseImages", param, function(res){
				if(callback){
					callback(res);
				}
			});
		},
		previewImage:function(param,callback) {
			callBridge("previewImage", param,function(res){
				if(callback){
					callback(res);
				}
			});
		},
		uploadImage:function(param,callback) {
			callBridge("uploadImage", param, function(res){
				if(callback){
					callback(res);
				}
			});
		},
		downloadImage:function(params,callback) {
			callBridge("downloadImage", param, function(res){
				if(callback){
					callback(res);
				}
			});
		},
		/**
		 * 语音
		 */
		startRecord:function(callback) {
			callBridge("startRecord", {}, function(e){
				 if(callback){
					 callback(e);
					}
			});
		},
		stopRecord:function() {
			callBridge("stopRecord", {}, null);
		},
		playVoice:function(param) {
			callBridge("playVoice", param, null);
		},
		pauseVoice:function(param) {
			callBridge("pauseVoice", param, null);
		},
		stopVoice:function(param) {
			callBridge("stopVoice", param, null);
		},
		onVoicePlayEnd:function(param,callback) {
			callBridge("onVoicePlayEnd", param, function(e){
				 if(callback){
					 callback(e);
					}
			});
		},
		uploadVoice:function(param,callback) {
			callBridge("uploadVoice", param, function(e){
				 if(callback){
					 callback(e);
					}
			});
		},
		downloadVoice:function(param,callback) {
			callBridge("downloadVoice", param, function(e){
				 if(callback){
					 callback(e);
					}
			});
		},
		scanQRCode:function(param,callback) {
			callBridge("scanQRCode", param, function(e){
				 if(callback){
					 callback(e);
					}
			});
		},
		openProductSpecificView:function(param) {
			callBridge("openProductSpecificView", param, null);
		},
		/**
		 * 分享到朋友圈
		 */
		onMenuShareTimeline:function(param){
			packAndcallBridge("onMenuShareTimeline",param);
		},
		set timelineData( param) {
			packAndcallBridge("setShareTimelineData",param);
		},
		afterShareTimeline:undefined,
		onShareTimeline:undefined,
		/**
		 * 分享给朋友
		 */
		onMenuShareAppMessage:function(param){
			packAndcallBridge("onMenuShareAppMessage",param);
		},
		set appmessageData( param) {
			packAndcallBridge("setShareAppMessageData",param);
		},
		afterShareAppmessage:undefined,
		onShareAppmessage:undefined,
		/**
		 * 分享给qq
		 */
		onMenuShareQQ:function(param){
			packAndcallBridge("onMenuShareQQ",param);
		},
		set QQData( param) {
			packAndcallBridge("setShareQQData",param);
		},
		afterShareQQ:undefined,
		onShareQQ:undefined,
		/**
		 * 分享给微博
		 */
		onMenuShareWeibo:function(param){
			packAndcallBridge("onMenuShareWeibo",param);
		},
		set weiboData( param) {
			packAndcallBridge("setShareWeiboData",param);
		},
		afterShareWeibo:undefined,
		onShareWeibo:undefined,

		/**
		 * 分享给QQ空间
		 */
		onMenuShareQZone:function(param){
			packAndcallBridge("onMenuShareQZone",param);
		},
		set QZoneData( param) {
			packAndcallBridge("setShareQZoneData",param);
		},
		afterShareQZone:undefined,
		onShareQZone:undefined,
		editAddress:function(param,callback){
			editAddress(param,!callback?null:function(e){
				if(callback){
					callback(e);
				}
			});
		},
		chooseWXPay:function(param){
			chooseWXPay(param);
		},
		toAddrPage:function(openId){//TODO 跳转方式改变
			//location.href=getRootPath() +"/authAddress/check.do?type=pub&target_uri=pages%2ftrading%2fpayAddress.html%3fopenId%3d"+openId+"&param_name=accessToken";
		},
		getBrandWCPayRequest:function(param,callback){
			getBrandWCPayRequest(param,!callback?null:function(e){
				if(callback){
					callback(e);
				}	
			});
		},
		addCard:function(cardId){
			addCard(cardId);
		},
		refreshMenuSettings:function(){//刷新重置菜单
			refreshMenuSettings();
		}
	};
})();
weixin.init();