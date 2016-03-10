package com.ttsales.microf.love.client.weixin.web.support;


import com.ttsales.microf.love.util.DomUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class WXCallbackContext {
	public final HttpServletRequest request;
	public final HttpServletResponse response;
	public final Document document;
	private boolean responded;
	private Element root;
	private String toUserName;
	private String fromUserName;
	private String msgType;
	private String createTime;

	public WXCallbackContext(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		this.request = request;
		this.response = response;
		document = DomUtils.loadDocument(request.getInputStream());
		responded = response.isCommitted();
	}

	/**
	 * 回复微信XML，仅当responded为false时有效
	 * 
	 * @param xml
	 * @return
	 * @throws IOException
	 * @see {@link #isResponded()}
	 */
	public WXCallbackContext writeXML(String xml) throws IOException {
		response.setContentType("text/xml;charset=UTF-8");
		writeText(response, xml);
		return this;
	}

	/**
	 * 回复微信空串，仅当responded为false时有效
	 * 
	 * @return
	 * @throws IOException
	 * @see {@link #isResponded()}
	 */
	public WXCallbackContext writeEmpty() throws IOException {
		writeText(response, "");
		return this;
	}

	private void writeText(HttpServletResponse response, String content)
			throws IOException {
		if (!responded) {
			PrintWriter writer = response.getWriter();
			writer.write(content);
			writer.flush();
			writer.close();
			responded = true;
		}
	}

	/**
	 * 是否已回复微信
	 * 
	 * @return
	 */
	public boolean isResponded() {
		return responded;
	}

	/**
	 * 根据节点及XPath读取元素
	 * 
	 * @param node
	 * @param xpath
	 * @return
	 */
	public Element readElement(Node node, String xpath) {
		return DomUtils.selectSingle(node, xpath);
	}

	/**
	 * 读取子节点的值
	 * 
	 * @param parent
	 * @param tagName
	 * @return
	 */
	public String readChildValue(Element parent, String tagName) {
		return DomUtils.childElementValue(parent, tagName);
	}

	/**
	 * 读取微信推送的XML的根节点
	 * 
	 * @return
	 */
	public Element readRoot() {
		if (root == null) {
			root = readElement(document, "/xml");
		}
		return root;
	}

	/**
	 * 读取开发者微信号
	 * 
	 * @return
	 */
	public String readToUserName() {
		if (toUserName == null) {
			toUserName = readChildValue(readRoot(), "ToUserName");
		}
		return toUserName;
	}

	/**
	 * 读取发送方帐号（OpenID）
	 * 
	 * @return
	 */
	public String readFromUserName() {
		if (fromUserName == null) {
			fromUserName = readChildValue(readRoot(), "FromUserName");
		}
		return fromUserName;
	}

	/**
	 * 读取消息类型
	 * 
	 * @return
	 */
	public String readMsgType() {
		if (msgType == null) {
			msgType = readChildValue(readRoot(), "MsgType");
		}
		return msgType;
	}

	/**
	 * 读取消息创建时间 （整型）
	 * 
	 * @return
	 */
	public String readCreateTime() {
		if (createTime == null) {
			createTime = readChildValue(readRoot(), "CreateTime");
		}
		return createTime;
	}
}
