var TempList=function(){
	var itemObject=[];
	var singleItem=null;
	var innerDivId="";
	var tempId="";
	var initItemUIs = function() {
		var innerDivObj=$('#'+TempList.innerDivId);
		var tempObj=$('#'+TempList.tempId);
		for(var i=0;i<itemObject.length;i++){
			createItemUI(itemObject[i], tempObj.html(), innerDivObj);
		}
	};
	var initSingleItemUI = function() {
		var innerDivObj=$('#'+TempList.innerDivId);
		var tempObj=$('#'+TempList.tempId);
		createHeadItem(singleItem, tempObj.html(), innerDivObj);
	};
	
	function createHeadItem(data, temp, dietList){
		for (var filed in data) {
			var value=data[filed];
			temp = bindTempValue(temp,filed, value);
		}
		var itemUI = $(temp);
		dietList.prepend(itemUI);
		return itemUI;
	}
	
	function createItemUI(data, temp, dietList){
		for (var filed in data) {
			var value=data[filed];
			temp = bindTempValue(temp,filed, value);
		}
		var itemUI = $(temp);
		dietList.append(itemUI);
		return itemUI;
	}

	function bindTempValue(temp, name, value) {
		var reg = new RegExp('\\{\\%\\s*' + name + '\\s*\\%\\}', 'g');
		return temp.replace(reg, value);
	}
	return {
		initItemUIs:function(data,innerDivId,tempId){
			if(!data||!innerDivId||!tempId){
				return ;
			}
			itemObject=data;
			this.innerDivId=innerDivId;
			this.tempId=tempId;
			initItemUIs();
		},
		initSingleItemUI:function(data,innerDivId,tempId){
			if(!data||!innerDivId||!tempId){
				return ;
			}
			singleItem=data;
			this.innerDivId=innerDivId;
			this.tempId=tempId;
			initSingleItemUI();
		}
	}
}();