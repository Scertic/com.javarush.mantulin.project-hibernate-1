package com.game.repository;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        verify(session).close();
        assertInstanceOf(Player.class, save);
        assertSame(preSave, save);
    }

    @Test
    void update_shouldReturnPlayer() {
        Player preSave = new Player();
        when(sessionFactory.openSession()).thenReturn(session);
        when(session.beginTransaction()).thenReturn(transaction);

        Player save = playerRepositoryDB.update(preSave);

        verify(session).update(preSave);
        verify(transaction).commit();
        verify(session).close();
        assertInstanceOf(Player.class, save);
        assertSame(preSave, save);
    }

    @Test
    void findById_shouldReturnOptionalOfPlayer() {
    }

    @Test
    void delete() {
    }

    @Test
    void beforeStop() {
    }
}