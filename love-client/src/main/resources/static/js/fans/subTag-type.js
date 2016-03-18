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
    }else{
        showIcon(obj);
    }
}
function  showIcon(obj){
    obj.attr('value','select');
    obj.find(".icon-select").html("√");
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
        if(dataArray[j].categoryId!="5"){
            items.push(dataArray[j]);
        }
    }
    $('.item').each(function(index) {
        if($(this).attr('value')=="select"){
           var  name=$(this).find("div:first-child").html();
            var selectItem={
                id:$(this).attr('id'),
                name:name,
                categoryId:'5'
            };
            items.push(selectItem);
        }
    });
    sessionStorage.oriData=JSON.stringify(items);
    history.go(-1);
}