package io.github.lumnitzf.eagerbeans;

import io.github.lumnitzf.eagerbeans.customscope.EagerCustomScoped;
import io.github.lumnitzf.eagerbeans.customscope.EagerustomScopedExtension;
import org.jboss.weld.environment.se.Weld;
import org.junit.Assert;
import org.junit.Test;

import javax.annotation.PostConstruct;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class EagerCustomScopedBeanTest extends AbstractBeanTest {

    @Override
    protected void customizeWeld(Weld weld) {
        weld.addExtension(new EagerustomScopedExtension());
    }

    @Override
    Collection<Class<?>> getBeanClasses() {
        return Arrays.asList(EagerBean.class);
    }

    @Override
    Collection<Class<? extends Annotation>> getActiveScopes() {
        return Collections.singleton(EagerCustomScoped.class);
    }

    @Test
    public void testEagerBeanCreated() {
        Assert.assertTrue(isCreated(EagerBean.class));
    }

    @EagerCustomScoped
    public static class EagerBean {

        @PostConstruct
        public void postConstruct() {
            markAsCreated(EagerBean.class);
        }
    }

}
