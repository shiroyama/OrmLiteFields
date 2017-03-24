package us.shiroyama.android.ormlitefields.internal;

import android.annotation.TargetApi;

import com.google.auto.service.AutoService;
import com.google.common.collect.ImmutableSet;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

import static us.shiroyama.android.ormlitefields.internal.utils.StringUtils.camelToSnake;
import static us.shiroyama.android.ormlitefields.internal.utils.StringUtils.stripFirstAndLastUnderscore;

@AutoService(Processor.class)
public class OrmLiteFieldsProcessor extends AbstractProcessor {
    private Elements elementUtils;
    private Types typeUtils;
    private Messager messager;
    private Filer filer;

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return ImmutableSet.of(DatabaseTable.class.getName(), DatabaseField.class.getName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latest();
    }

    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);

        elementUtils = env.getElementUtils();
        typeUtils = env.getTypeUtils();
        messager = env.getMessager();
        filer = env.getFiler();
    }

    @TargetApi(24)
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment env) {
        if (set.isEmpty()) {
            return true;
        }

        env.getElementsAnnotatedWith(DatabaseTable.class)
                .stream()
                .filter(element -> element.getKind() == ElementKind.CLASS)
                .map(element -> (TypeElement) element)
                .forEach(databaseClass -> {
                    String className = databaseClass.getSimpleName().toString();
                    String fieldClassName = className + "Fields";
                    String packageName = elementUtils.getPackageOf(databaseClass).toString();

                    Map<String, String> fieldNameMap = databaseClass.getEnclosedElements()
                            .stream()
                            .filter(element -> element.getKind() == ElementKind.FIELD)
                            .filter(element -> element.getAnnotation(DatabaseField.class) != null)
                            .map(element -> (VariableElement) element)
                            .map(field -> {
                                DatabaseField databaseField = field.getAnnotation(DatabaseField.class);
                                String columnName = databaseField.columnName();
                                if (!"".equals(columnName)) {
                                    return columnName;
                                }
                                return field.getSimpleName().toString();
                            })
                            .collect(Collectors.toMap(
                                    OrmLiteFieldsProcessor::convertFieldName,
                                    fieldName -> fieldName
                            ));

                    List<FieldSpec> fieldSpecs = fieldNameMap.entrySet()
                            .stream()
                            .map(entry -> {
                                String field = entry.getKey();
                                String fieldValue = entry.getValue();
                                return FieldSpec.builder(String.class, field)
                                        .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                                        .initializer("$S", fieldValue)
                                        .build();
                            })
                            .collect(Collectors.toList());

                    try {
                        TypeSpec typeSpec = TypeSpec
                                .classBuilder(fieldClassName)
                                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                                .addFields(fieldSpecs)
                                .build();
                        JavaFile javaFile = JavaFile
                                .builder(packageName, typeSpec)
                                .build();
                        javaFile.writeTo(filer);
                    } catch (IOException e) {
                        messager.printMessage(Diagnostic.Kind.ERROR, e.getMessage(), databaseClass);
                    }
                });

        return false;
    }

    private static String convertFieldName(String from) {
        return stripFirstAndLastUnderscore(camelToSnake(from)).toUpperCase();
    }

}
