package org.shtl.mybatis.generator;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaElement;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.internal.DefaultCommentGenerator;

import java.util.Date;
import java.util.Properties;

/**
 * mybatis generator 自定义CommentGenerator
 *
 * @author cuiyang
 */
public class MyCommentGenerator extends DefaultCommentGenerator {

    private static final String AUTHOR = "author";

    /** properties */
    private Properties properties = new Properties();
    /** author */
    private String author = "";

    @Override
    public void addConfigurationProperties(Properties properties) {
        this.properties.putAll(properties);
        this.author = this.properties.getProperty(AUTHOR);
        if (null == author || "".equals(author)) {
            author = System.getProperty("user.name");
        }
        super.addConfigurationProperties(properties);
    }

    @Override
    public void addFieldComment(Field field, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
        field.addJavaDocLine("/** " + introspectedColumn.getRemarks() + " */");

        //swagger类
        field.addJavaDocLine(" @ApiModelProperty(value = \"" + introspectedColumn.getRemarks() + "\" name = \"" + field.getName() + "\")");
    }

    @Override
    public void addModelClassComment(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        topLevelClass.addJavaDocLine("/**");
        topLevelClass.addJavaDocLine(" * " + introspectedTable.getRemarks());
        topLevelClass.addJavaDocLine(" *");
        topLevelClass.addJavaDocLine(" * <p>table name : " + introspectedTable.getFullyQualifiedTable().getIntrospectedTableName());
        topLevelClass.addJavaDocLine(" *");
        topLevelClass.addJavaDocLine(" * @author " + author);
        topLevelClass.addJavaDocLine(" */");

        //swagger类
        FullyQualifiedJavaType modelType = new FullyQualifiedJavaType("io.swagger.annotations.ApiModel");
        topLevelClass.addImportedType(modelType);
        FullyQualifiedJavaType propertyType = new FullyQualifiedJavaType("io.swagger.annotations.ApiModelProperty;");
        topLevelClass.addImportedType(propertyType);
        topLevelClass.addJavaDocLine("@ApiModel(description= \"" + introspectedTable.getRemarks() + "\")");

    }

    @Override
    protected void addJavadocTag(JavaElement javaElement, boolean markAsDoNotDelete) {}
}
