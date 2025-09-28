package game.engine.entities.items.items;

import game.engine.IWorldView;
import game.engine.entities.IEntity;
import game.engine.entities.IUsageModifiers;
import game.engine.entities.items.attacks.DamageModifier;
import game.engine.entities.items.attacks.IAttack;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class BasicSwordTest {

    private BasicSword sword;
    private IWorldView mockWorldView;
    private IEntity mockUser;
    private IUsageModifiers mockModifiers;
    private DamageModifier mockDamageModifier;
    private IAttack mockAttack;

    @BeforeEach
    void setUp() {
        sword = new BasicSword();
        mockWorldView = mock(IWorldView.class);
        mockUser = mock(IEntity.class);
        mockModifiers = mock(IUsageModifiers.class);
        mockDamageModifier = mock(DamageModifier.class);

        when(mockModifiers.getDamageModifier()).thenReturn(mockDamageModifier);

        mockAttack = mock(IAttack.class);
        sword.attack = mockAttack;
    }

    @Test
    void testStackSizeIsOne() {
        assertEquals(1, sword.stackSize());
    }

    @Test
    void testGetItemInfo() {
        assertNotNull(sword.getItemInfo());
        assertEquals("Basic Sword", sword.getItemInfo().name());
        assertEquals("A simple sword. Reliable and sturdy.", sword.getItemInfo().description());
    }

    @Test
    void testItemUnequipAllowsNextAttackImmediately() {
        sword.primaryAction(mockWorldView, mockUser, mockModifiers);
        sword.primaryAction(mockWorldView, mockUser, mockModifiers);
        verify(mockAttack, never()).attack(any(), any(), any());

        sword.itemUnequip(mockWorldView, mockUser, mockModifiers);

        for (int i = 0; i < 30; i++) {
            sword.primaryAction(mockWorldView, mockUser, mockModifiers);
        }

        verify(mockAttack, times(1)).attack(mockWorldView, mockUser, mockDamageModifier);
    }

    @Test
    void testPrimaryActionDoesNotAttackBeforeAttackTime() {
        sword.primaryAction(mockWorldView, mockUser, mockModifiers);
        sword.primaryAction(mockWorldView, mockUser, mockModifiers);

        verify(mockAttack, never()).attack(any(), any(), any());
    }

    @Test
    void testPrimaryActionAttacksAfterAttackTime() {
        for (int i = 0; i < 30; i++) {
            sword.primaryAction(mockWorldView, mockUser, mockModifiers);
        }

        verify(mockAttack, times(1)).attack(mockWorldView, mockUser, mockDamageModifier);
    }

    @Test
    void testAttackClockResetsAfterAttack() {
        for (int i = 0; i < 30; i++) {
            sword.primaryAction(mockWorldView, mockUser, mockModifiers);
        }
        verify(mockAttack, times(1)).attack(mockWorldView, mockUser, mockDamageModifier);

        sword.primaryAction(mockWorldView, mockUser, mockModifiers);
        sword.primaryAction(mockWorldView, mockUser, mockModifiers);

        verifyNoMoreInteractions(mockAttack);
    }

    @Test
    void testMultipleAttackCycles() {
        for (int i = 0; i < 30; i++) {
            sword.primaryAction(mockWorldView, mockUser, mockModifiers);
        }

        for (int i = 0; i < 30; i++) {
            sword.primaryAction(mockWorldView, mockUser, mockModifiers);
        }

        verify(mockAttack, times(2)).attack(mockWorldView, mockUser, mockDamageModifier);
    }
}
