package io.github.lumnitzf.eagerbeans;

import javax.enterprise.inject.spi.DefinitionException;

/**
 * "Marker" interface allowing implementing beans to be initialized without calling the (may expensive) {@link
 * Object#toString()}. Implementing classes still have to be annotated with {@link Eager @Eager}, otherwise a {@link
 * DefinitionException} is thrown at deployment time.
 *
 * @author Fritz Lumnitz
 */
public interface EagerInitializable {

    /**
     * Called in order to trigger the initialization at context start in favor of may expensive {@link #toString()}.
     *
     * @deprecated Should not be overwritten. Is only called by the container to trigger the initialization in favor of
     * {@link #toString()}. Actual initialization should be done in a {@code javax.annotation.PostConstruct} annotated
     * method.
     */
    @SuppressWarnings("DeprecatedIsStillUsed")
    @Deprecated
    default void init() {
    }

}
