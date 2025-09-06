package game.engine.entities.items.items;

import game.engine.IWorldView;
import game.engine.entities.IEntity;
import game.engine.entities.items.UsageModifiers;
import game.engine.entities.items.attacks.IAttack;
import game.engine.entities.items.attacks.DamageModifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class BasicSwordTest {

    private BasicSword sword;
    private IWorldView mockWorldView;
    private IEntity mockUser;
    private UsageModifiers mockModifiers;
    private DamageModifier mockDamageModifier;
    private IAttack mockAttack;

    @BeforeEach
    void setUp() {
        sword = new BasicSword();
        mockWorldView = mock(IWorldView.class);
        mockUser = mock(IEntity.class);
        mockModifiers = mock(UsageModifiers.class);
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

        sword.primaryAction(mockWorldView, mockUser, mockModifiers);
        sword.primaryAction(mockWorldView, mockUser, mockModifiers);
        sword.primaryAction(mockWorldView, mockUser, mockModifiers);
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
        sword.primaryAction(mockWorldView, mockUser, mockModifiers);
        sword.primaryAction(mockWorldView, mockUser, mockModifiers);
        sword.primaryAction(mockWorldView, mockUser, mockModifiers);

        verify(mockAttack, times(1)).attack(mockWorldView, mockUser, mockDamageModifier);
    }

    @Test
    void testAttackClockResetsAfterAttack() {
        sword.primaryAction(mockWorldView, mockUser, mockModifiers);
        sword.primaryAction(mockWorldView, mockUser, mockModifiers);
        sword.primaryAction(mockWorldView, mockUser, mockModifiers);

        verify(mockAttack, times(1)).attack(mockWorldView, mockUser, mockDamageModifier);

        sword.primaryAction(mockWorldView, mockUser, mockModifiers);
        sword.primaryAction(mockWorldView, mockUser, mockModifiers);

        verifyNoMoreInteractions(mockAttack);
    }

    @Test
    void testMultipleAttackCycles() {
        sword.primaryAction(mockWorldView, mockUser, mockModifiers);
        sword.primaryAction(mockWorldView, mockUser, mockModifiers);
        sword.primaryAction(mockWorldView, mockUser, mockModifiers);

        sword.primaryAction(mockWorldView, mockUser, mockModifiers);
        sword.primaryAction(mockWorldView, mockUser, mockModifiers);
        sword.primaryAction(mockWorldView, mockUser, mockModifiers);

        verify(mockAttack, times(2)).attack(mockWorldView, mockUser, mockDamageModifier);
    }
}
