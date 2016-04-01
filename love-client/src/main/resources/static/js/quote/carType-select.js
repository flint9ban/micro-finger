$(function () {
    onItemClick();
    setShareData();
});

function onItemClick() {
    $(".item").click(function () {
        toggleState($(this));
    });
}

function toggleState(obj) {
    var itemValue = obj.attr("value");
    if (itemValue == 'select') {
        hideIcon(obj);
    } else {
        showIcon(obj);
    }
}
function showIcon(obj) {
    obj.attr('value', 'select');
    obj.find(".icon-select").html("<img src=\"../img/select_confirm.png\" class=\"img-icon\"/>");
}
function hideIcon(obj) {
    obj.attr('value', 'unselect');
    obj.find(".icon-select").html("");
}
function getSelectValues() {
    var compCarTypeInfo = getSingleBrandCarTypes();
    if (compCarTypeInfo == null) {
        deleteCarType();
        history.go(-2);
        return;
    }
    var compCarTypeInfos = getAllBrandCarTypes(compCarTypeInfo);
    if (!valdateLength(compCarTypeInfos)) {
        humane.info('最多只能选择5个车系！');
        return;
    }
    sessionStorage.compCarTypeInfo = JSON.stringify(compCarTypeInfos);
    history.go(-2);
}
function deleteCarType() {
    var compCarTypeInfos = JSON.parse(sessionStorage.compCarTypeInfo);
    if (!compCarTypeInfos) {
        return;
    }
    var length = compCarTypeInfos.length;
    for (var i = 0; i < length; i++) {
        if (compCarTypeInfos[i].brandId == $("#brandId").val()) {
            compCarTypeInfos.splice(i, 1);
            break;
        }
    }
    sessionStorage.compCarTypeInfo = JSON.stringify(compCarTypeInfos);
}

function getSingleBrandCarTypes() {
    var typeNames = "";
    var ids = "";
    $('.item').each(function (index) {
        if ($(this).attr('value') == "select") {
            var name = $("#brandName").val() + "-" + $(this).find("div:first-child").html();
            var id = $(this).attr('id');
            ids += id + ",";
            typeNames += name + ","
        }
    });
    if (ids == "") {
        return null;
    }
    return {
        brandId: $("#brandId").val(),
        carTypeIds: (ids == "" ? "" : ids.substr(0, ids.length - 1)),
        carTypeNames: (typeNames == "" ? "" : typeNames.substr(0, typeNames.length - 1))
    }
}

function getAllBrandCarTypes(compCarTypeInfo) {
    var compCarTypeInfos;
    if(sessionStorage.compCarTypeInfo!="undefined"){
        compCarTypeInfos=JSON.parse(sessionStorage.compCarTypeInfo)
    }else{
        compCarTypeInfos= [];
    }
    var exist = false;
    for (var i = 0; i < compCarTypeInfos.length; i++) {
        if (compCarTypeInfos[i].brandId == compCarTypeInfo.brandId) {
            compCarTypeInfos[i].carTypeIds = compCarTypeInfo.carTypeIds;
            compCarTypeInfos[i].carTypeNames = compCarTypeInfo.carTypeNames;
            exist = true;
            break;
        }
    }
    if (!exist) {
        var compCarTypeInfo = {
            brandId: compCarTypeInfo.brandId,
            carTypeIds: compCarTypeInfo.carTypeIds,
            carTypeNames: compCarTypeInfo.carTypeNames
        };
        compCarTypeInfos.push(compCarTypeInfo);
    }
    return compCarTypeInfos;
}

function valdateLength(compCarTypeInfos) {
    var length = 0;
    for (var i = 0; i < compCarTypeInfos.length; i++) {
        var ids = compCarTypeInfos[i].carTypeIds.split(",");
        length += ids.length;
    }

    return length <= 5;

}

function setShareData() {
    var shareData = {
        title : sessionStorage.storeName+'：如何从报价上抢占客户',
        desc : '直击客户心坎的报价技巧，看完这篇就够了',
        link : sessionStorage.appUrl+'/auth/check?scope=snsapi_base&target_uri=quoteHome%2finit%3fmemberId%3d'+sessionStorage.memberId+'&redirect;',
        img_url : sessionStorage.appUrl + '/img/share.jpg'
    };
    weixin.showOptionMenu();
    weixin.onMenuShareAppMessage(shareData);
    weixin.onMenuShareTimeline(shareData);
}