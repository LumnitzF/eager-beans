package io.github.lumnitzf.eagerbeans.impl;

import io.github.lumnitzf.eagerbeans.Eager;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.DeploymentException;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessManagedBean;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;


public class EagerExtension implements Extension {

    private final Map<Class<? extends Annotation>, Set<Bean<?>>> eagerBeans = new HashMap<>();

    void processBeans(@Observes ProcessManagedBean<?> event, BeanManager beanManager) {
        final boolean isEager = event.getAnnotated().isAnnotationPresent(Eager.class);
        if (isEager) {
            final Bean<?> bean = event.getBean();
            Logger.getLogger(EagerExtension.class.getName()).fine("Found eager bean " + event.getBean());
            if (Dependent.class.equals(bean.getScope())) {
                throw new DeploymentException("Eager bean " + bean + " must not have scope @Dependent");
            }
            eagerBeans.computeIfAbsent(bean.getScope(), ignored -> new HashSet<>()).add(bean);
        }
    }

    void afterDiscovery(@Observes AfterBeanDiscovery event, BeanManager beanManager) {
        for (Map.Entry<Class<? extends Annotation>, Set<Bean<?>>> entry : eagerBeans.entrySet()) {
            event.addObserverMethod(new BeanInitializer(entry.getKey(), entry.getValue(), beanManager));
        }
    }
}
