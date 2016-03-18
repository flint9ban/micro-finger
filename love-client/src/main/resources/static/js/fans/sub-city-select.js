$(function(){
    onItemClick();
});

function  onItemClick(){
    $(".item").click(function(){
        $(".item").attr("value","unselect");
        $(this).attr("value","select");
        $(".item .icon-select").hide();
        $(this).find(".icon-select").show();
       // location.href="linkPage.do?url=fans/city-select&type=city&tagIds="+$(this).attr("value");
    });
}
function getCityValue(){
    var oriData=sessionStorage.oriData;
    var dataArray=JSON.parse(oriData);
    var length=dataArray.length;
    var items=[];
    for(var j=0;j<length;j++){
        if(dataArray[j].categoryId!="7"){
            items.push(dataArray[j]);
        }
    }
    $('.item').each(function(index) {
        if($(this).attr('value')=="select"){
            var  name=$(this).find("div:first-child").html();
            var selectItem={
                id:$(this).attr('id'),
                name:name,
                categoryId:'7'
            };
            items.push(selectItem);
        }
    });
    sessionStorage.oriData=JSON.stringify(items);
    history.go(-2);
}