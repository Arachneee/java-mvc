package com.interface21.webmvc.servlet.mvc.tobe.returnvaluehandler;

import com.interface21.bean.container.BeanContainer;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.tobe.ReturnValueHandler;
import java.util.List;

public class ReturnValueHandlers {

    private final List<ReturnValueHandler> returnValueHandlers;

    public ReturnValueHandlers() {
        BeanContainer beanContainer = BeanContainer.getInstance();
        this.returnValueHandlers = beanContainer.getSubTypeBeansOf(ReturnValueHandler.class);
    }

    public ModelAndView handler(Object returnValue) {
        ReturnValueHandler handler = returnValueHandlers.stream()
                .filter(returnValueHandler -> returnValueHandler.support(returnValue))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("처리할 수 없는 반환값입니다."));

        return handler.handle(returnValue);
    }
}
