$(function(){
    var data=[
        {code:'001',
            name:'安徽省'
        },
        {
            code:'002',
            name:'浙江省'
        },
        {
            code:'003',
            name:'四川省'
        }
    ];
    showValues(data);
});
function showValues(data){
    var items=[];
    var selectItem="002";
    for(var i=0;i<data.length;i++){
        var item={};
        item.name=data[i].name;
        item.code=data[i].code;
   //     if(item.code==selectItem){
            item.display="display:none";
    //    }
        items.push(item)
    }
    TempList.initItemUIs(items,"context","tmpl-province-group");
    onItemClick();
}

function  onItemClick(){
    /*	$('.tagSpan').each(function(index) {
     var tag={
     value:$(this).data('value'),
     text:$(this).html()
     }
     tags.push(tag);
     });*/

    $(".item").click(function(){
        $(".item .icon-select").hide();
        var id=$(this).data("id");
        var icon=$('#'+id).show();
        location.href="city-select.html?code="+id;
    });
}