package io.github.lumnitzf.eagerbeans;

import org.junit.Assert;
import org.junit.Test;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.enterprise.util.AnnotationLiteral;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class ApplicationScopedBeanTest extends AbstractBeanTest {

    @Override
    Collection<Class<?>> getBeanClasses() {
        return Arrays.asList(EagerBean.class, LazyBean.class);
    }

    @Override
    Collection<Class<? extends Annotation>> getActiveScopes() {
        return Collections.singleton(ApplicationScoped.class);
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


    @ApplicationScoped
    @Eager
    public static class EagerBean {

        @PostConstruct
        public void postConstruct() {
            markAsCreated(EagerBean.class);
        }
    }

    @ApplicationScoped
    public static class LazyBean {

        @PostConstruct
        public void postConstruct() {
            markAsCreated(LazyBean.class);
        }
    }
}
