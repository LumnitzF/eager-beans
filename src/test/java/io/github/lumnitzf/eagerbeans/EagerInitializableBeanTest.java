package io.github.lumnitzf.eagerbeans;

import io.github.lumnitzf.eagerbeans.impl.EagerExtension;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.junit4.WeldInitiator;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.spi.DefinitionException;

public class EagerInitializableBeanTest {

    @Test
    public void testDefinitionExceptionOnEagerInitializableBean() {
        // Cannot use as Rule because the initialization is performed outside of reachable code
        try {
            final Weld weld = WeldInitiator.createWeld();
            weld.addBeanClass(EagerInitializableBean.class);
            weld.addExtension(new EagerExtension());


            // To test the DefinitionException force the initialization of the CDI environment by evaluating the Weld Rule by hand
            WeldInitiator.from(weld).build().apply(new Statement() {
                @Override
                public void evaluate() {
                    Assert.fail("Statement should not get executed");
                }
            }, Description.EMPTY).evaluate();
            Assert.fail("No DefinitionException thrown");
        } catch (DefinitionException expected) {
            // Expected behavior
        } catch (Throwable unexpected) {
            Assert.fail(unexpected.toString());
        }
    }

    // No Eager wanted for this test!
    // @Eager
    public static class EagerInitializableBean implements EagerInitializable {

        @PostConstruct
        public void postConstruct() {
            Assert.fail("Not @Eager annotated EagerInitializableBean should never be created");
        }
    }
}
