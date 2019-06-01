package net.csdn.video.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 微信自定义菜单
 */
public class WxMenu {
    private List<WxMenuItem> button = new ArrayList<>();

    public List<WxMenuItem> getButton() {
        return button;
    }

    public void setButton(List<WxMenuItem> button) {
        this.button = button;
    }
}
