package com.shu.shuny.spring;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;


public class SunnyRemoteReferenceNamespaceHandler extends NamespaceHandlerSupport {
    @Override
    public void init() {
        registerBeanDefinitionParser("reference", new ClientFactoryBeanDefinitionParser());
    }
}
