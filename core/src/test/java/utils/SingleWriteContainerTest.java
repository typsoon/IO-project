package utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SingleWriteContainerTest {

    @Test
    void getContents_throwsIfNotSet() {
        SingleWriteContainer<String> container = new SingleWriteContainer<>();
        assertThrows(NullPointerException.class, container::getContents);
    }

    @Test
    void setContents_storesValue() {
        SingleWriteContainer<String> container = new SingleWriteContainer<>();
        container.setContents("hello");

        assertEquals("hello", container.getContents());
    }

    @Test
    void setContents_twice_throws() {
        SingleWriteContainer<String> container = new SingleWriteContainer<>();
        container.setContents("first");

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> container.setContents("second"));

        assertTrue(ex.getMessage().contains("first"));
    }

    @Test
    void getContents_returnsSameObject() {
        Object obj = new Object();
        SingleWriteContainer<Object> container = new SingleWriteContainer<>();
        container.setContents(obj);

        assertSame(obj, container.getContents());
    }
}
