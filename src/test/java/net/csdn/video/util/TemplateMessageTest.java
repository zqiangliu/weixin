package net.csdn.video.util;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TemplateMessageTest {
    @Autowired
    WeixinUtil weixinUtil;

    @Test
    public void testSendMessage(){
        HashMap data = new HashMap();
        HashMap value = new HashMap();
        value.put("value", "测试员");
        value.put("color", "#ff0000");
        data.put("name", value);

        boolean result = weixinUtil.sendTemplateMessage("oFtxx5joPv7ZW_Pm6IFXvbns-1yQ",
                "Eqf-b5fC6aNPqW5kfg0g3zzdYpcn23sii3fQZ9tBoho", "http://www.baidu.com", data);

        Assert.assertEquals(true, result);
    }
}
