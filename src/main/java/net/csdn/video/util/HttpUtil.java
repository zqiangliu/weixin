package net.csdn.video.util;

import com.github.kevinsawicki.http.HttpRequest;

public class HttpUtil {
    public static String post(String url, String data){
        HttpRequest request = HttpRequest.post(url);
        request.trustAllCerts();
        request.trustAllHosts();
        return request.send(data).body("utf-8");
    }

    public static String get(String url){
        HttpRequest request = HttpRequest.get(url);
        request.trustAllCerts();
        request.trustAllHosts();
        return request.body("utf-8");
    }
}
