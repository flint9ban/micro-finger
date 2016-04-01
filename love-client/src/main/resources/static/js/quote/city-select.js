$(function(){
    onItemClick();
    setShareData();
});

function  onItemClick(){
    $(".item").click(function(){
        $(".item").attr("value","unselect");
        $(this).attr("value","select");
        $(".item .icon-select").hide();
        $(this).find(".icon-select").show();

        var regionType=$("#cityType").val();
        if(regionType=="self-city"){
            sessionStorage.selfCityId=$(this).attr("id");
            sessionStorage.selfCityCode=$(this).find("input").val();
            sessionStorage.selfArea= sessionStorage.selfProvinceName + "-" + $(this).find("div:first-child").html();
            sessionStorage.selfLevel=3;
        }else{
            sessionStorage.compCityId=$(this).attr("id");
            sessionStorage.compCityCode=$(this).find("input").val();
            sessionStorage.compArea= sessionStorage.compProvinceName + "-" + $(this).find("div:first-child").html();
            sessionStorage.compLevel=3;
        }

        history.go(-2);
    });

    $("#all_city").click(function(){
        var regionType=$("#cityType").val();
        if(regionType=="self-city"){
            sessionStorage.selfArea= sessionStorage.selfProvinceName;
            sessionStorage.selfLevel=2;
        }else{
            sessionStorage.compArea= sessionStorage.compProvinceName;
            sessionStorage.compLevel=2;
        }

        history.go(-2);
    });
}
function getCityValue(){
    $('.item').each(function(index) {
        if($(this).attr('value')=="select"){
            sessionStorage.cityId=$(this).attr("id");
            sessionStorage.cityCode=$(this).find("input").val()
            sessionStorage.orgArea= sessionStorage.provinceName + "-" + $(this).find("div:first-child").html();
            history.go(-2);
        }
    });
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