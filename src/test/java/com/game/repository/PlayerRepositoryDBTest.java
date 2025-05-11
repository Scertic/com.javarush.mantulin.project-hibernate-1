package com.game.repository;

import com.game.entity.Player;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class PlayerRepositoryDBTest {

    SessionFactory sessionFactory;
    Session session;
    PlayerRepositoryDB playerRepositoryDB;
    Transaction transaction;

    @BeforeEach
    void init() {
        sessionFactory = mock(SessionFactory.class);
        session = mock(Session.class);
        transaction = mock(Transaction.class);
        playerRepositoryDB = new PlayerRepositoryDB(sessionFactory);
    }

    @Test
    void getAll_shouldReturnListPlayer() {
        List<Player> players = new ArrayList<>();
        players.add(new Player());

        when(sessionFactory.openSession()).thenReturn(session);

        NativeQuery<Player> query = mock(NativeQuery.class);
        when(query.setParameter("pageSize", 10)).thenReturn(query);
        when(query.setParameter("offset", 0)).thenReturn(query);
        when(query.list()).thenReturn(players);

        when(session.createNativeQuery(
                "select * from rpg.player order by id ASC limit :pageSize offset :offset",
                Player.class)).thenReturn(query);

        List<Player> result = playerRepositoryDB.getAll(0, 10);

        assertEquals(1, result.size());
    }


    @Test
    void getAllCount() {
        when(sessionFactory.openSession()).thenReturn(session);

        Query<Long> findAll = mock(Query.class);
        when(session.createNamedQuery("Player.findAll", Long.class))
                .thenReturn(findAll);
        when(findAll.uniqueResult()).thenReturn(10L);

        int allCount = playerRepositoryDB.getAllCount();

        verify(findAll).uniqueResult();
        verify(sessionFactory).openSession();

        assertEquals(10, allCount);

    }

    @Test
    void save_shouldReturnPlayer() {
        Player preSave = new Player();
        when(sessionFactory.openSession()).thenReturn(session);
        when(session.beginTransaction()).thenReturn(transaction);

        Player save = playerRepositoryDB.save(preSave);

        verify(session).persist(preSave);
        verify(transaction).commit();
        verify(transaction, never()).rollback();
        verify(session).close();
        assertInstanceOf(Player.class, save);
        assertSame(preSave, save);
    }

    @Test
    void save_shouldReturnRuntimeException() {
        Player preSave = new Player();
        when(sessionFactory.openSession()).thenReturn(session);
        when(session.beginTransaction()).thenReturn(transaction);
        doThrow(new RuntimeException()).when(session).persist(preSave);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            playerRepositoryDB.save(preSave);
        });

        verify(transaction, never()).commit();
        verify(transaction).rollback();
        verify(session).close();
        assertInstanceOf(RuntimeException.class, exception);
    }

    @Test
    void update_shouldReturnPlayer() {
        Player preSave = new Player();
        when(sessionFactory.openSession()).thenReturn(session);
        when(session.beginTransaction()).thenReturn(transaction);

        Player save = playerRepositoryDB.update(preSave);

        verify(session).update(preSave);
        verify(transaction).commit();
        verify(transaction, never()).rollback();
        verify(session).close();
        assertInstanceOf(Player.class, save);
        assertSame(preSave, save);
    }

    @Test
    void findById_shouldReturnOptionalOfPlayer() {
        Player player = new Player();
        when(sessionFactory.openSession()).thenReturn(session);
        when(session.get(Player.class, 1L)).thenReturn(player);

        Optional<Player> finded = playerRepositoryDB.findById(1);

        verify(session).get(Player.class, 1L);
        verify(session).close();
        assertInstanceOf(Optional.class, finded);
        assertInstanceOf(Player.class, finded.get());
    }

    @Test
    void delete() {
        Player player = new Player();
        when(sessionFactory.openSession()).thenReturn(session);
        when(session.beginTransaction()).thenReturn(transaction);

        playerRepositoryDB.delete(player);

        verify(session).delete(player);
        verify(transaction).commit();
        verify(transaction, never()).rollback();
        verify(session).close();
    }

}