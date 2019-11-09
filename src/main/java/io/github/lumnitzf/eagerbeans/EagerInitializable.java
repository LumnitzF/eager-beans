package io.github.lumnitzf.eagerbeans;

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
