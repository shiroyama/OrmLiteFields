package us.shiroyama.android.ormlitefields.ui.sample.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by shiroyama on 2017/03/24.
 */

public class MySqliteOpenHelper extends OrmLiteSqliteOpenHelper {
    private static final String DATABASE_NAME = "sample.db";
    private static final int DATABASE_VERSION = 1;

    public MySqliteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        Timber.d("MySqliteOpenHelper#onCreate");
        try {
            TableUtils.createTableIfNotExists(connectionSource, Country.class);
            insertInitialData();
        } catch (SQLException e) {
            Timber.e(e.getMessage(), e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        Timber.d("MySqliteOpenHelper#onUpgrade: oldVersion=%d, newVersion=%d", oldVersion, newVersion);
    }

    private void insertInitialData() throws SQLException {
        List<Country> countries = new ArrayList<Country>() {
            {
                add(new Country("Afghanistan", 93, "AF"));
                add(new Country("Albania", 355, "AL"));
                add(new Country("Algeria", 213, "DZ"));
                add(new Country("American Samoa", 1, 684, "AS"));
                add(new Country("Andorra", 376, "AD"));
            }
        };
        Dao<Country, Long> dao = getDao(Country.class);
        dao.create(countries);
    }

}
