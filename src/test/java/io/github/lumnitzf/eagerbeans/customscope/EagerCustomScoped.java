package io.github.lumnitzf.eagerbeans.customscope;

import io.github.lumnitzf.eagerbeans.Eager;

import javax.enterprise.context.NormalScope;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Eager
@NormalScope
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface EagerCustomScoped {
}
