$(function(){
	$("#myTagGroup").tagGroup({
	tags:[ {text:'hangzhou',
		value:'001'
	}, {text:' dongfeng',
		value:'002'
	}]
	})
});
function getTags(){
	var tags=$("#myTagGroup").tagGroup('getTags');
	console.log(tags);
}
function addTag(){
	$("#myTagGroup").tagGroup('appendTag',{
		value:'003',
		text:'changsha'
			});
}
(function($){
    $.fn.tagGroup = function (method,value) {
        if (methods[method]) {          
        	return methods[method].apply(this,Array.prototype.slice.call(arguments, 1));
        } else if (typeof method === 'object' || !method) {
            return methods.init.apply(this, arguments);
        } else {
            $.error('Method ' + method + ' does not exist on jQuery.tagGroup');
        }
    };
    var currTags=[];
    var methods = {
        init : function (_options) {
         	_options = $.extend($.fn.tagGroup.defaults,_options);  
        	var tags=_options.tags;
        	currTags = tags.concat();
        	initTags(this,tags);
            return this;  
        },
        appendTag : function(_options){
        	if(!validateExist(_options)){
        		currTags.push(_options);
        		appendTag(this,_options)
        	}
				return this;
        },
        deleteTag:function(index){
        	deleteTag(this,index);
        },
        getTags:function(){
        	return  getTags();
        }
    };
    function getTags(){
    	var tags=[];
    	$('.tagSpan').each(function(index) {
    		var tag={
    				value:$(this).data('value'),
    				text:$(this).html()
    		}
    		tags.push(tag);
    	  });
    	return tags;
    }
    function initTags(_jq,tags){
    	for(var i=0;i<tags.length;i++){
			 appendTag(_jq,tags[i])
    	}
    	//initEvent();
    }
    function appendTag(_jq,tag){
	   var tagSpan=$("<span>").addClass("tagSpan");
	   var delSpan=$("<span>").addClass("delSpan");
	   tagSpan.data("value",tag.value);
       tagSpan.html(tag.text);
       delSpan.data("value",tag.value);
        delSpan.html("X");
       _jq.append(tagSpan);
       _jq.append(delSpan);
	  delEvent();
    }
    
    function deleteTag(_jq,index){
    	currTags.splice(index,1);
    	_jq.html("");
    	initTags(_jq,currTags);
    }
function delEvent(){
	$('.delSpan').each(function(index){
        $(this).unbind('click').click(function(){
        	$("#myTagGroup").tagGroup('deleteTag',index);
         })
	});
}
 function validateExist(_opition){
    	var exist=false;
    	for(var i=0;i<currTags.length;i++){
    		if(currTags[i].value==_opition.value){
    			exist=true;
    			break;
    		}
    	}
    	return exist;
    }
    $.fn.tagGroup.defaults = { tags:[]  };
})(jQuery);
