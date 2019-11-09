package io.github.lumnitzf.eagerbeans.customscope;

import javax.enterprise.context.NormalScope;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@NormalScope
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomScoped {
}
