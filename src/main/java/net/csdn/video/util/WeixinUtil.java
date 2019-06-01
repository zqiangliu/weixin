package net.csdn.video.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.csdn.video.config.WeixinConfig;
import net.csdn.video.model.*;
import net.csdn.video.util.wx.AesException;
import net.csdn.video.util.wx.SHA1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Component
public class WeixinUtil {
    static Logger logger = LoggerFactory.getLogger(WeixinUtil.class);

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    WeixinConfig weixinConfig;

    /**
     * 获取微信普通接口的access token
     * @return
     */
    public String getAccessToken(){
        String accessToken = (String) redisTemplate.opsForValue().get("access_token");
        if(StringUtils.isEmpty(accessToken)){
            String response = HttpUtil.get("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="
                    + weixinConfig.getAppid() + "&secret=" + weixinConfig.getAppsecret());
            WxAccessToken token = JSONObject.parseObject(response, WxAccessToken.class);
            if(token.getAccess_token() == null){
                logger.warn(response);
                return null;
            }
            //获取成功，更新缓存
            redisTemplate.opsForValue().set("access_token", token.getAccess_token(), token.getExpires_in() - 5, TimeUnit.SECONDS);
            return token.getAccess_token();
        }else{
            return accessToken;
        }
    }

    /**
     * 获取网页授权的access token
     * @return
     */
    public WxAccessToken4Auth getAccessToken4Auth(String code){
        String accessTokenJson = (String) redisTemplate.opsForValue().get("access_token_auth");
        if(StringUtils.isEmpty(accessTokenJson)){
            String response = HttpUtil.get("https://api.weixin.qq.com/sns/oauth2/access_token?appid="
                    + weixinConfig.getAppid() + "&secret=" + weixinConfig.getAppsecret() + "&code=" + code + "&grant_type=authorization_code");
            logger.debug(response);
            WxAccessToken4Auth token = JSONObject.parseObject(response, WxAccessToken4Auth.class);
            if(token.getAccess_token() == null){
                logger.warn(response);
                return null;
            }
            //获取成功，更新缓存
            redisTemplate.opsForValue().set("access_token_auth", JSON.toJSONString(token), token.getExpires_in() - 5, TimeUnit.SECONDS);
            return token;
        }else{
            return JSONObject.parseObject(accessTokenJson, WxAccessToken4Auth.class);
        }
    }

    /**
     * 获取js-sdk ticket
     * @return
     */
    public String getJsapiTicket(){
        String ticket = (String) redisTemplate.opsForValue().get("jsapi_ticket");
        if(StringUtils.isEmpty(ticket)){
            String response = HttpUtil.get("https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=" + getAccessToken() + "&type=jsapi");
            logger.debug(response);
            WxJsapiTicket jsapiTicket = JSONObject.parseObject(response, WxJsapiTicket.class);

            if(StringUtils.isEmpty(jsapiTicket.getTicket())){
                return null;
            }
            //获取成功，更新缓存
            redisTemplate.opsForValue().set("jsapi_ticket", jsapiTicket.getTicket(), jsapiTicket.getExpires_in() - 5, TimeUnit.SECONDS);
            return jsapiTicket.getTicket();
        }else{
            return ticket;
        }
    }

    /**
     * 获取用户信息（授权）
     * @param accessToken
     * @param openId
     * @return
     */
    public WxUserInfo4Auth getUserInfo4Auth(String accessToken, String openId){
        String response = HttpUtil.get("https://api.weixin.qq.com/sns/userinfo?access_token=" + accessToken + "&openid=" + openId + "&lang=zh_CN");
        logger.debug(response);
        WxUserInfo4Auth userInfo4Auth = JSONObject.parseObject(response, WxUserInfo4Auth.class);
        if(userInfo4Auth.getOpenid() == null){
            logger.warn(response);
            return null;
        }
        return userInfo4Auth;
    }

    /**
     * 获取用户信息(unionid机制)
     * @param openId
     * @return
     */
    public WxUserInfo getUserInfo(String openId){
        logger.debug(openId);
        String response = HttpUtil.get("https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + getAccessToken() + "&openid=" + openId + "&lang=zh_CN");
        logger.debug(response);
        WxUserInfo userInfo = JSONObject.parseObject(response, WxUserInfo.class);
        if(userInfo.getOpenid() == null){
            logger.warn(response);
            return null;
        }
        return userInfo;
    }


    /**
     * 发送模板消息
     * @param toUser
     * @param templateId
     * @param url
     * @param data
     * @return
     */
    public boolean sendTemplateMessage(String toUser, String templateId, String url, Object data){
        WxTemplateMessage templateMessage = new WxTemplateMessage();
        templateMessage.setTouser(toUser);
        templateMessage.setTemplate_id(templateId);
        templateMessage.setUrl(url);
        templateMessage.setData(data);
        String response = HttpUtil.post("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + getAccessToken(),
                JSON.toJSONString(templateMessage));
        JSONObject jsonObject = JSONObject.parseObject(response);
        return jsonObject.getInteger("errcode") == 0;
    }

    /**
     * 获取js-sdk 签名
     * @param noncestr
     * @param timestamp
     * @param url
     * @return
     */
    public String getJsapiSignature(String noncestr, String timestamp, String url){
        String[] array = new String[]{"noncestr=" + noncestr, "jsapi_ticket=" + getJsapiTicket(), "timestamp=" + timestamp, "url=" + url};
        Arrays.sort(array);
        StringBuffer stringBuffer = new StringBuffer();
        for(String kv : array){
            stringBuffer.append(kv).append("&");
        }
        stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        try {
            String signature = SHA1.getSHA1(stringBuffer.toString());
            return signature;
        } catch (AesException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 创建普通菜单
     * @param menu
     * @return
     */
    public boolean createMenu(WxMenu menu){
        String postData = JSON.toJSONString(menu);
        String response = HttpUtil.post("https://api.weixin.qq.com/cgi-bin/menu/create?access_token=" + getAccessToken(), postData);

        JSONObject jsonObject = JSONObject.parseObject(response);
        return jsonObject.getInteger("errcode") == 0;
    }

    /**
     * 创建个性化菜单
     * @param menu
     * @return
     */
    public boolean createConditionalMenu(WxConditionalMenu menu){
        String postData = JSON.toJSONString(menu);
        String response = HttpUtil.post("https://api.weixin.qq.com/cgi-bin/menu/addconditional?access_token=" + getAccessToken(), postData);

        JSONObject jsonObject = JSONObject.parseObject(response);
        logger.debug(response);
        return jsonObject.containsKey("menuid");
    }

}
