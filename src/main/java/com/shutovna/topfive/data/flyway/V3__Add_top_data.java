package com.shutovna.topfive.data.flyway;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Component
public class V3__Add_top_data extends BaseJavaMigration {


    @Override
    public void migrate(Context context) throws SQLException {
        Connection connection = context.getConnection();

        try (Statement statement = connection.createStatement()) {
            statement.execute("INSERT INTO topfive.top(id,type,title,details,user_id) " +
                    "VALUES(1, 0, 'Лучшие чилаут композиции', 'Из сборника Chillout Brilliants', 1)");
            statement.execute("INSERT INTO topfive.top(id,type,title,details,user_id) " +
                    "VALUES(2, 0, 'Лучшие метал композиции', 'Из сборника Top 100 Metall Songs', 1)");

            statement.execute("INSERT INTO topfive.top(id,type,title,details,user_id) " +
                    "VALUES(3, 1, 'Лучшие котики', 'Из глубин ютуба', 1)");
            statement.execute("INSERT INTO topfive.top(id,type,title,details,user_id) " +
                    "VALUES(4, 1, 'Подборка приколов', null, 1)");

            statement.execute("INSERT INTO topfive.top(id,type,title,details,user_id) " +
                    "VALUES(5, 2, 'Мои фото', 'Из личной коллекции', 1)");
            statement.execute("INSERT INTO topfive.top(id,type,title,details,user_id) " +
                    "VALUES(6, 2, 'Отпуск', 'Турция', 1)");

            statement.execute("INSERT INTO topfive.top(id,type,title,details,user_id) " +
                    "VALUES(7, 0, 'Лучшие psy-trance сеты', null, 1)");
        }
    }
}
