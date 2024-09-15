package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import java.util.Arrays;
import java.util.List;

public class HandlerKeys {

    private final List<HandlerKey> keys;

    private HandlerKeys(List<HandlerKey> keys) {
        this.keys = keys;
    }

    public static HandlerKeys from(RequestMapping requestMapping) {
        String url = requestMapping.value();
        RequestMethod[] requestMethods = requestMapping.method();

        List<HandlerKey> keys = creatKeys(requestMethods, url);

        return new HandlerKeys(keys);
    }

    private static List<HandlerKey> creatKeys(RequestMethod[] requestMethods, String url) {
        return Arrays.stream(requestMethods)
                .map(method -> new HandlerKey(url, method))
                .toList();
    }

    public List<HandlerKey> getKeys() {
        return keys;
    }
}
