Array.prototype.remove=function(dx)
{
    if(isNaN(dx)||dx>this.length){return false;}
    for(var i=0,n=0;i<this.length;i++)
    {
        if(this[i]!=this[dx])
        {
            this[n++]=this[i]
        }
    }
    this.length-=1
}
$(function(){
    onItemClick();
})

function onItemClick(){
    $(".item").click(function(){
        toggleState($(this));
    });
}

function toggleState(obj){
    var itemValue=obj.attr("value");
    if(itemValue=='select'){
        hideIcon(obj);
        deleteTags(obj)
    }else{
        showIcon(obj);
        appendTags(obj);
    }
}
function appendTags(obj){
   // var text=obj.find("div:first-child").html();
   // var value=obj.attr("id");
   //$("#taggroup").tagGroup('appendTag', {
   //     value: value,
   //     text: text
   // });
    $("#taggroup").html("");
    $("#taggroup").tagGroup({
        tags:getSelectTags()
    });
}
function deleteTags(obj){
    var text=obj.find("div:first-child").html();
    var value=obj.attr("id");
    $("#taggroup").html("");

    $("#taggroup").tagGroup({
        tags:getSelectTags()
    });

}

function getSelectTags(){
    var items=[];
    $('.item').each(function(index) {
        if($(this).attr('value')=="select"){
            var  name=$(this).find("div:first-child").html();
            var selectItem={
                value:$(this).attr('id'),
                text:name
            };
            items.push(selectItem);
        }
    });
    return items;
}

function  showIcon(obj){
    obj.attr('value','select');
    obj.find(".icon-select").html("<img src=\"../img/select_confirm.png\" class=\"img-icon\"/>");
}
function  hideIcon(obj){
    obj.attr('value','unselect');
    obj.find(".icon-select").html("");
}
function getSelectValues(){
    var oriData=sessionStorage.oriData;
    var dataArray=JSON.parse(oriData);
    var length=dataArray.length;
    var items=[];
    for(var j=0;j<length;j++){
        if(dataArray[j].categoryId!="6"){
            items.push(dataArray[j]);
        }
    }
    var length=0;
    $('.item').each(function(index) {
        if($(this).attr('value')=="select"){
            length++;
            var  name=$(this).find("div:first-child").html();
            var selectItem={
                id:$(this).attr('id'),
                name:name,
                categoryId:'6'
            };
            items.push(selectItem);
        }
    });
    if(length>3){
        humane.info('最多只能订阅3个品牌');
        return ;
    }
    sessionStorage.oriData=JSON.stringify(items);
    history.go(-1);
}