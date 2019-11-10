package io.github.lumnitzf.eagerbeans.impl;

import io.github.lumnitzf.eagerbeans.Eager;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.Annotated;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.DefinitionException;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessManagedBean;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;


public class EagerExtension implements Extension {

    private final Map<Class<? extends Annotation>, Set<Bean<?>>> eagerBeans = new HashMap<>();

    void processBeans(@Observes ProcessManagedBean<?> event, BeanManager beanManager) {
        final boolean isEager = isEager(event.getAnnotated(), beanManager);
        if (isEager) {
            final Bean<?> bean = event.getBean();
            Logger.getLogger(EagerExtension.class.getName()).fine("Found eager bean " + event.getBean());
            if (Dependent.class.equals(bean.getScope())) {
                throw new DefinitionException("@Eager marked " + bean + " must not have scope @Dependent");
            }
            eagerBeans.computeIfAbsent(bean.getScope(), ignored -> new HashSet<>()).add(bean);
        }
    }

    private boolean isEager(Annotated annotated, BeanManager beanManager) {
        final Set<Annotation> toInspect = new HashSet<>(annotated.getAnnotations());
        final Set<Annotation> alreadyInspected = new HashSet<>();
        for (Iterator<Annotation> iter = toInspect.iterator(); iter.hasNext(); iter = toInspect.iterator()) {
            final Annotation inspected = iter.next();
            alreadyInspected.add(inspected);
            iter.remove();
            if (inspected instanceof Eager) {
                return true;
            }
            final Set<Annotation> mayInspected;
            if (beanManager.isStereotype(inspected.annotationType())) {
                mayInspected = new HashSet<>(beanManager.getStereotypeDefinition(inspected.annotationType()));
            } else if (beanManager.isScope(inspected.annotationType())) {
                mayInspected = new HashSet<>(Arrays.asList(inspected.annotationType().getAnnotations()));
            } else {
                mayInspected = Collections.emptySet();
            }
            mayInspected.removeAll(alreadyInspected);
            toInspect.addAll(mayInspected);
        }
        return false;
    }

    void afterDiscovery(@Observes AfterBeanDiscovery event, BeanManager beanManager) {
        for (Map.Entry<Class<? extends Annotation>, Set<Bean<?>>> entry : eagerBeans.entrySet()) {
            event.addObserverMethod(new BeanInitializer(entry.getKey(), entry.getValue(), beanManager));
        }
    }
}
