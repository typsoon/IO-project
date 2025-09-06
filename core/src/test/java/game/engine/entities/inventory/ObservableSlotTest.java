package game.engine.entities.inventory;

import game.engine.entities.items.IItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ObservableSlotTest {

    private ISlot mockSlot;
    private IItem mockItem;
    private Consumer<IItem> mockConsumer;

    @SuppressWarnings("unchecked")
    @BeforeEach
    void setUp() {
        mockSlot = mock(ISlot.class);
        mockItem = mock(IItem.class);
        mockConsumer = mock(Consumer.class);
    }

    @Test
    void testGetItemDelegatesToSlot() {
        when(mockSlot.getItem()).thenReturn(mockItem);
        ObservableSlot observable = new ObservableSlot(mockConsumer, mockSlot);

        assertEquals(mockItem, observable.getItem());
        verify(mockSlot).getItem();
    }

    @Test
    void testGetItemCountDelegatesToSlot() {
        when(mockSlot.getItemCount()).thenReturn(42);
        ObservableSlot observable = new ObservableSlot(mockConsumer, mockSlot);

        assertEquals(42, observable.getItemCount());
        verify(mockSlot).getItemCount();
    }

    @Test
    void testAddItemsDelegatesToSlot() {
        when(mockSlot.addItems(5, mockItem)).thenReturn(true);
        ObservableSlot observable = new ObservableSlot(mockConsumer, mockSlot);

        boolean result = observable.addItems(5, mockItem);

        assertTrue(result);
        verify(mockSlot).addItems(5, mockItem);
    }

    @Test
    void testRemoveItemsTriggersConsumerWhenEmpty() {
        when(mockSlot.removeItems(5)).thenReturn(true);
        when(mockSlot.isEmpty()).thenReturn(true);
        when(mockSlot.getItem()).thenReturn(mockItem);

        ObservableSlot observable = new ObservableSlot(mockConsumer, mockSlot);

        boolean result = observable.removeItems(5);

        assertTrue(result);
        verify(mockSlot).removeItems(5);
        verify(mockConsumer).accept(mockItem);
    }

    @Test
    void testRemoveItemsDoesNotTriggerConsumerWhenNotEmpty() {
        when(mockSlot.removeItems(2)).thenReturn(true);
        when(mockSlot.isEmpty()).thenReturn(false);

        ObservableSlot observable = new ObservableSlot(mockConsumer, mockSlot);

        boolean result = observable.removeItems(2);

        assertTrue(result);
        verify(mockSlot).removeItems(2);
        verify(mockConsumer, never()).accept(any());
    }

    @Test
    void testRemoveItemsDoesNotTriggerConsumerWhenRemoveFails() {
        when(mockSlot.removeItems(3)).thenReturn(false);

        ObservableSlot observable = new ObservableSlot(mockConsumer, mockSlot);

        boolean result = observable.removeItems(3);

        assertFalse(result);
        verify(mockSlot).removeItems(3);
        verify(mockConsumer, never()).accept(any());
    }

    @Test
    void testRemoveItemsTriggersConsumerWithLastItem() {
        when(mockSlot.getItem()).thenReturn(mockItem);     // item before removal
        when(mockSlot.removeItems(5)).thenReturn(true);
        when(mockSlot.isEmpty()).thenReturn(true);

        ObservableSlot observable = new ObservableSlot(mockConsumer, mockSlot);

        boolean result = observable.removeItems(5);

        assertTrue(result);
        verify(mockConsumer).accept(mockItem);
    }
}
