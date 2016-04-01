$(function(){
    onItemClick();
    setShareData()
});

function onItemClick(){
    $(".item").click(function(){
        toggleState($(this));
        location.href="linkPage.do?url=quote/carType-select&type=carType&id="+$(this).attr('id')+"&ids="+getCarTypeIds($(this).attr('id'));
    });
}
function getCarTypeIds(id){
    if(sessionStorage.compCarTypeInfo=="undefined"){
        return "";
    }
    var compCarTypeInfo=JSON.parse( sessionStorage.compCarTypeInfo);
    var carTypeIds="";
    for(var i=0;i<compCarTypeInfo.length;i++){
       if(compCarTypeInfo[i].brandId==id){
           carTypeIds=compCarTypeInfo[i].carTypeIds;
           break;
       }
    }
    return carTypeIds;
}

function toggleState(obj){
    var itemValue=obj.attr("value");
    if(itemValue=='select'){
        hideIcon(obj);
    }else{
        showIcon(obj);
    }
}

function  showIcon(obj){
    obj.attr('value','select');
    obj.find(".icon-select").html("<img src=\"../img/select_confirm.png\" class=\"img-icon\"/>");
}
function  hideIcon(obj){
    obj.attr('value','unselect');
    obj.find(".icon-select").html("");
}

function setShareData() {
    var shareData = {
        title : sessionStorage.storeName+'：如何从报价上抢占客户',
        desc : '直击客户心坎的报价技巧，看完这篇就够了',
        link : sessionStorage.appUrl+'/auth/check?scope=snsapi_base&target_uri=quoteHome%2finit%3fmemberId%3d'+sessionStorage.memberId+'&redirect;',
        img_url : sessionStorage.appUrl + '/img/share.jpg'
    };
    weixin.showOptionMenu();
    weixin.onMenuShareAppMessage(shareData);
    weixin.onMenuShareTimeline(shareData);
}
