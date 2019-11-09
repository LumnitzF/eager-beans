package io.github.lumnitzf.eagerbeans.beans;

import io.github.lumnitzf.eagerbeans.Eager;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import java.time.Instant;

@Eager
@RequestScoped
public class RequestScopedBean {

    private Instant constructed;

    @PostConstruct
    void postConstruct() {
        constructed = Instant.now();
        System.out.println("Constructed");
    }

    public Instant getConstructed() {
        return constructed;
    }
}
