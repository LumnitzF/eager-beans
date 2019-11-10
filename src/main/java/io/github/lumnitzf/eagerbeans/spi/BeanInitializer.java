package io.github.lumnitzf.eagerbeans.spi;

import io.github.lumnitzf.eagerbeans.Eager;
import io.github.lumnitzf.eagerbeans.EagerInitializable;

import javax.enterprise.context.Initialized;
import javax.enterprise.event.Reception;
import javax.enterprise.event.TransactionPhase;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.ObserverMethod;
import javax.enterprise.util.AnnotationLiteral;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Set;
import java.util.logging.Logger;

/**
 * {@link ObserverMethod} implementation, that triggers the initialization of an {@link Eager} bean upon initialization
 * of its scope. Listens on the {@link Initialized} event of a scope, and triggers the initialization of the eager
 * beans.
 *
 * @author Fritz Lumnitz
 */
class BeanInitializer implements ObserverMethod<Object> {

    /**
     * The observed {@link Initialized} annotation value.
     */
    private final Initialized observed;

    /**
     * The {@link Eager} beans to trigger upon scope initialization.
     */
    private final Set<Bean<?>> beans;

    /**
     * The {@link BeanManager} for bean reference lookup.
     */
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
        triggerBeans();
    }

    private void triggerBeans() {
        for (Bean<?> bean : beans) {
            // Get the (uninitialized) reference to the eager bean
            Object beanReference = beanManager.getReference(bean, bean.getBeanClass(),
                    beanManager.createCreationalContext(bean));
            initBean(beanReference);
        }
    }

    @SuppressWarnings("deprecation")
    private void initBean(Object bean) {
        // To trigger the bean initialization we have to call a method from it
        Logger.getLogger(BeanInitializer.class.getName()).fine("Initializing eager bean " + bean);
        if (bean instanceof EagerInitializable) {
            // The bean is an EagerInitializable, so call that init method
            // Avoids expensive toString
            ((EagerInitializable) bean).init();
        } else {
            // Trigger the creation of the bean using its toString()
            // unfortunately hashCode or equals does not work
            //noinspection ResultOfMethodCallIgnored
            bean.toString();
        }
    }

    /**
     * Supports inline instantiation of the {@link Initialized} qualifier.
     *
     * @author Fritz Lumnitz
     */
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
