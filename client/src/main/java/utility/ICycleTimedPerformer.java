package utility;

public interface ICycleTimedPerformer {
    /**
     * Performs a timed cycle operation.
     *
     * @param deltaTime The time elapsed since the last cycle, in seconds.
     */
    void performCycle(float deltaTime);
}
