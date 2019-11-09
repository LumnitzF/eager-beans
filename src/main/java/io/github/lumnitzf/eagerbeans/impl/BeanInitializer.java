package io.github.lumnitzf.eagerbeans.impl;

import javax.enterprise.context.Initialized;
import javax.enterprise.event.Reception;
import javax.enterprise.event.TransactionPhase;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.CDI;
import javax.enterprise.inject.spi.ObserverMethod;
import javax.enterprise.util.AnnotationLiteral;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Set;

class BeanInitializer implements ObserverMethod<Object> {

    private final Initialized observed;

    private final Set<Bean<?>> beans;

    private final BeanManager beanManager;

    BeanInitializer(Class<? extends Annotation> scope, Set<Bean<?>> beans, BeanManager beanManager) {
        this.observed = new InitializedLiteral(scope);
        this.beans = beans;
        this.beanManager = beanManager;
    }


    @Override
    public Class<?> getBeanClass() {
        return BeanInitializer.class;
    }

    @Override
    public Type getObservedType() {
        return Object.class;
    }

    @Override
    public Set<Annotation> getObservedQualifiers() {
        return Collections.singleton(observed);
    }

    @Override
    public Reception getReception() {
        return Reception.ALWAYS;
    }

    @Override
    public TransactionPhase getTransactionPhase() {
        return TransactionPhase.IN_PROGRESS;
    }

    @Override
    public void notify(Object event) {
        for(Bean<?> bean : beans) {
            //noinspection ResultOfMethodCallIgnored: We simply want to trigger it to force the initialization
            beanManager.getReference(bean, bean.getBeanClass(), beanManager.createCreationalContext(bean)).hashCode();
        }
    }

    private static class InitializedLiteral extends AnnotationLiteral<Initialized> implements Initialized {

        private static final long serialVersionUID = 1L;

        private final Class<? extends Annotation> value;

        InitializedLiteral(Class<? extends Annotation> value) {
            this.value = value;
        }

        @Override
        public Class<? extends Annotation> value() {
            return value;
        }
    }
}
