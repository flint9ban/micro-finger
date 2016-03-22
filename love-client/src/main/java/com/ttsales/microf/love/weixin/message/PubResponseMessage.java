package com.ttsales.microf.love.weixin.message;




import com.ttsales.microf.love.util.WXApiException;
import com.ttsales.microf.love.weixin.message.PubResponseMsgContent;

public class PubResponseMessage {
	private static final String TEMP = "<xml><ToUserName><![CDATA[%1$s]]></ToUserName><FromUserName><![CDATA[%2$s]]></FromUserName><CreateTime>%3$d</CreateTime>%4$s</xml>";
	private String toUserName;
	private String fromUserName;
	private Long createTime;
	private PubResponseMsgContent content;

	public String getToUserName() {
		return toUserName;
	}

	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}

	public String getFromUserName() {
		return fromUserName;
	}

	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public PubResponseMsgContent getContent() {
		return content;
	}

	public void setContent(PubResponseMsgContent content) {
		this.content = content;
	}

	public String toMsgString() throws WXApiException {
		if (toUserName == null) {
			throw new WXApiException("ToUserName cannot be null");
		}
		if (fromUserName == null) {
			throw new WXApiException("FromUserName cannot be null");
		}
		if (createTime == null) {
			throw new WXApiException("CreateTime cannot be null");
		}
		if (content == null) {
			throw new WXApiException("Message Content cannot be null");
		}
		return String.format(TEMP, toUserName, fromUserName, createTime,
				content.toPubResponseMsgStr());
	}
}