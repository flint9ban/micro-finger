package com.ttsales.microf.love.quote.service;

import com.ttsales.microf.love.article.domain.Article;
import com.ttsales.microf.love.article.domain.ArticleTag;
import com.ttsales.microf.love.quote.domain.QueryInfo;
import com.ttsales.microf.love.util.WXApiException;
import net.sf.json.JSONObject;
import org.apache.http.HttpException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Date;
import java.util.List;

/**
 * Created by liyi on 2016/3/11.
 */
public interface QuoteService {

    QueryInfo queryQueryInfo(String openId);

    boolean isQueryLimit(String openId);

    JSONObject queryQuote(String storeId);

    JSONObject query(QueryInfo queryInfo);

    List<JSONObject> querySelfBrand(QueryInfo queryInfo);

    List<JSONObject> queryCompetBrand(QueryInfo queryInfo);
}
