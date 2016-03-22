package com.ttsales.microf.love.weixin.message;


public class TextMsgContent implements PubResponseMsgContent{
	private static final String TEMP_XML = "<MsgType><![CDATA[text]]></MsgType><Content><![CDATA[%s]]></Content>";
	private String content;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}


	public String toPubResponseMsgStr() {
		return toXmlMsgStr();
	}
	private String toXmlMsgStr() {
		return String.format(TEMP_XML, content);
	}

}