package net.csdn.video.util;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AccessTokenTest {
    @Autowired
    WeixinUtil weixinUtil;

    @Test
    public void testGetAccessToken(){
        String accessToken = weixinUtil.getAccessToken();
        Assert.assertNotNull(accessToken);
        System.out.println(accessToken);
    }
}
