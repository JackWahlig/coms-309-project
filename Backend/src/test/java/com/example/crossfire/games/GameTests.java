package com.example.crossfire.games;

import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

public class GameTests {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private Games game1;

    @Mock
    private Games game2;

    @Before
    public void setupMock() {
        MockitoAnnotations.initMocks(this);
        when(gameRepository.save(game1)).thenReturn(game1);
        when(gameRepository.findByGameId(1)).thenReturn(game1);
        when(gameRepository.findByGameId(2)).thenReturn(game2);
        when(game1.getPlayer1()).thenReturn(5);
        when(game2.getPlayer1()).thenReturn(null);
    }

    @Test
    public void testMockCreation() {
        assertNotNull(gameRepository);
        assertNotNull(game1);
        assertNotNull(game2);
    }

    @Test
    public void testGameReturnedAfterBeingSaved() {
        assert(game1.equals(gameRepository.save(game1)));
    }

    @Test
    public void testFindGameById() {
        assert(game1.equals(gameRepository.findByGameId(1)));
    }

    @Test
    public void testNullIsReturnedWhenPlayerNotAssigned() {
        assert(game2.getPlayer1() == null);
    }

    @Test
    public void testNotNullPlayerIdIsReturned() {
        assert(game1.getPlayer1().equals(5));
    }

}
