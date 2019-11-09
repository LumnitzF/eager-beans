package io.github.lumnitzf.eagerbeans;

import io.github.lumnitzf.eagerbeans.beans.RequestScopedBean;
import org.jboss.weld.junit4.WeldInitiator;
import org.junit.Rule;
import org.junit.Test;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import java.time.Instant;

public class EagerExtensionTest {
    @Rule
    public WeldInitiator weld = WeldInitiator.from(RequestScopedBean.class).activate(RequestScoped.class).build();

    @Test
    public void testFoo() throws InterruptedException {
        Thread.sleep(100L);
        System.out.println(weld.select(RequestScopedBean.class).get().getConstructed());
        System.out.println(Instant.now());
    }
}