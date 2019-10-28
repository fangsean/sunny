package com.shu.shuny.spring;

import org.springframework.beans.factory.xml.DefaultNamespaceHandlerResolver;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;


/**
 * <describ>
 * 定义由 {@link DefaultNamespaceHandlerResolver#resolve} 初始化加载自定义解析器放入parsers中</>
 */
public class SunnyRemoteServiceNamespaceHandler extends NamespaceHandlerSupport {
    @Override
    public void init() {
        registerBeanDefinitionParser("service", new ProviderFactoryBeanDefinitionParser());
    }
}
