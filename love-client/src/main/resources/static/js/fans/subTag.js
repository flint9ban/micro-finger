$(function(){
    if(sessionStorage.jump){
        if(sessionStorage.oriData){
            var data=JSON.parse(sessionStorage.oriData);
            showValues(data);
        }
    }else{
        $.ajax({
            type : 'POST',
            url : 'queryFanTags.do',
            data : {
                'openId':getParamOfUrl('openId')
            },
            dataType : 'json',
            success : function(data, textStatus, jqXHR) {
                sessionStorage.oriData=JSON.stringify(data);
                showValues(data);
            }
        });
    }
});
function jumpChoosePg(param){
    sessionStorage.jump=true;
    if(param=="dataType"){
        var tagIds=$("#dataType").data("ids");
        location.href="../subTag/linkPage.do?url=fans/subTag-type&type=dataType&tagIds="+tagIds;
    }
    if(param=="dataBrand"){
        var tagIds=$("#brand").data("ids");
        location.href="../subTag/linkPage.do?url=fans/subTag-brand&type=brand&tagIds="+tagIds;
    }
    if(param=="dataArea"){
        location.href="../subTag/linkPage.do?url=fans/sub-province-select&type=province";
    }
}
var ids=""
function showValues(data){
    ids="";
    var typeArray=[];
    var brandArray=[];
    var areaArray=[];
    for(var i=0;i<data.length;i++) {
        ids+=data[i].id+","
        var span = $("<span>").addClass("tagSpan").html(data[i].name);
        $("#select-tags").append(span);
        if (data[i].categoryId == "5") {//数据类型
            typeArray.push(data[i]);
        }
        if (data[i].categoryId == "6") {//品牌
            brandArray.push(data[i]);
        }
        if (data[i].categoryId == "7") {//地区
            areaArray.push(data[i]);
        }
    }
        setItemValue( $("#dataType"),typeArray);
        setItemValue( $("#brand"),brandArray);
        setItemValue( $("#area"),areaArray);

}
function setItemValue(obj,data){
    var fmtData=fmtItemData(data);
    obj.data("ids",fmtData.ids)
    if(!fmtData.names){
        obj.css("color",'#a1a1a1').html("选择");
        return;
    }
    obj.addClass(".item-show").html(fmtData.names);
  ;
}
function fmtItemData(data){
    var names="";
    var ids="";
    for(var i=0;i<data.length;i++){
        names+=  data[i].name+",";
        ids+=  data[i].id+",";
    }
    var itemData={
            names:names.length>0?names.substr(0,names.length-1):"",
            ids:ids.length>0?ids.substr(0,ids.length-1):""
    }
    ;
    return itemData;
}

function editFansTag(){
    $.ajax({
        type : 'POST',
        url : '../subTag/editFanTags.do',
        data : {
            'openId':getParamOfUrl("openId"),
            'tagIds':ids

        },
        dataType : 'json',
        success : function(data, textStatus, jqXHR) {
            humane.info('订阅成功');
        }
    });

}

function getCurrTagIds(){
    return ids.length>0?ids.substr(0,ids.length-1):"";
}

function getParamOfUrl(param) {
    var url = window.location.href;
    var paraString = url.substring(url.indexOf("?") + 1, url.length).split("&");
    var paraObj = {};
    for (var i = 0; j = paraString[i]; i++) {
        paraObj[j.substring(0, j.indexOf("=")).toLowerCase()] = j.substring(j.indexOf("=") + 1, j.length);
    }
    var returnValue = paraObj[param.toLowerCase()];
    if (typeof(returnValue) == "undefined") {
        return "";
    } else {
        return returnValue;
    }
}