package net.csdn.video.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 微信图文回复消息
 */
public class WxNewsMessage {
    private String ToUserName;
    private String FromUserName;
    private String CreateTime;
    private String MsgType;
    private Integer ArticleCount;
    private List<WxNewsMessageItem> Articles = new ArrayList<>();

    public String getToUserName() {
        return ToUserName;
    }

    public void setToUserName(String toUserName) {
        ToUserName = toUserName;
    }

    public String getFromUserName() {
        return FromUserName;
    }

    public void setFromUserName(String fromUserName) {
        FromUserName = fromUserName;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public String getMsgType() {
        return MsgType;
    }

    public void setMsgType(String msgType) {
        MsgType = msgType;
    }

    public Integer getArticleCount() {
        return ArticleCount;
    }

    public void setArticleCount(Integer articleCount) {
        ArticleCount = articleCount;
    }

    public List<WxNewsMessageItem> getArticles() {
        return Articles;
    }

    public void setArticles(List<WxNewsMessageItem> articles) {
        Articles = articles;
    }
}
