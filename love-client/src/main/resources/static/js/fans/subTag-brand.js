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
    var data=[
        {id:'001',
            name:'东风日产',
        },
        {
            id:'002',
            name:'上海通用',
        },
        {
            id:'003',
            name:'广汽三菱',
        }
    ];
    showValues(data);
})
function showValues(data){
    var selectItem=[];
    var oriData=sessionStorage.oriData;
    var dataArray=JSON.parse(oriData);
    for(var j=0;j<dataArray.length;j++){
        if(dataArray[j].categoryId=="002"){
            selectItem.push(dataArray[j]);
        }
    }
    var items=[];
    for(var i=0;i<data.length;i++){
        var item={};
        item.name=data[i].name;
        item.id=data[i].id;
        for(var j=0;j<selectItem.length;j++){
            if(item.id==selectItem[j].id){
                item.state="select";
                item.display="display:block";
            }
        }
        items.push(item)
    }
    TempList.initItemUIs(items,"context","tmpl-tag-group");
    onItemClick();

}

function onItemClick(){
    $(".item").click(function(){
        toggleState($(this));
    });
}

function toggleState(obj){
    if(obj.data('state')=='select'){
        hideIcon(obj);
    }else{
        showIcon(obj);
    }
}
function  showIcon(obj){
    obj.data('state','select');
    var id=obj.data("id");
    var iconObj=$('#'+id);
    var icon=iconObj.show();
}
function  hideIcon(obj){
    obj.data('state','unselect');
    var id=obj.data("id");
    var iconObj=$('#'+id);
    var icon=iconObj.hide();
}
function getSelectValues(){
    var items=[];
    var oriData=sessionStorage.oriData;
    var dataArray=JSON.parse(oriData);
    for(var j=0;j<dataArray.length;j++){
        if(dataArray[j].categoryId!="002"){
           // dataArray.remove(j);
            items.push(dataArray[j]);
        }
    }
    $('.item').each(function(index) {
        if($(this).data('state')=="select"){
            var selectItem={
                id:$(this).data('id'),
                name:$(this).data('name'),
                categoryId:'002'
            };
            items.push(selectItem);
        }
    });
    sessionStorage.oriData=JSON.stringify(items);
    history.go(-1);
}