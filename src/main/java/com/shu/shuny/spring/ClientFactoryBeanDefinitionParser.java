package com.shu.shuny.spring;

import com.shu.shuny.common.Constants;
import com.shu.shuny.common.exception.BizException;
import com.shu.shuny.rpc.consumer.ConsumerFactoryBean;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.w3c.dom.Element;


public class ClientFactoryBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

    //logger
    private static final Logger logger = LoggerFactory.getLogger(ClientFactoryBeanDefinitionParser.class);

    @Override
    protected Class getBeanClass(Element element) {
        return ConsumerFactoryBean.class;
    }

    @Override
    protected void doParse(Element element, BeanDefinitionBuilder bean) {

        try {
            String timeOut = element.getAttribute(Constants.SUNNY_SERVER_TIMEOUT);
            String targetInterface = element.getAttribute(Constants.SUNNY_INTERFACE);
            String clusterStrategy = element.getAttribute(Constants.SUNNY_CLUSTER_STRATEGY);
            String remoteAppKey = element.getAttribute(Constants.SUNNY_SERVER_SERVICE_KEY);
            String version = element.getAttribute(Constants.SUNNY_SERVER_VERSION);
            bean.addPropertyValue(Constants.SUNNY_SERVER_TIMEOUT, Integer.parseInt(timeOut));
            bean.addPropertyValue(Constants.SUNNY_TARGET_INTERFACE, Class.forName(targetInterface));
            bean.addPropertyValue(Constants.SUNNY_SERVER_SERVICE_KEY, remoteAppKey);
            if (StringUtils.isNotBlank(clusterStrategy)) {
                bean.addPropertyValue(Constants.SUNNY_CLUSTER_STRATEGY, clusterStrategy);
            }
            if (StringUtils.isNotBlank(version)) {
                bean.addPropertyValue(Constants.SUNNY_SERVER_VERSION, version);
            }
        } catch (Exception e) {
            logger.error("RevokerFactoryBeanDefinitionParser error.", e);
            throw new BizException(e);
        }

    }
}
