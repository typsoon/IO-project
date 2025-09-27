package game.engine.entities.inventory;

import game.actions.UsageType;
import game.engine.IWorldView;
import game.engine.entities.IEntity;
import game.engine.entities.IUsageModifiers;
import game.engine.entities.items.IItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InventoryTest {

    private Inventory inventory;
    private IItem mockItem;
    private IWorldView mockWorldView;
    private IEntity mockUser;
    private IUsageModifiers mockModifiers;

    @BeforeEach
    void setUp() {
        inventory = new Inventory(3); // 3 slots
        mockItem = mock(IItem.class);
        when(mockItem.stackSize()).thenReturn(1);
        mockWorldView = mock(IWorldView.class);
        mockUser = mock(IEntity.class);
        mockModifiers = mock(IUsageModifiers.class);
    }

    @Test
    void testInitialResourcesAreZero() {
        for (Resource r : Resource.values()) {
            assertEquals(0, inventory.getResourceCount(r));
        }
    }

    @Test
    void testAddAndRemoveResource() {
        inventory.addResource(Resource.WOOD, 5);
        assertEquals(5, inventory.getResourceCount(Resource.WOOD));

        boolean removed = inventory.removeResource(Resource.WOOD, 3);
        assertTrue(removed);
        assertEquals(2, inventory.getResourceCount(Resource.WOOD));
    }

    @Test
    void testRemoveResourceFailsWhenNotEnough() {
        boolean result = inventory.removeResource(Resource.STONE, 10);
        assertFalse(result);
        assertEquals(0, inventory.getResourceCount(Resource.STONE));
    }

    @Test
    void testSwapSlots() {
        ISlot slot1 = inventory.getSlot(0);
        ISlot slot2 = inventory.getSlot(1);

        inventory.swapSlots(0, 1);

        assertSame(slot1, inventory.getSlot(1));
        assertSame(slot2, inventory.getSlot(0));
    }

    @Test
    void testUseSlotEquipsAndTriggersPrimaryAction() {
        // put item into slot 0
        inventory.getSlot(0).addItems(1, mockItem);

        inventory.useSlot(0, UsageType.PRIMARY, mockWorldView, mockUser, mockModifiers);

        verify(mockItem).itemEquip(mockWorldView, mockUser, mockModifiers);
        verify(mockItem).primaryAction(mockWorldView, mockUser, mockModifiers);
    }

    @Test
    void testUseSlotSwitchesActiveSlot() {
        IItem item1 = mock(IItem.class);
        when(item1.stackSize()).thenReturn(1);
        IItem item2 = mock(IItem.class);
        when(item2.stackSize()).thenReturn(1);


        inventory.getSlot(0).addItems(1, item1);
        inventory.getSlot(1).addItems(1, item2);

        // first use slot 0
        inventory.useSlot(0, UsageType.PRIMARY, mockWorldView, mockUser, mockModifiers);
        verify(item1).itemEquip(mockWorldView, mockUser, mockModifiers);

        // now use slot 1, should unequip item1 and equip item2
        inventory.useSlot(1, UsageType.PRIMARY, mockWorldView, mockUser, mockModifiers);

        verify(item1).itemUnequip(mockWorldView, mockUser, mockModifiers);
        verify(item2).itemEquip(mockWorldView, mockUser, mockModifiers);
    }

    @Test
    void testUseSlotSecondaryAndSpecialActions() {
        inventory.getSlot(0).addItems(1, mockItem);

        inventory.useSlot(0, UsageType.SECONDARY, mockWorldView, mockUser, mockModifiers);
        verify(mockItem).secondaryAction(mockWorldView, mockUser, mockModifiers);

        inventory.useSlot(0, UsageType.SPECIAL, mockWorldView, mockUser, mockModifiers);
        verify(mockItem).specialAction(mockWorldView, mockUser, mockModifiers);
    }

    @Test
    void testUseSlotDoesNothingWhenEmpty() {
        // slot 0 is empty by default
        inventory.useSlot(0, UsageType.PRIMARY, mockWorldView, mockUser, mockModifiers);

        verifyNoInteractions(mockItem);
    }

    @Test
    void testActiveSlotResetsWhenItemConsumed() {
        inventory.getSlot(0).addItems(1, mockItem);
        inventory.useSlot(0, UsageType.PRIMARY, mockWorldView, mockUser, mockModifiers);

        // remove item, ObservableSlot should trigger checkActiveSlot
        inventory.getSlot(0).removeItems(1);

        // now slot is empty, using it should not trigger any actions
        inventory.useSlot(0, UsageType.PRIMARY, mockWorldView, mockUser, mockModifiers);
        verify(mockItem, times(1)).primaryAction(any(), any(), any()); // only once from first call
    }

    @Test
    void testGetInventoryInfoReturnsCorrectData() {
        inventory.addResource(Resource.WOOD, 7);
        inventory.getSlot(0).addItems(1, mockItem);

        InventoryInfo info = inventory.getInventoryInfo();

        assertEquals(3, info.slotNum());
        assertEquals(3, info.slots().getData().length);
        assertEquals(Resource.values().length, info.resources().getData().length);

        assertTrue(Arrays.stream(info.resources().getData()).anyMatch(r -> r.resource() == Resource.WOOD && r.amount() == 7));
    }
}
