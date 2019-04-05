package org.shtl.mybatis.generator.impl;

import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.AbstractJavaMapperMethodGenerator;

import java.util.Set;
import java.util.TreeSet;

/**
 * 添加方法
 *
 * @author Zhaolong Liu
 * @create 2019-04-05
 */
public class CustomJavaMapperMethodGenerator extends AbstractJavaMapperMethodGenerator {

    //方法名称
    private static final String FIND_ONE = "finOne";

    @Override
    public void addInterfaceElements(Interface anInterface) {
        //添加findOne
        addInterfaceFindOne(anInterface);
    }

    /**
     * 添加findOne方法
     *
     * @param anInterface Interface
     */
    private void addInterfaceFindOne(Interface anInterface) {
        //创建import对象
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<>();
        //添加List包
        importedTypes.add(FullyQualifiedJavaType.getNewListInstance());
        //添加类型对象
        FullyQualifiedJavaType returnType;
        if (introspectedTable.getRules().generateRecordWithBLOBsClass()) {
            returnType = new FullyQualifiedJavaType(introspectedTable.getRecordWithBLOBsType());
        } else {
            returnType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
        }

        importedTypes.add(returnType);

        //方法对象
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(returnType);//设置放回类型对象
        method.setName(FIND_ONE);//设置方法名称
        //添加参数
        method.addParameter(new Parameter(returnType, "record"));

        addMapperAnnotations(anInterface, method);
        context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
        if (context.getPlugins().clientSelectByPrimaryKeyMethodGenerated(method, anInterface, introspectedTable)) {
            anInterface.addImportedTypes(importedTypes);
            anInterface.addMethod(method);
        }
    }

    public void addMapperAnnotations(Interface interfaze, Method method) {
    }
}
