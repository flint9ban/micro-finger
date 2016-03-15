$(function(){
    var data=[
        {	id:'001',
            name:'杭州市'
        },
        {
            id:'002',
            name:'宁波市'
        },
        {
            id:'003',
            name:'温州市'
        }
    ];
    showValues(data);
});
function showValues(data){
    var items=[];
    //	var selectItem="002"
    for(var i=0;i<data.length;i++){
        var item={};
        item.name=data[i].name;
        item.id=data[i].id;
//				if(item.code==selectItem){
//					item.display="display:block";
//				}
        items.push(item)
    }
    TempList.initItemUIs(items,"context","tmpl-province-group");
    onItemClick();
}

function  onItemClick(){
    $(".item").click(function(){
        $(".item").data('state','unselected');
        $(this).data('state','select');
        $(".item .icon-select").hide();
        var id=$(this).data("id");
        var icon=$('#'+id).show();
    });
}
function getCityValue(){
    var items=[];
    var oriData=sessionStorage.oriData;
    var dataArray=JSON.parse(oriData);
    for(var j=0;j<dataArray.length;j++){
        if(dataArray[j].categoryId!="003"){
           // dataArray.remove(j);
            items.push(dataArray[j]);
        }
    }
    $('.item').each(function(index) {
        if($(this).data('state')=="select"){
            var selectItem={
                id:$(this).data('id'),
                name:$(this).data('name'),
                categoryId:'001'
            };
            items.push(selectItem);
        }
    });
    sessionStorage.oriData=JSON.stringify(items);
    history.go(-2);
}