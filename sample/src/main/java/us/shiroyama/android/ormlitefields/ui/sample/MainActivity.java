package us.shiroyama.android.ormlitefields.ui.sample;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;
import us.shiroyama.android.ormlitefields.R;
import us.shiroyama.android.ormlitefields.ui.sample.db.Country;
import us.shiroyama.android.ormlitefields.ui.sample.db.CountryFields;
import us.shiroyama.android.ormlitefields.ui.sample.db.MySqliteOpenHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView text = (TextView) findViewById(R.id.text);

        MySqliteOpenHelper helper = OpenHelperManager.getHelper(this, MySqliteOpenHelper.class);
        try {
            Dao<Country, Long> dao = helper.getDao(Country.class);
            PreparedQuery<Country> preparedQuery = dao.queryBuilder().where().eq(CountryFields.NAME, "Albania").prepare();
            List<Country> countries = dao.query(preparedQuery);
            if (countries.isEmpty()) {
                Timber.d("result is empty.");
                return;
            }
            final List<String> countryNames = new ArrayList<>(countries.size());
            for (Country country : countries) {
                countryNames.add(country.getName());
            }
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    text.setText(TextUtils.join("\n", countryNames));
                }
            });
        } catch (SQLException e) {
            Timber.e(e.getMessage(), e);
        }
    }

    @Override
    protected void onDestroy() {
        OpenHelperManager.releaseHelper();
        super.onDestroy();
    }

}
