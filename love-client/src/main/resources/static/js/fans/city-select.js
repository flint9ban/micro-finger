$(function(){
    onItemClick();
});

function  onItemClick(){
    $(".item").click(function(){
        $(".item").attr("value","unselect");
        $(this).attr("value","select");
        $(".item .icon-select").hide();
        $(this).find(".icon-select").show();
       // location.href="linkPage.do?url=fans/city-select&type=city&tagIds="+$(this).attr("value");
    });
}
function getCityValue(){
    $('.item').each(function(index) {
        if($(this).attr('value')=="select"){
            sessionStorage.cityId=$(this).attr("id");
        //   sessionStorage.cityName=$(this).find("div:first-child").html();
            sessionStorage.cityCode=$(this).find("input").val()
            sessionStorage.orgArea= sessionStorage.provinceName + "-" + $(this).find("div:first-child").html();
            history.go(-2);
        }
    });
}