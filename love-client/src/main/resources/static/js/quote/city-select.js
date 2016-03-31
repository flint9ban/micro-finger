$(function(){
    onItemClick();
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