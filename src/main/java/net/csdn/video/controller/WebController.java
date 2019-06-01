package net.csdn.video.controller;

import net.csdn.video.config.WeixinConfig;
import net.csdn.video.model.WxAccessToken4Auth;
import net.csdn.video.model.WxUserInfo;
import net.csdn.video.model.WxUserInfo4Auth;
import net.csdn.video.util.WeixinUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.UUID;

@Controller
public class WebController {
    static Logger logger = LoggerFactory.getLogger(WebController.class);
    @Autowired
    WeixinConfig weixinConfig;
    @Autowired
    WeixinUtil weixinUtil;

    @RequestMapping(value = "/turnplate", method = RequestMethod.GET)
    public String turnplate(){
        return "redirect:/turnpalte_authorize";
    }

    @RequestMapping(value = "/turnplate_back", method = RequestMethod.GET)
    public String turnplateAuthBack(@RequestParam String code, @RequestParam String state, Model model){
        //获取网页授权的access token
        WxAccessToken4Auth accessToken = weixinUtil.getAccessToken4Auth(code);
        //拉取用户信息
        WxUserInfo4Auth userInfo = weixinUtil.getUserInfo4Auth(accessToken.getAccess_token(), accessToken.getOpenid());

        model.addAttribute("userInfo", userInfo);

        Long timestamp = System.currentTimeMillis() / 1000;
        String nonceStr = UUID.randomUUID().toString();
        String url = weixinConfig.getUrl_prefix() + "/turnplate_back?code=" + code + "&state=" + state;
        String signature = weixinUtil.getJsapiSignature(nonceStr, String.valueOf(timestamp), url);

        model.addAttribute("appId", weixinConfig.getAppid());
        model.addAttribute("timestamp", timestamp);
        model.addAttribute("nonceStr", nonceStr);
        model.addAttribute("signature", signature);
        return "turnplate";
    }

    @RequestMapping(value = "/subscribe_status", method = RequestMethod.GET)
    @ResponseBody
    public boolean getSubscribeStatus(@RequestParam String openId){
        WxUserInfo userInfo = weixinUtil.getUserInfo(openId);
        return userInfo != null && userInfo.getSubscribe() == 1;
    }


    @RequestMapping(value = "/turnpalte_authorize", method = RequestMethod.GET)
    public String turnplateAuthorize(){
        String redirect_uri = weixinConfig.getUrl_prefix() + "/turnplate_back";

        String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + weixinConfig.getAppid()
                + "&redirect_uri=" + redirect_uri + "&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";

        return "redirect:" + url;
    }
}
