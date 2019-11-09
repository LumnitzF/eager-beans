package io.github.lumnitzf.eagerbeans.customscope;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.Extension;

public class CustomScopedExtension implements Extension {

    public void addScope(@Observes final BeforeBeanDiscovery event) {
        event.addScope(CustomScoped.class, true, false);
    }
}
