package com.interface21.webmvc.servlet.mvc.tobe.returnvaluehandler;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.tobe.ReturnValueHandler;

public class ModelAndViewReturnValueHandler implements ReturnValueHandler {

    @Override
    public boolean support(Object returnValue) {
        return returnValue instanceof ModelAndView;
    }

    @Override
    public ModelAndView handle(Object returnValue) {
        return (ModelAndView) returnValue;
    }
}
