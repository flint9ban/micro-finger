$(function () {
    $("#search").autocomplete("find-type-name");
    $("#search1").autocomplete("find-type-name");
});

function query() {
    var params = {};
    params.tagName = $("search-text").val();
    params.typeIds=getTypes("#search-text")
    getAjax("query",params,reloadData);
}

function getTypes(id){
    var tags = $(id).tagGroup('getTags');
    var typeIds = "";
    if(tags.length>0){
        for(var tag in tags){
            typeIds +=tag.value+",";
        }
        typeIds.substr(0,params.typeIds.length-1)
    }
}

function getTypeNames(id){
    var tags = $(id).tagGroup('getTags');
    var typeIds = "";
    if(tags.length>0){
        for(var tag in tags){
            typeIds +=tag.text+",";
        }
        typeIds.substr(0,params.typeIds.length-1)
    }
}

function reloadData(datas){
    $('#tagTable').datagrid('reload',datas);
}

function saveTag() {
    if (operate == "add") {
        insertRow(getDlgRow());
    } else {
        updateRow(getDlgRow());
    }
    $('#dlg').dialog('close');
}

function saveCategory() {
    var categoryName = $("#dlgCategoryName").val();
    postAjax('createType', {name: categoryName}, function () {
        $('#category-dlg').dialog('close');
    });
}

function onClickCell(index, field, value) {
    if ('update' == field) {
        updateTag(index);
    }
    if ('delete' == field) {
        deleteRow(index);
    }
}

function showCtyDlg() {
    $("dlgCategoryName").textbox('setValue', '');
    $('#category-dlg').dialog('open').dialog('center').dialog('setTitle',
        '修改兴趣点');
}

function updateTag(index) {
    showDeatilDlg(index);

}
var operate = "add";
function showDeatilDlg(index) {
    operate = "add";
    if (index || index == '0') {
        operate = "edit";
        var rows = $('#tagTable').datagrid('getRows');
        var row = rows[index];
        $("#tagName").textbox('setValue', row.tagName);
        $("#tagCategory").combobox('setValue', row.categoryName);
        $("#rowIndex").val(index);
        $('#dlg').dialog('open').dialog('center').dialog('setTitle',
            '修改兴趣点');
    } else {
        $("#rowIndex").val('')
        $("#tagName").textbox('setValue', '');
        $("#tagCategory").combobox('setValue', '');
        $('#dlg').dialog('open').dialog('center').dialog('setTitle',
            '新增兴趣点');
    }

}

function getAddOrEdit() {
    return operate;
}

function getDlgRow() {
    var tagName = $("#tagName").textbox('getValue');
    var tagCategory = $("#tagCategory").textbox('getValue');
    var row = {
        index: $("#rowIndex").val(),
        tagName: tagName,
        categoryName: tagCategory
    }
    return row;
}

function updateRow(row) {

    var params = {};
    params.name = $("#search1-text").val();
    params.typeIds = getTypes("#search1-text");
    params.typeNames = getTypeNames("#search1-text");
    postAjax("updateTag",params,afterUpdateRow);
}

function afterUpdateRow(row){
    $('#tagTable').datagrid('updateRow', {
        index: row.index,
        row: {
            name: row.name,
            types: row.types
        }
    });
}

function insertRow(row) {
    var params = {};
    params.name = $("#search1-text").val();
    params.typeIds = getTypes("#search1-text");
    params.typeNames = getTypeNames("#search1-text");
    postAjax("createTag",params,afterInsertRow);
}

function afterInsertRow(param,data){
    $('#tagTable').datagrid('appendRow', {
        name: param.name,
        types: param.types,
        id:data.id,
        update: '',
        del: ''
    });
}

function deleteRow(index) {
    var param = {};
    param.id = id;
    postAjax("deleteTag",param,afterDeleteRow);

}

function afterDeleteRow(index){
    $('#tagTable').datagrid('deleteRow', index)
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
                successCallback(data)
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
        type: "POST",
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