package net.csdn.video.controller;

import com.thoughtworks.xstream.XStream;
import net.csdn.video.config.WeixinConfig;
import net.csdn.video.model.*;
import net.csdn.video.util.wx.AesException;
import net.csdn.video.util.wx.SHA1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping("/wx")
public class WeixinController {

    static Logger logger = LoggerFactory.getLogger(WeixinController.class);


    @Autowired
    WeixinConfig weixinConfig;

    @GetMapping("/connector")
    public String verifySignature(@RequestParam String signature, @RequestParam String timestamp,
                                  @RequestParam String nonce, @RequestParam String echostr){
        logger.debug("signature={}, echostr={}", signature, echostr);
        try {
            String msgSignature = SHA1.getSHA1(weixinConfig.getToken(), timestamp, nonce, "");
            if(msgSignature.equals(signature)){
                return echostr;
            }
        } catch (AesException e) {
            e.printStackTrace();
        }
        return "";
    }

    @PostMapping("/connector")
    public String getNotify(HttpServletRequest request){
        XStream xStream = new XStream();
        xStream.aliasType("xml", WxNotify.class);

        try {
            //WxTextMessage textMessage = (WxTextMessage)xStream.fromXML(request.getInputStream());
            WxNotify notify = (WxNotify)xStream.fromXML(request.getInputStream());

            logger.debug(notify.toString());

            switch (notify.getMsgType()){
                case "text":
                    //return createNewsMessage(notify);
                    return createTextMessage(notify);
                case "image":
                    return createImageMessage(notify);
            }

            if("event".equals(notify.getMsgType()) && "subscribe".equals(notify.getEvent())){
                return createNewsMessage(notify);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private String createTextMessage(WxNotify notify){
        WxTextMessage textMessage = new WxTextMessage();
        textMessage.setToUserName(notify.getFromUserName());
        textMessage.setFromUserName(notify.getToUserName());
        textMessage.setMsgType("text");
        textMessage.setCreateTime(String.valueOf(System.currentTimeMillis() / 1000));
        textMessage.setContent("公众号建设中...敬请期待");
        XStream xStream = new XStream();
        xStream.aliasType("xml", WxTextMessage.class);
        return xStream.toXML(textMessage);
    }

    private String createImageMessage(WxNotify notify){
        WxImageMessage imageMessage = new WxImageMessage();
        imageMessage.setToUserName(notify.getFromUserName());
        imageMessage.setFromUserName(notify.getToUserName());
        imageMessage.setMsgType("image");
        imageMessage.setCreateTime(String.valueOf(System.currentTimeMillis() / 1000));
        WxImageMessageItem item = new WxImageMessageItem();
        item.setMediaId(notify.getMediaId());
        imageMessage.setImage(item);
        XStream xStream = new XStream();
        xStream.aliasType("xml", WxImageMessage.class);
        return xStream.toXML(imageMessage);
    }

    private String createNewsMessage(WxNotify notify){
        WxNewsMessage newsMessage = new WxNewsMessage();
        newsMessage.setFromUserName(notify.getToUserName());
        newsMessage.setToUserName(notify.getFromUserName());
        newsMessage.setMsgType("news");
        newsMessage.setCreateTime(String.valueOf(System.currentTimeMillis() / 1000));
        newsMessage.setArticleCount(2);

        WxNewsMessageItem item1 = new WxNewsMessageItem();
        item1.setTitle("图文消息1");
        item1.setDescription("图文描述1");
        item1.setUrl("http://www.baidu.com");
        item1.setPicUrl("http://mmbiz.qpic.cn/mmbiz_jpg/2FaYicNibG9VBxeIyGoJ9wOzcnvPgb4PXicey6oXsXIe1xc06LULGlVdrHYBkQV2mvXsggibwMvp3qfrt40sOsZ4hQ/0");

        WxNewsMessageItem item2 = new WxNewsMessageItem();
        item2.setTitle("图文消息2");
        item2.setDescription("图文描述2");
        item2.setUrl("http://www.qq.com");
        item2.setPicUrl("http://mmbiz.qpic.cn/mmbiz_jpg/2FaYicNibG9VBxeIyGoJ9wOzcnvPgb4PXicQTfia905ubW96yeQMH1SDayKe8oC6anYH98XUsmWNEtxkzppERiaax3Q/0");

        newsMessage.getArticles().add(item1);
        newsMessage.getArticles().add(item2);

        XStream xStream = new XStream();
        xStream.aliasType("xml", WxNewsMessage.class);
        xStream.aliasType("item", WxNewsMessageItem.class);

        return xStream.toXML(newsMessage);
    }
}
