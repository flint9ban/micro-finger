$(function(){
 onItemClick();
});

function  onItemClick(){
    $(".item").click(function(){
        $(".item .icon-select").hide();
        $(this).find(".icon-select").show();
        location.href="linkPage.do?url=fans/sub-city-select&type=tag-city&tagIds="+$(this).attr("value");
    });
}