/**
 *
 * Created by liyi on 16/3/16.
 */

$(function () {
    $("#search").autocomplete("findTagByName");
    init();
});

function init(){
    var param = {}
    param.articleId = $('#articleId').text();
    getAjax('getArticle',articleId,initPG);
}

function initPG(data){
    if(data.article.tip){
        $('#tip').textbox('setValue',data.article.tip);
    }
    if(data.article.url){
        $('#url').textbox('setValue',data.article.url);
    }
    $("#search").restAutocomplete(data.tags);
}

function save(){
    var param = {};
    param.articleId=$('#articleId').val();
    param.tip=$('#tip').textbox('getValue');
    param.url=$('#linkText').textbox('getValue');
    param.tagIds = getTypes('#search');
    postAjax("update",param,function(){
        window.close();
    })
}

function cancelSave(){
    window.close();
}

function removeTags(target){
    var containerId = $(target).siblings('.hiddenId').val();
    var param ={};
    param.containerId = containerId;
    $(target).parents('.tag-div').remove();
    postAjax('removeContainer',param);
}

function selectTags(target){
    var spans = $(target).siblings('span');
    var tags = [];
    for(var i=0;i<spans.length;i++){
        var span = spans[i];
        tags.push({'text':$(span).text(),'value':$(span).attr('value')})
    }
    $("#search").resetAutoComplete(tags);
}

function setCommon(){
    var typeIds = getTypes('#search');
    var param = {};
    param.tagIds =typeIds;
    postAjax('setCommon',param,addCommon);
}

function addCommon(data){
    var containerId = data.containerId;
    var div='<div class="tag-div "><input type="radio" name="gen-radio" value="1001" onclick="select(this)"/>';
    var tags = $('#search').tagGroup('getTags');
    for(var i=0;i<tags.length;i++){
        var tag = tags[i];
        div+='<span class="tag-span" value="'+tag.value+'">'+tag.text+'</span>';
    }
    div+='<input class="hiddenId" type="hidden" value="'+containerId+'"/>';
    div+='<a class="easyui-linkbutton" onclick="remove(this)" style="font-size:20px;" data-options="plain:true,iconCls:\'icon-cancel\'">' +
        '<span class="l-btn-left l-btn-icon-left"><span class="l-btn-text l-btn-empty">&nbsp;</span><span class="l-btn-icon icon-cancel">&nbsp;</span></span></a></div>'
    $('#p').append(div);
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
                    successCallback(data);
                }

            }
        },
        error: function () {
            $.messager.alert('错误', '系统错误，请联系管理员', 'error');
        }
    });
}

function getAjax(postUrl, postData, successCallback) {
    $.ajax({
        type: "GET",
        url: postUrl,
        dataType: "json",
        data: postData,
        success: function (data) {
            if (data.error) {
                showError(data.error);
                //$.messager.alert('错误',data.error,'error');
            } else {
                if(successCallback){
                    successCallback(data);
                }

            }
        },
        error: function () {
            $.messager.alert('错误', '系统错误，请联系管理员', 'error');
        }
    });
}