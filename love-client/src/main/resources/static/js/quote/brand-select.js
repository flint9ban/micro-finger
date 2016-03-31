$(function(){
    onItemClick();
});

function onItemClick(){
    $(".item").click(function(){
        toggleState($(this));
        location.href="linkPage.do?url=quote/carType-select&type=carType&id="+$(this).attr('id')+"&ids="+getCarTypeIds($(this).attr('id'));
    });
}
function getCarTypeIds(id){
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
