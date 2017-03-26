package us.shiroyama.android.ormlitefields.internal;

import android.annotation.TargetApi;

import com.google.testing.compile.JavaFileObjects;

import org.apache.commons.io.IOUtils;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.tools.JavaFileObject;

import static com.google.common.truth.Truth.assert_;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;

/**
 * Test for {@link OrmLiteFieldsProcessor}
 *
 * @author Fumihiko Shiroyama
 */

public class OrmLiteFieldsProcessorTest {
    @Ignore
    @Test
    public void valid() throws Exception {
        assert_()
                .about(javaSource())
                .that(readResource("Country.java"))
                .processedWith(new OrmLiteFieldsProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(readResource("CountryFields.java"));
    }

    @TargetApi(24)
    private JavaFileObject readResource(String fileName) {
        File file = new File("src/test/assets", fileName);
        try (FileInputStream fis = new FileInputStream(file)) {
            String className = fileName.substring(0, fileName.indexOf("."));
            return JavaFileObjects
                    .forSourceString(className, IOUtils.toString(fis, "UTF-8"));
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
