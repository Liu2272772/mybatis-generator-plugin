package org.shtl.mybatis.generator.plugin;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.util.List;

/**
 * A MyBatis Generator plugin to use Lombok's annotations.
 * For example, use @Data annotation instead of getter ands setter.
 *
 * @author Paolo Predonzani (http://softwareloop.com/)
 */
public class LombokPlugin extends PluginAdapter {

    private FullyQualifiedJavaType dataAnnotation;

    public LombokPlugin() {
        dataAnnotation = new FullyQualifiedJavaType("lombok.Data");
    }

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        addDataAnnotation(topLevelClass);
        return true;
    }

    @Override
    public boolean modelPrimaryKeyClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        addDataAnnotation(topLevelClass);
        return true;
    }

    @Override
    public boolean modelRecordWithBLOBsClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        addDataAnnotation(topLevelClass);
        return true;
    }

    @Override
    public boolean modelGetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        return false;
    }

    @Override
    public boolean modelSetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn,IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        return false;
    }

    /**
     * add @Data annotation
     * @param topLevelClass TopLevelClass
     */
    private void addDataAnnotation(TopLevelClass topLevelClass) {
        topLevelClass.addImportedType(dataAnnotation);
        topLevelClass.addAnnotation("@Data");
    }

}
