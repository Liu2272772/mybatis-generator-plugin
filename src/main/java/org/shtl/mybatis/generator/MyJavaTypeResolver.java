package org.shtl.mybatis.generator;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.internal.types.JavaTypeResolverDefaultImpl;

import java.sql.Types;

/**
 * mybatis generator 自定义JavaTypeResolver
 *
 * @author cuiyang
 */
public class MyJavaTypeResolver extends JavaTypeResolverDefaultImpl {

    private static final String BOOLEAN_TYPE_PREFIX = "is_";

    public MyJavaTypeResolver() {
        // 不使用Byte
        typeMap.put(Types.TINYINT, new JdbcTypeInformation("TINYINT",
                new FullyQualifiedJavaType(Integer.class.getName())));
    }

    @Override
    protected FullyQualifiedJavaType overrideDefaultType(IntrospectedColumn column, FullyQualifiedJavaType defaultType) {
        // 解析boolean类型
        if (column.getJdbcType() == Types.TINYINT && column.getActualColumnName().startsWith(BOOLEAN_TYPE_PREFIX)) {
            column.setJavaProperty(column.getActualColumnName().substring(3));
            return new FullyQualifiedJavaType(Boolean.class.getName());
        }
        return super.overrideDefaultType(column, defaultType);
    }
}
