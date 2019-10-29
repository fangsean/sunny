package com.shu.shuny.spring;

import com.shu.shuny.common.Constants;
import com.shu.shuny.common.exception.BizException;
import com.shu.shuny.common.util.Flector;
import com.shu.shuny.model.InvokerService;
import com.shu.shuny.rpc.consumer.ConsumerFactoryBean;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;


/**
 *
 */
public class ClientFactoryBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {
    private static final Logger logger = LoggerFactory.getLogger(ClientFactoryBeanDefinitionParser.class);

    @Override
    protected Class getBeanClass(Element element) {
        return ConsumerFactoryBean.class;
    }

    @Override
    protected void doParse(Element element, BeanDefinitionBuilder bean) {
        InvokerService invoker = new InvokerService();
        try {
            /**通过flector实现*/
            Flector<InvokerService> flector = new Flector(InvokerService.class);
            flector.setter(invoker,Constants.SUNNY_SERVER_TIMEOUT, Long.parseLong(element.getAttribute(Constants.SUNNY_SERVER_TIMEOUT)));
            flector.setter(invoker, Constants.SUNNY_SERVER_INTERFACE, Class.forName(element.getAttribute(Constants.SUNNY_INTERFACE)));
            flector.setter(invoker, Constants.SUNNY_SERVER_SERVICE_KEY, element.getAttribute(Constants.SUNNY_SERVER_SERVICE_KEY));
            String clusterStrategy = element.getAttribute(Constants.SUNNY_CLUSTER_STRATEGY);
            if (StringUtils.isNotBlank(clusterStrategy)) {
                flector.setter(invoker, Constants.SUNNY_CLUSTER_STRATEGY, clusterStrategy);
            }
            String version = element.getAttribute(Constants.SUNNY_SERVER_VERSION);
            if (StringUtils.isNotBlank(version)) {
                flector.setter(invoker, Constants.SUNNY_SERVER_VERSION, version);
            }
            bean.addPropertyValue(Constants.SUNNY_INVOKER, invoker);
        } catch (Exception e) {
            logger.error("RevokerFactoryBeanDefinitionParser error.", e);
            throw new BizException(e);
        }

    }
}
