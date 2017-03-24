# OrmLiteFields

OrmLiteFields generates the class that holds [OrmLite](http://ormlite.com/)'s Database field names corresponding to `@DatabaseField` annotations.

## Installation

```
annotationProcessor 'us.shiroyama.android:ormlitefields-processor:1.0.0'
compile 'com.j256.ormlite:ormlite-android:5.0'
```

## How to Use

If you have a [OrmLite](http://ormlite.com/)'s model class like below.

```java
@DatabaseTable(tableName = "country")
public class Country {
  @DatabaseField(columnName = BaseColumns._ID, generatedId = true)
  private Long id;

  @DatabaseField
  private String name;

  @DatabaseField(columnName = "country_code")
  private int countryCode;

  @DatabaseField(columnName = "sub_country_code")
  private Integer subCountryCode;

  @DatabaseField(columnName = "iso_code")
  private String isoCode;

  @SuppressWarnings("unused")
  private Country() {}
}
```

OrmLiteFields will generate a field class like this.

```java
public final class CountryFields {
  public static final String ISO_CODE = "iso_code";

  public static final String ID = "_id";

  public static final String SUB_COUNTRY_CODE = "sub_country_code";

  public static final String COUNTRY_CODE = "country_code";

  public static final String NAME = "name";
}
```

You can use them like below.

```java
Dao<Country, Long> dao = helper.getDao(Country.class);
PreparedQuery<Country> preparedQuery = dao.queryBuilder().where().eq(CountryFields.NAME, "Albania").prepare();
List<Country> countries = dao.query(preparedQuery);
```

It's safer than querying with raw String `where().eq("name", "Albania")`.

## License

```
Copyright 2017 Fumihiko Shiroyama

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
