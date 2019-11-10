package io.github.lumnitzf.eagerbeans;

import io.github.lumnitzf.eagerbeans.spi.EagerExtension;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.junit4.WeldInitiator;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.spi.DefinitionException;

public class DependentScopedBeanTest {

    @Test
    public void testDefinitionExceptionOnEagerDependentBean() {
        // Cannot use as Rule because the initialization is performed outside of reachable code
        try {
            final Weld weld = WeldInitiator.createWeld();
            weld.addBeanClass(EagerBean.class);
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

    @Eager
    public static class EagerBean {

        @PostConstruct
        public void postConstruct() {
            Assert.fail("@Dependent EagerBean should never be created");
        }
    }
}
