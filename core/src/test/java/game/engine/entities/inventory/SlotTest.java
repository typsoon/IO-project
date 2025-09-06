package game.engine.entities.inventory;

import game.engine.entities.items.IItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SlotTest {

    private IItem mockItem;

    @BeforeEach
    void setUp() {
        mockItem = mock(IItem.class);
        when(mockItem.stackSize()).thenReturn(10);
    }

    @Test
    void testEmptyConstructor() {
        Slot slot = new Slot();
        assertNull(slot.getItem());
        assertEquals(0, slot.getItemCount());
    }

    @Test
    void testParameterizedConstructor() {
        Slot slot = new Slot(mockItem, 5);
        assertEquals(mockItem, slot.getItem());
        assertEquals(5, slot.getItemCount());
    }

    @Test
    void testAddItemsToEmptySlotSuccess() {
        Slot slot = new Slot();
        boolean result = slot.addItems(5, mockItem);

        assertTrue(result);
        assertEquals(mockItem, slot.getItem());
        assertEquals(5, slot.getItemCount());
    }

    @Test
    void testAddItemsExceedingStackSizeFails() {
        Slot slot = new Slot();
        boolean result = slot.addItems(15, mockItem);

        assertFalse(result);
        assertNull(slot.getItem());
        assertEquals(0, slot.getItemCount());
    }

    @Test
    void testAddItemsToSameItemStackSuccess() {
        Slot slot = new Slot(mockItem, 5);

        boolean result = slot.addItems(3, mockItem);

        assertTrue(result);
        assertEquals(8, slot.getItemCount());
    }

    @Test
    void testAddItemsToSameItemStackFailsWhenExceedingStackSize() {
        Slot slot = new Slot(mockItem, 9);

        boolean result = slot.addItems(5, mockItem);

        assertFalse(result);
        assertEquals(9, slot.getItemCount());
    }

    @Test
    void testAddItemsToDifferentItemFails() {
        IItem otherItem = mock(IItem.class);
        when(otherItem.stackSize()).thenReturn(10);

        Slot slot = new Slot(mockItem, 5);

        boolean result = slot.addItems(3, otherItem);

        assertFalse(result);
        assertEquals(5, slot.getItemCount());
        assertEquals(mockItem, slot.getItem());
    }

    @Test
    void testRemoveItemsSuccess() {
        Slot slot = new Slot(mockItem, 5);

        boolean result = slot.removeItems(3);

        assertTrue(result);
        assertEquals(2, slot.getItemCount());
        assertEquals(mockItem, slot.getItem());
    }

    @Test
    void testRemoveItemsAll() {
        Slot slot = new Slot(mockItem, 5);

        boolean result = slot.removeItems(5);

        assertTrue(result);
        assertEquals(0, slot.getItemCount());
        assertNull(slot.getItem());
    }

    @Test
    void testRemoveItemsFailsWhenNotEnough() {
        Slot slot = new Slot(mockItem, 2);

        boolean result = slot.removeItems(5);

        assertFalse(result);
        assertEquals(2, slot.getItemCount());
        assertEquals(mockItem, slot.getItem());
    }

    @Test
    void testRemoveZeroItemsFromEmptySlotFails() {
        Slot slot = new Slot(); // empty

        boolean result = slot.removeItems(0);

        assertFalse(result);
        assertNull(slot.getItem());
        assertEquals(0, slot.getItemCount());
    }
}