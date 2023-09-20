package dbTest;

import database.Database;
import database.models.Game;
import database.models.Player;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class dbTest {
    static Database database;
    @BeforeAll
    public static void setup() {
        database = new Database();
    }

    @Test
    void testAddPlayerOnDatabase(){
        database.addPlayerOnDatabase("test");
        final Session session = database.sessionFactory.getCurrentSession();
        session.beginTransaction();
        final Query query = session.createQuery("FROM Player where nickname = :name").setParameter("name", "test");
        final Player player = (Player) query.uniqueResult();
        Assertions.assertEquals(player.nickname, "test");
        session.delete(player);
        session.getTransaction().commit();
    }
    @Test
    void testCheckRegistration(){
        final boolean checkFalse = database.checkRegistration("test");
        Assertions.assertEquals(checkFalse, false);

        database.addPlayerOnDatabase("test");

        final boolean checkTrue = database.checkRegistration("test");
        Assertions.assertEquals(checkTrue, true);

        final Session session = database.sessionFactory.getCurrentSession();
        session.beginTransaction();
        final Query query = session.createQuery("FROM Player where nickname = :name").setParameter("name", "test");
        final Player player = (Player) query.uniqueResult();
        session.delete(player);
        session.getTransaction().commit();
    }
    @Test
    void testAddGame(){
        database.addGame(new Game(1, 1, 'b', 1));

        final Session session = database.sessionFactory.getCurrentSession();
        session.beginTransaction();
        final Query query = session.createQuery("FROM Game where gameId = :gameId").setParameter("gameId", 1);
        final Game game = (Game) query.uniqueResult();
        Assertions.assertEquals(new Game(1, 1 ,'b' , 1), game);
        session.delete(game);
        session.getTransaction().commit();
    }

}
