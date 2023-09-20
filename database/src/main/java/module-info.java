module reversi.deeplay.database.main {
    requires org.hibernate.orm.core;
    exports database;
    exports database.models;
    requires java.naming;
    requires java.persistence;
}