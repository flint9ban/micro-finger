$(function(){
    initQueryParams();
});

function initQueryParams(){
    $("#search").autocomplete("../tag/findTagByName");
    $('#orgType').combobox({
        onSelect: function (rec) {
           if(rec.value=="4S店"){
               showArea();
           }else{
               hideArea();
           }
        }
    });
    $('#orgBrand').combobox({
        valueField: 'name',
        textField: 'name',
        url: 'getFansQueryParms.do?type=brand'
    });
    $('#province').combobox({
        valueField: 'id',
        textField: 'name',
        url: 'getFansQueryParms.do?type=province',
        onSelect: function (rec) {
            var url = 'getFansQueryParms.do?type=city&id=' + rec.id;
            $('#city').combobox('reload', url);
        }
    });
    $('#city').combobox({
        valueField: 'id',
        textField: 'name',
        onSelect: function (rec) {
            var url = 'getFansQueryParms.do?type=store&id=' + rec.id;
            $('#store').combobox('reload', url);
        }
    });
    $('#store').combobox({
        valueField: 'storeId',
        textField: 'storeName'});
}
function showArea(){
    $('#province_span').show();
    $('#city_span').show();
    $('#store_span').show();
}
function hideArea(){
    $('#province_span').hide();
    $('#city_span').hide();
    $('#store_span').hide();
}

function queryFans(){
    var param={
        name: $("#fpwName").textbox('getValue'),
        orgType: $("#orgType").combobox('getValue'),
        orgBrand: $("#orgBrand").combobox('getValue'),
        orgPosition: $("#orgPosition").combobox('getValue'),
        orgProvince: $("#province").combobox('getValue'),
        orgCity: $("#city").combobox('getValue'),
        orgStore: $("#store").combobox('getValue'),
        tagIds:getTagIds()
}
    $('#fansTable').datagrid('load',param);
}

function  showAticleDlg(){
    $('#allsend').window('open');
    $("#articleTb").datagrid("unselectAll");
}

function getTagIds(){
    var tags = $("#search-text").tagGroup('getTags');
    var typeIds = "";
    if(tags.length>0){
        for(var i=0;i<tags.length;i++){
            typeIds +=tags[i].value+",";
        }
        typeIds = typeIds.substr(0,typeIds.length-1)
    }
    return typeIds;
}

function sendAticle(){
    var row=$("#articleTb").datagrid("getSelected");
    if(!row){
        $.messager.alert('警告','请选择要发送的文章','error');
    }
    $.ajax({
        type : 'POST',
        url : 'sendArticles.do',
        data : {
            mediaId:row.mediaId,
            articleId:row.id,
            name: $("#fpwName").textbox('getValue'),
            orgType: $("#orgType").combobox('getValue'),
            orgBrand: $("#orgBrand").combobox('getValue'),
            orgPosition: $("#orgPosition").combobox('getValue'),
            orgProvince: $("#province").combobox('getValue'),
            orgCity: $("#city").combobox('getValue'),
            orgStore: $("#store").combobox('getValue'),
            tagIds:getTagIds()
        },
        dataType : 'json',
        success : function(data, textStatus, jqXHR) {
           if(data.errMsg){
               $.messager.alert('失败',data.errMsg,'error');
           }
            $('#allsend').dialog('close');
        }
    });

}