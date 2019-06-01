package net.csdn.video.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "weixin")
public class WeixinConfig {
    private String appid;
    private String appsecret;
    private String token;
    private String aesKey;
    private String url_prefix;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getAppsecret() {
        return appsecret;
    }

    public void setAppsecret(String appsecret) {
        this.appsecret = appsecret;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAesKey() {
        return aesKey;
    }

    public void setAesKey(String aesKey) {
        this.aesKey = aesKey;
    }

    public String getUrl_prefix() {
        return url_prefix;
    }

    public void setUrl_prefix(String url_prefix) {
        this.url_prefix = url_prefix;
    }

    @Override
    public String toString() {
        return "WeixinConfig{" +
                "appid='" + appid + '\'' +
                ", appsecret='" + appsecret + '\'' +
                ", token='" + token + '\'' +
                ", aesKey='" + aesKey + '\'' +
                ", url_prefix='" + url_prefix + '\'' +
                '}';
    }
}
