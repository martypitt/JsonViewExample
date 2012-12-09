package com.mangofactory.jsonview;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.HandlerMethodReturnValueHandlerComposite;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import com.google.common.collect.Lists;

/**
 * Modified Spring 3.1's internal Return value handlers, and wires up a decorator
 * to add support for @JsonView
 * 
 * @author martypitt
 *
 */
@Slf4j
public class JsonViewSupportFactoryBean implements InitializingBean {

    @Autowired
    private RequestMappingHandlerAdapter adapter;
    @Override
    public void afterPropertiesSet() throws Exception {
        HandlerMethodReturnValueHandlerComposite returnValueHandlers = adapter.getReturnValueHandlers();
        List<HandlerMethodReturnValueHandler> handlers = Lists.newArrayList(returnValueHandlers.getHandlers());
        decorateHandlers(handlers);
        adapter.setReturnValueHandlers(handlers);
    }
    private void decorateHandlers(List<HandlerMethodReturnValueHandler> handlers) {
        for (HandlerMethodReturnValueHandler handler : handlers) {
            if (handler instanceof RequestResponseBodyMethodProcessor)
            {
                ViewInjectingReturnValueHandler decorator = new ViewInjectingReturnValueHandler(handler);
                int index = handlers.indexOf(handler);
                handlers.set(index, decorator);
                log.info("JsonView decorator support wired up");
                break;
            }
        }        
    }

}