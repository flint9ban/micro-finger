$(function(){
    if(sessionStorage.jump){
        if(sessionStorage.oriData){
            var data=JSON.parse(sessionStorage.oriData);
            showValues(data);
        }
    }else{
        var data=[
            {id:'001',
                name:'市占率分析',
                categoryId:'001'
            },
            {
                id:'002',
                name:'东风日产',
                categoryId:'002'
            },
            {
                id:'003',
                name:'杭州',
                categoryId:'003'
            }
        ];
        sessionStorage.oriData=JSON.stringify(data);
        showValues(data);
    }
})
function jumpChoosePg(param){
    sessionStorage.jump=true;
    if(param=="dataType"){
        location.href="subTag-type.html?openId=1111";
    }
    if(param=="dataBrand"){
        location.href="subTag-brand.html?openId=1111";
    }
    if(param=="dataArea"){
        location.href="province-select.html?openId=1111";
    }
}
function showValues(data){
    var typeArray=[];
    var brandArray=[];
    var areaArray=[];
    for(var i=0;i<data.length;i++) {
        var span = $("<span>").addClass("tagSpan").html(data[i].name);
        $("#select-tags").append(span);
        if (data[i].categoryId == "001") {//数据类型
            typeArray.push(data[i]);
        }
        if (data[i].categoryId == "002") {//品牌
            brandArray.push(data[i]);
        }
        if (data[i].categoryId == "003") {//地区
            areaArray.push(data[i]);
        }
    }
        $("#dataType").addClass(".item-show").append(formatterName(typeArray));
        $("#brand").addClass(".item-show").append(formatterName(brandArray));
        $("#area").addClass(".item-show").append(formatterName(areaArray));

}
function formatterName(data){
    var name="";
    for(var i=0;i<data.length;i++){
        name+=  data[i].name+",";
    }
    return name.length>0?name.substr(0,name.length-1):"";
}