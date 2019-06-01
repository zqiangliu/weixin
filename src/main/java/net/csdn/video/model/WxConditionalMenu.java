package net.csdn.video.model;

import java.util.ArrayList;
import java.util.List;

public class WxConditionalMenu {
    private List<WxMenuItem> button = new ArrayList<>();

    private Object matchrule;

    public List<WxMenuItem> getButton() {
        return button;
    }

    public void setButton(List<WxMenuItem> button) {
        this.button = button;
    }

    public Object getMatchrule() {
        return matchrule;
    }

    public void setMatchrule(Object matchrule) {
        this.matchrule = matchrule;
    }
}
