package org.example;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MyClassTest {

    @Mock
    ThreadLocal<Integer> threadLocalMock;

    @Mock
    Map<String, ThreadLocal<Integer>> threadLocalMapMock;

    @InjectMocks
    MyClass myClass;

    @BeforeEach
    void setUp() {
        when(threadLocalMapMock.get("Value1")).thenReturn(threadLocalMock);
        when(threadLocalMock.get()).thenReturn(1);
    }

    @Test
    void usingTryWithResources_returnsInt() {
        try (final MockedConstruction<AutoClosableResource> autoClosableResourceMock = mockConstruction(AutoClosableResource.class)) {
            // When
            final int result = myClass.usingTryWithResources();

            // Then
            assertEquals(1, result);
            verify(threadLocalMock).get();
            verify(threadLocalMock).remove();

            final AutoClosableResource mock = autoClosableResourceMock.constructed().getFirst();
            verify(mock).close();
        }
    }

    @Test
    void returnValueWithoutAutoClosable_returnsInt() {
        try (final MockedConstruction<AutoClosableResource> autoClosableResourceMock = mockConstruction(AutoClosableResource.class)) {
            // When
            final int result = myClass.explicitlyClosingResource();

            // Then
            assertEquals(1, result);
            verify(threadLocalMock).get();
            verify(threadLocalMock).remove();

            final AutoClosableResource mock = autoClosableResourceMock.constructed().getFirst();
            verify(mock).close();
        }
    }
}