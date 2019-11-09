package io.github.lumnitzf.eagerbeans;

import org.junit.Assert;
import org.junit.Test;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Stereotype;
import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class StereotypedBeanTest extends AbstractBeanTest {

    @Override
    Collection<Class<?>> getBeanClasses() {
        return Arrays.asList(StereotypedEagerBean.class, StackedStereotypedEagerBean.class);
    }

    @Override
    Collection<Class<? extends Annotation>> getActiveScopes() {
        return Collections.singleton(RequestScoped.class);
    }

    @Test
    public void testStereotypedEagerBean() {
        Assert.assertTrue(isCreated(StereotypedEagerBean.class));
    }

    @Test
    public void testStackedStereotypedEagerBean() {
        Assert.assertTrue(isCreated(StackedStereotypedEagerBean.class));
    }

    @Eager
    @Stereotype
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    public @interface EagerStereotype {

    }

    @EagerStereotype
    @Stereotype
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    public @interface StackedEagerStereotype {

    }

    @EagerStereotype
    @RequestScoped
    public static class StereotypedEagerBean {

        @PostConstruct
        public void postConstruct() {
            markAsCreated(StereotypedEagerBean.class);
        }
    }

    @StackedEagerStereotype
    @RequestScoped
    public static class StackedStereotypedEagerBean {

        @PostConstruct
        public void postConstruct() {
            markAsCreated(StackedStereotypedEagerBean.class);
        }
    }

}
