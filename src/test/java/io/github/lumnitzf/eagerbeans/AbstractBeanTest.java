package io.github.lumnitzf.eagerbeans;

import io.github.lumnitzf.eagerbeans.spi.EagerExtension;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.junit4.WeldInitiator;
import org.junit.After;
import org.junit.Rule;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractBeanTest {

    private static final Set<Class<?>> CREATED = new HashSet<>();

    @Rule
    public WeldInitiator weld = createWeld();

    static void markAsCreated(Class<?> beanClass) {
        CREATED.add(beanClass);
    }

    // Do not use before, as eager initializations runs before the @Before method
    @After
    public void cleanup() {
        CREATED.clear();
    }

    boolean isCreated(Class<?> beanClass) {
        return CREATED.contains(beanClass);
    }

    private WeldInitiator createWeld() {
        final Weld weld = WeldInitiator.createWeld();
        getBeanClasses().forEach(weld::addBeanClass);
        weld.addExtension(new EagerExtension());
        customizeWeld(weld);
        final WeldInitiator.Builder weldInitiatorBuilder = WeldInitiator.from(weld);
        getActiveScopes().forEach(weldInitiatorBuilder::activate);
        return weldInitiatorBuilder.build();
    }

    protected void customizeWeld(Weld weld) {
        // Do nothing, but let children override
    }

    abstract Collection<Class<?>> getBeanClasses();

    abstract Collection<Class<? extends Annotation>> getActiveScopes();
}
