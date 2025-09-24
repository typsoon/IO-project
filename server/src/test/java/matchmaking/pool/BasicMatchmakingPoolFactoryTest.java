package matchmaking.pool;

import matchmaking.MatchmakingParameters;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BasicMatchmakingPoolFactoryTest {

    @Test
    void createPool_returnsBasicMatchmakingPoolWithCorrectParameters() {
        MatchmakingParameters params = new MatchmakingParameters(3);
        BasicMatchmakingPoolFactory factory = new BasicMatchmakingPoolFactory();

        IMatchmakingPool pool = factory.createPool(params);

        assertNotNull(pool);
        assertTrue(pool instanceof BasicMatchmakingPool);
        assertEquals(0, pool.size());
    }
}