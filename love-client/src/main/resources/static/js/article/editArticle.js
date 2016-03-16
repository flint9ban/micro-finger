/**
 *
 * Created by liyi on 16/3/16.
 */

$(function () {
    $("#search").autocomplete("findTagByName");
});

function save(){
    var param = {};
    param.articleId=1;
    param.tip=$('#tip').textbox('getValue');
    param.url=$('#linkText').textbox('getValue');
    postAjax("update",param,function(){

    })
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