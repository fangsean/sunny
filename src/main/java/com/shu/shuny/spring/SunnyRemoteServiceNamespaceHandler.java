package com.shu.shuny.spring;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;


public class SunnyRemoteServiceNamespaceHandler extends NamespaceHandlerSupport {
    @Override
    public void init() {
        registerBeanDefinitionParser("service", new ProviderFactoryBeanDefinitionParser());
    }
}
