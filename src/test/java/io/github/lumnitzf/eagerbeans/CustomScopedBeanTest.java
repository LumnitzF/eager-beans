package io.github.lumnitzf.eagerbeans;

import io.github.lumnitzf.eagerbeans.customscope.CustomScoped;
import io.github.lumnitzf.eagerbeans.customscope.CustomScopedExtension;
import org.jboss.weld.environment.se.Weld;
import org.junit.Assert;
import org.junit.Test;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Default;
import javax.enterprise.util.AnnotationLiteral;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class CustomScopedBeanTest extends AbstractBeanTest {

    @Override
    protected void customizeWeld(Weld weld) {
        weld.addExtension(new CustomScopedExtension());
    }

    @Override
    Collection<Class<?>> getBeanClasses() {
        return Arrays.asList(EagerBean.class, LazyBean.class);
    }

    @Override
    Collection<Class<? extends Annotation>> getActiveScopes() {
        return Collections.singleton(CustomScoped.class);
    }

    @Test
    public void testEagerBeanCreated() {
        Assert.assertTrue(isCreated(EagerBean.class));
    }

    @Test
    public void testLazyBeanNotCreated() {
        Assert.assertFalse(isCreated(LazyBean.class));
    }

    @Test
    public void testLazyBeanCreated() {
        // Initialize the LazyBean using toString
        //noinspection ResultOfMethodCallIgnored
        weld.select(LazyBean.class, new AnnotationLiteral<Default>() {}).get().toString();
        Assert.assertTrue(isCreated(LazyBean.class));
    }

    @CustomScoped
    @Eager
    public static class EagerBean {

        @PostConstruct
        public void postConstruct() {
            markAsCreated(EagerBean.class);
        }
    }

    @CustomScoped
    public static class LazyBean {

        @PostConstruct
        public void postConstruct() {
            markAsCreated(LazyBean.class);
        }
    }

}
