package net.csdn.video.util;

import net.csdn.video.model.WxConditionalMenu;
import net.csdn.video.model.WxMenu;
import net.csdn.video.model.WxMenuItem;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;

@SpringBootTest
@RunWith(SpringRunner.class)
public class WxMenuTest {
    @Autowired
    WeixinUtil weixinUtil;

    @Test
    public void testCreateMenu(){
        WxMenu menu = new WxMenu();

        WxMenuItem item1 = new WxMenuItem();
        item1.setName("菜单1");
        item1.setType("click");
        item1.setKey("CLICK_ID_01");

        WxMenuItem item2 = new WxMenuItem();
        item2.setName("菜单2");

        WxMenuItem item21 = new WxMenuItem();
        item21.setName("菜单21");
        item21.setType("view");
        item21.setUrl("http://www.baidu.com");

        WxMenuItem item22 = new WxMenuItem();
        item22.setName("菜单22");
        item22.setType("scancode_push");
        item22.setKey("SCAN_ID_01");

        item2.getSub_button().add(item21);
        item2.getSub_button().add(item22);

        WxMenuItem item3 = new WxMenuItem();
        item3.setName("菜单3");

        WxMenuItem item31 = new WxMenuItem();
        item31.setName("选择图片");
        item31.setType("pic_photo_or_album");
        item31.setKey("SELECT_PIC_01");

        item3.getSub_button().add(item31);

        menu.getButton().add(item1);
        menu.getButton().add(item2);
        menu.getButton().add(item3);

        boolean result = weixinUtil.createMenu(menu);
        Assert.assertEquals(true, result);
    }

    @Test
    public void testCreateConditionalMenu(){
        WxConditionalMenu menu = new WxConditionalMenu();

        WxMenuItem item1 = new WxMenuItem();
        item1.setName("个性菜单1");
        item1.setType("click");
        item1.setKey("CLICK_ID_01");

        WxMenuItem item2 = new WxMenuItem();
        item2.setName("个性菜单2");

        WxMenuItem item21 = new WxMenuItem();
        item21.setName("个性菜单21");
        item21.setType("view");
        item21.setUrl("http://www.baidu.com");

        WxMenuItem item22 = new WxMenuItem();
        item22.setName("个性菜单22");
        item22.setType("scancode_push");
        item22.setKey("SCAN_ID_01");

        item2.getSub_button().add(item21);
        item2.getSub_button().add(item22);

        menu.getButton().add(item1);
        menu.getButton().add(item2);

        HashMap matchRule = new HashMap();
        matchRule.put("country", "中国");
        matchRule.put("province", "广东");

        menu.setMatchrule(matchRule);

        boolean result = weixinUtil.createConditionalMenu(menu);
        Assert.assertEquals(true, result);
    }
}
