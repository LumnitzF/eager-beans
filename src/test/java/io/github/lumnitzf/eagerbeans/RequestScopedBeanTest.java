package io.github.lumnitzf.eagerbeans;

import org.junit.Assert;
import org.junit.Test;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Default;
import javax.enterprise.util.AnnotationLiteral;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class RequestScopedBeanTest extends AbstractBeanTest {

    @Override
    Collection<Class<?>> getBeanClasses() {
        return Arrays.asList(EagerBean.class, LazyBean.class, SecondEagerBean.class);
    }

    @Override
    Collection<Class<? extends Annotation>> getActiveScopes() {
        return Collections.singleton(RequestScoped.class);
    }

    @Test
    public void testEagerBeanCreated() {
        Assert.assertTrue(isCreated(EagerBean.class));
    }

    @Test
    public void testSecondEagerBeanCreated() {
        Assert.assertTrue(isCreated(SecondEagerBean.class));
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


    @RequestScoped
    @Eager
    public static class EagerBean implements EagerInitializable {

        @PostConstruct
        public void postConstruct() {
            markAsCreated(EagerBean.class);
        }

        @Override
        public String toString() {
            Assert.fail("EagerInitializable::init should be called instead of toString()");
            return null;
        }
    }

    @RequestScoped
    @Eager
    public static class SecondEagerBean implements EagerInitializable {

        @PostConstruct
        public void postConstruct() {
            markAsCreated(SecondEagerBean.class);
        }

        @Override
        public String toString() {
            Assert.fail("EagerInitializable::init should be called instead of toString()");
            return null;
        }
    }

    @RequestScoped
    public static class LazyBean {

        @PostConstruct
        public void postConstruct() {
            markAsCreated(LazyBean.class);
        }
    }
}
