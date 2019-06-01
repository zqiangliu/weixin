package net.csdn.video.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 微信自定义菜单项
 */
public class WxMenuItem {
    private String type;
    private String name;
    private String key;
    private String appid;
    private String url;
    private String pagepath;
    private String media_id;
    private List<WxMenuItem> sub_button = new ArrayList<>();

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPagepath() {
        return pagepath;
    }

    public void setPagepath(String pagepath) {
        this.pagepath = pagepath;
    }

    public String getMedia_id() {
        return media_id;
    }

    public void setMedia_id(String media_id) {
        this.media_id = media_id;
    }

    public List<WxMenuItem> getSub_button() {
        return sub_button;
    }

    public void setSub_button(List<WxMenuItem> sub_button) {
        this.sub_button = sub_button;
    }
}
