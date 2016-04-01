    $(function(){
        var shareData = {
            title : sessionStorage.storeName+'：如何从报价上抢占客户',
            desc : '直击客户心坎的报价技巧，看完这篇就够了',
            link : sessionStorage.appUrl+'/auth/check?scope=snsapi_base&target_uri=quoteHome%2finit%3fmemberId%3d'+sessionStorage.memberId+'&redirect;',
            img_url : sessionStorage.appUrl + '/img/share.jpg'
        };
        weixin.showOptionMenu();
        weixin.onMenuShareAppMessage(shareData);
        weixin.onMenuShareTimeline(shareData);
    });
