package io.github.lumnitzf.eagerbeans;

import javax.enterprise.context.Dependent;
import javax.enterprise.context.NormalScope;
import javax.enterprise.inject.Stereotype;
import javax.enterprise.inject.spi.DefinitionException;
import javax.enterprise.util.AnnotationLiteral;
import java.lang.annotation.*;

/**
 * Marks a bean to be initialized as soon as its associated context gets activated.
 * <p>
 * This annotation may be put on a class, a {@link Stereotype} or a {@link NormalScope}.
 * </p>
 * <p>
 * The annotated bean must <b>not</b> have scope {@link Dependent @Dependent}. In this case a
 * {@link DefinitionException} will be thrown at deployment time.
 * </p>
 *
 * @author Fritz Lumnitz
 * @implNote The bean initialization will be triggered by calling {@link Object#toString() toString()} on the bean
 * reference. In case the toString() computation is considered too expensive, annotated classes may implement {@link
 * EagerInitializable}. Initialization then will be triggered by calling the no-op {@link EagerInitializable#triggerInitialization()}.
 * @see EagerInitializable
 */
@SuppressWarnings("deprecation")
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Eager {

    /**
     * Supports inline instantiation of the {@link Eager} qualifier.
     *
     * @author Fritz Lumnitz
     */
    final class Literal extends AnnotationLiteral<Eager> implements Eager {

        public static final Literal INSTANCE = new Literal();

        private static final long serialVersionUID = 1L;

    }
}
