package net.csdn.video;

import net.csdn.video.config.WeixinConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class WeixinApplication {
    public static void main(String[] args) {
        SpringApplication.run(WeixinApplication.class, args);
    }

    @Autowired
    WeixinConfig weixinConfig;

    @GetMapping("/test")
    public String test(){
        return weixinConfig.toString();
    }
}
