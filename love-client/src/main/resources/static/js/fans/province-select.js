$(function(){
 onItemClick();
});

function  onItemClick(){
    $(".item").click(function(){
        $(".item .icon-select").hide();
        $(this).find(".icon-select").show();
        sessionStorage.provinceId=$(this).attr("id");
        sessionStorage.provinceName=$(this).find("div:first-child").html();
        location.href="linkPage.do?url=fans/city-select&type=city&tagIds="+$(this).attr("value");
    });
}