package com.shu.shuny.spring;

import com.shu.shuny.common.Constants;
import com.shu.shuny.common.exception.BizException;
import com.shu.shuny.rpc.provider.ProviderFactoryBean;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.w3c.dom.Element;

import java.util.concurrent.locks.Condition;


public class ProviderFactoryBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

    //logger
    private static final Logger logger = LoggerFactory.getLogger(ProviderFactoryBeanDefinitionParser.class);


    @Override
    protected Class getBeanClass(Element element) {
        return ProviderFactoryBean.class;
    }

    @Override
    protected void doParse(Element element, BeanDefinitionBuilder bean) {

        try {
            String serviceItf = element.getAttribute(Constants.SUNNY_INTERFACE);
            String timeOut = element.getAttribute(Constants.SUNNY_SERVER_TIMEOUT);
            String serverPort = element.getAttribute(Constants.SUNNY_SERVER_SERVER_PORT);
            String ref = element.getAttribute(Constants.SUNNY_SERVER_REF);
            String weight = element.getAttribute(Constants.SUNNY_SERVER_WEIGHT);
            String workerThreads = element.getAttribute(Constants.SUNNY_SERVER_WORKER_THREADS);
            String appKey = element.getAttribute(Constants.SUNNY_SERVER_SERVICE_KEY);
            String groupName = element.getAttribute(Constants.SUNNY_SERVER_VERSION);

            bean.addPropertyValue(Constants.SUNNY_SERVER_SERVER_PORT, Integer.parseInt(serverPort));
            bean.addPropertyValue(Constants.SUNNY_SERVER_TIMEOUT, Integer.parseInt(timeOut));
            bean.addPropertyValue(Constants.SUNNY_SERVER_INTERFACE, Class.forName(serviceItf));
            bean.addPropertyReference(Constants.SUNNY_SERVICE_IMPL, ref);
            bean.addPropertyValue(Constants.SUNNY_SERVER_SERVICE_KEY, appKey);
            if (NumberUtils.isNumber(weight)) {
                bean.addPropertyValue(Constants.SUNNY_SERVER_WEIGHT, Integer.parseInt(weight));
            }
            if (NumberUtils.isNumber(workerThreads)) {
                bean.addPropertyValue(Constants.SUNNY_SERVER_WORKER_THREADS, Integer.parseInt(workerThreads));
            }
            if (StringUtils.isNotBlank(groupName)) {
                bean.addPropertyValue(Constants.SUNNY_SERVER_VERSION, groupName);
            }
        } catch (Exception e) {
            logger.error("ProviderFactoryBeanDefinitionParser error.", e);
            throw new BizException(e);
        }

    }


}
