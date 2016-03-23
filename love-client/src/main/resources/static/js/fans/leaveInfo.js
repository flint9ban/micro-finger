$(function () {
    init();
});

function init() {
    initData();
    initEvent();
}
function initData() {
    if (sessionStorage.chooseArea) {
        initSessionData();
    } else {
        initAJaxData();
    }
}

function  initAJaxData(){
    $.ajax({
        type: 'POST',
        url: '../leaveInfo/getFanInfo.do',
        data: {
            'openId':getParamOfUrl('openId')
        },
        dataType: 'json',
        success: function (data, textStatus, jqXHR) {
            if (data.errMsg) {
                alert(data.errMsg);
                return;
            }
            sessionStorage.brands=JSON.stringify(data.brands);
            setBransSet(data.brands);
            setStoreSet(data.stores);
            if (data.fansInfo) {
                setValue(data.fansInfo);
            }
        }
    });
}

function  initSessionData(){
    $.ajax({
        type: 'POST',
        url: '../leaveInfo/getStores.do',
        data: {
            'city': sessionStorage.cityCode
        },
        dataType: 'json',
        success: function (data, textStatus, jqXHR) {
            setStoreSet(data);
            setValue(getSessionData());
        }
    });
}

function getSessionData(){
    var data = {
        name: sessionStorage.name,
        mobile: sessionStorage.mobile,
        orgType: sessionStorage.orgType,
        orgBrand: sessionStorage.orgBrand,
        orgPosition: sessionStorage.orgPosition,
        orgStore: sessionStorage.orgStore,
        orgArea: sessionStorage.orgArea,
        orgProvince: sessionStorage.provinceId,
        orgCity: sessionStorage.cityCode
    };
    var brands=JSON.parse(sessionStorage.brands);
    setBransSet(brands);
    return data;

}
function setBransSet(brands) {
    $('#orgBrand').html("");
    for (var i = 0; i < brands.length; i++) {
        var option = "<option value=\"" + brands[i].name + "\">" + brands[i].name + "</option>";
        $('#orgBrand').append(option)
    }
}

function setStoreSet(store) {
    $('#orgStore').html("");
    if(!store){
        return;
    }
    for (var i = 0; i < store.length; i++) {
        var option = "<option value=\"" + store[i].storeName + "\">" + store[i].storeName + "</option>";
        $('#orgStore').append(option)
    }
}

function saveInfo() {
    $.ajax({
        type: 'POST',
        url: '../leaveInfo/editFanInfo.do',
        data: {
            'openId': getParamOfUrl('openId'),
            'name': $("#name").val(),
            'mobile': $("#mobile").val(),
            'orgType': $("#orgType").val(),
            'orgBrand': $("#orgBrand").val(),
            'orgPosition': $("#orgPosition").val(),
            'orgStore': $("#orgStore").val(),
            'orgCity': $("#orgCity").val(),
            'orgArea': $("#orgArea").html(),
            'orgProvince': $("#orgProvince").val(),
        },
        dataType: 'json',
        success: function (data, textStatus, jqXHR) {
            if (data.errMsg) {
                humane.info(data.errMsg);
                return;
            }
            humane.info('保存成功');
        }
    });
}

function initEvent() {
    //  $("")
}

function setValue(data) {
    $("#name").val(data.name);
    $("#mobile").val(data.mobile);
    var type = data.orgType;
    if (type == "4S店") {
        showItems();
    }
    $("#orgType").val(data.orgType);
    $("#orgProvince").val(data.orgProvince);
    $("#orgCity").val(data.orgCity);
    $("#orgBrand").val(data.orgBrand);
    $("#orgPosition").val(data.orgPosition);
    $("#orgStore").val(data.orgStore);
    if (data.orgArea) {
        $("#orgArea").css('color', '#4d4d4d').html(data.orgArea);
    }
    setSelectText('orgType');
    setSelectText('orgBrand');
    setSelectText('orgPosition');
    setSelectText('orgStore');
}
function setSelectText(id) {
    var select = $("#" + id);
    if (!select.val()) {
        return;
    }
    var text = $("#" + id + "_div");
    text.css('color', '#4d4d4d').html(select.find("option:selected").text());
}

function showItems() {
    $("#area_item").show();
    $("#store_item").show();
    $("#area_hr").show();
    $("#store_hr").show();
}
function hideItems() {
    $("#area_item").hide();
    $("#store_item").hide();
    $("#area_hr").hide();
    $("#store_hr").hide();
}
function showText(item) {
    var div = $('#' + item + '_div');
    var select = $('#' + item);
    if(!select.val()||select.val()==""){
        div.css('color', '#a1a1a1').html("选择");
        return;
    }
    div.css('color', '#4d4d4d').html(select.find("option:selected").text());
}
function changeChoice(item) {
    if ($("#orgType").val() == "4S店") {
        showItems();
    } else {
        hideItems()
    }
    showText(item);
}

function toAreaChoic() {
    sessionStorage.chooseArea = true;
    sessionStorage.name = $("#name").val();
    sessionStorage.mobile = $("#mobile").val();
    sessionStorage.orgType = $("#orgType").val();
    sessionStorage.orgBrand = $("#orgBrand").val();
    sessionStorage.orgPosition = $("#orgPosition").val();
    sessionStorage.orgStore = $("#orgStore").val();
    sessionStorage.orgArea=$("#orgArea").html();
    sessionStorage.orgProvince=$("#orgProvince").val();
    sessionStorage.orgCity=$("#orgCity").val();
    location.href = "linkPage.do?url=fans/province-select&type=province";
}

function getParamOfUrl(param) {
    var url = window.location.href;
    var paraString = url.substring(url.indexOf("?") + 1, url.length).split("&");
    var paraObj = {};
    for (var i = 0; j = paraString[i]; i++) {
        paraObj[j.substring(0, j.indexOf("=")).toLowerCase()] = j.substring(j.indexOf("=") + 1, j.length);
    }
    var returnValue = paraObj[param.toLowerCase()];
    if (typeof(returnValue) == "undefined") {
        return "";
    } else {
        return returnValue;
    }
}