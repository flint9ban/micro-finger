$(function () {
    $("#search").autocomplete("find-type-name");
    $("#search1").autocomplete("find-type-name");
});

function query() {
    var params = {};
    params.tagName = $("#queryTagName").textbox("getValue");
    params.typeIds=getTypes("#search")
    getAjax("query",params,reloadData);
}

function reloadData(datas){
    $('#tagTable').datagrid('reload',datas);
}

function getTypes(id){
    var tags = $(id).tagGroup('getTags');
    var typeIds = "";
    if(tags.length>0){
        for(var i=0;i<tags.length;i++){
            typeIds +=tags[i].value+",";
        }
        typeIds = typeIds.substr(0,typeIds.length-1)
    }
    return typeIds;
}

function getTypeNames(id){
    var tags = $(id).tagGroup('getTags');
    var typeNames = "";
    if(tags.length>0){
        for(var i=0;i<tags.length;i++){
            typeNames +=tags[i].text+",";
        }
        typeNames = typeNames.substr(0,typeNames.length-1)
    }
    return typeNames;
}

function showCtyDlg() {
    $("#dlgCategoryName").textbox('setValue', '');
    $('#category-dlg').dialog('open').dialog('center').dialog('setTitle',
        '新增类型');
}

function saveCategory() {
    var categoryName = $("#dlgCategoryName").val();
    postAjax('createType', {name: categoryName}, function () {
        $('#category-dlg').dialog('close');
    });
}


var operate = "add";

function onClickCell(index, field, value) {
    if ('update' == field) {
        updateTag(index);
    }
    if ('delete' == field) {
        deleteRow(index);
    }
}

function updateTag(index) {
    showDeatilDlg(index);
}

function deleteRow(index) {
    var rows = $('#tagTable').datagrid('getRows');
    var row = rows[index];
    var param = {};
    param.id = row.id;
    postAjax("deleteTag",param);
    $('#tagTable').datagrid('deleteRow',index);
}


function showDeatilDlg(index) {
    operate = "add";
    if (index || index == '0') {
        operate = "edit";
        var rows = $('#tagTable').datagrid('getRows');
        var row = rows[index];
        $("#tagName").textbox('setValue', row.name);
        $('#search1').resetAutoComplete(getTagsByString(row.typeIds,row.typeNames));
        $("#tagId").val(row.id);
        $("#rowIndex").val(index);
        $('#dlg').dialog('open').dialog('center').dialog('setTitle',
            '修改兴趣点');
    } else {
        $("#rowIndex").val('');
        $("#tagId").val('')
        $("#tagName").textbox('setValue', '');
        $('#search1').resetAutoComplete([]);
        $('#dlg').dialog('open').dialog('center').dialog('setTitle',
            '新增兴趣点');
    }

}

function saveTag() {
    var param = getTagData();
    if (operate == "add") {
        postAjax("createTag",param,insertRow);
    } else {
        postAjax("updateTag",param,updateRow);
    }
    $('#dlg').dialog('close');
}


function insertRow(param,data) {
    $('#tagTable').datagrid('insertRow', {
        index:1,
        row:{
            name: param.tagName,
            typeNames: param.typeNames,
            typeIds:param.typeIds,
            id:data.id,
            update: '',
            del: ''
        }
    });
}

function updateRow(param,data) {
    $('#tagTable').datagrid('updateRow', {
        index: param.index,
        row: {
            name: param.tagName,
            typeNames: param.typeNames,
            typeIds:param.typeIds,
            id: param.tagId
        }
    });
}


function getTagData() {
    var tagName = $("#tagName").textbox('getValue');
    var typeIds = getTypes("#search1");
    var typeNames = getTypeNames("#search1");
    var params= {};
    params.tagName = tagName;
    if(operate=='edit'){
        params.tagId=$("#tagId").val();
        params.index = $("#rowIndex").val();
    }
    params.typeIds = typeIds;
    params.typeNames = typeNames;
    return params;
}




function getTagsByString(tagIds,tagNames){
    var tags = [];
    if(tagIds){
        var tagIdArray = tagIds.split(",");
        var tagNameArray = tagNames.split(",");
        for(var i=0;i<tagIdArray.length;i++){
            var tagId = tagIdArray[i];
            var tagName = tagNameArray[i];
            var tag = {};
            tag.text = tagName;
            tag.value = tagId;
            tags.push(tag);
        }
    }
    return tags;
}

function getAddOrEdit() {
    return operate;
}

function fmtUpdateIcon(value, row, index) {
    return "<span  style='cursor: pointer;font-size:24px;' class=\"icon-edit\" ;\">&nbsp;&nbsp;&nbsp;</span>";
}
function fmtDelIcon(value, row, index) {
    return "<span  style='cursor: pointer;font-size:24px;' class=\"icon-cancel\";\">&nbsp;&nbsp;&nbsp;</span>";
}

function postAjax(postUrl, postData, successCallback) {
    $.ajax({
        type: "POST",
        url: postUrl,
        dataType: "json",
        data: postData,
        success: function (data) {
            if (data.error) {
                showError(data.error);
                //$.messager.alert('错误',data.error,'error');
            } else {
                if(successCallback){
                    successCallback(postData,data);
                }
            }

        },
        error: function () {
            $.messager.alert('错误', '系统错误，请联系管理员', 'error');
        }
    });
}

function postAjaxText(postUrl, postData, successCallback) {
    $.ajax({
        type: "POST",
        url: postUrl,
        dataType: "text",
        data: postData,
        success: function (data) {
            if (data.error) {
                showError(data.error);
                //$.messager.alert('错误',data.error,'error');
            } else {
                successCallback(postData,data)
            }
        },
        error: function () {
            $.messager.alert('错误', '系统错误，请联系管理员', 'error');
        }
    });
}

function getAjax(getUrl, getData, successCallback) {
    $.ajax({
        type: "GET",
        url: getUrl,
        dataType: "json",
        data: getData,
        success: function (data) {
            if (data.error) {
                showError(data.error);
                //$.messager.alert('错误',data.error,'error');
            } else {
                successCallback(data)
            }
        },
        error: function () {
            $.messager.alert('错误', '系统错误，请联系管理员', 'error');
        }
    });
}

function showError(error) {
    $.messager.alert('错误', error, 'error');
}