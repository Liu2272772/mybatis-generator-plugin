package org.shtl.mybatis.generator.impl;

import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.AbstractJavaMapperMethodGenerator;

import java.util.List;
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
    private static final String LIST = "list";
    private static final String BATCH_INSERT = "batchInsert";

    @Override
    public void addInterfaceElements(Interface anInterface) {
        //添加findOne
        addInterfaceFindOne(anInterface);

        //添加list方法
        addInterfaceList(anInterface);

        //批量添加方法
        addInterfaceBatchInsert(anInterface);
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

    /**
     * 添加list方法
     *
     * @param anInterface Interface
     */
    private void addInterfaceList(Interface anInterface) {
        //创建import对象
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<>();
        //添加List包
        importedTypes.add(FullyQualifiedJavaType.getNewListInstance());
        //添加类型对象
        FullyQualifiedJavaType parameterType;
        if (introspectedTable.getRules().generateRecordWithBLOBsClass()) {
            parameterType = new FullyQualifiedJavaType(introspectedTable.getRecordWithBLOBsType());
        } else {
            parameterType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
        }
        importedTypes.add(parameterType);

        //方法对象
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        FullyQualifiedJavaType returnType = FullyQualifiedJavaType.getNewListInstance();
        returnType.addTypeArgument(parameterType);
        method.setReturnType(returnType);
        method.setName(LIST);
        method.addParameter(new Parameter(parameterType, "record"));

        addMapperAnnotations(anInterface, method);
        context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
        if (context.getPlugins().clientSelectByPrimaryKeyMethodGenerated(method, anInterface, introspectedTable)) {
            anInterface.addImportedTypes(importedTypes);
            anInterface.addMethod(method);
        }
    }

    /**
     * 批量添加
     *
     * @param anInterface Interface
     */
    private void addInterfaceBatchInsert(Interface anInterface) {
        //创建import对象
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<>();
        //添加List包
        importedTypes.add(FullyQualifiedJavaType.getNewListInstance());
        //添加类型对象
        FullyQualifiedJavaType parameterType;
        if (introspectedTable.getRules().generateRecordWithBLOBsClass()) {
            parameterType = new FullyQualifiedJavaType(introspectedTable.getRecordWithBLOBsType());
        } else {
            parameterType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
        }
        importedTypes.add(parameterType);

        //方法对象
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        PrimitiveTypeWrapper integerWrapper = FullyQualifiedJavaType.getIntInstance().getPrimitiveTypeWrapper();
        method.setReturnType(integerWrapper);
        method.setName(BATCH_INSERT);
        FullyQualifiedJavaType parameterTypeList = FullyQualifiedJavaType.getNewListInstance();
        parameterTypeList.addTypeArgument(parameterType);
        method.addParameter(new Parameter(parameterTypeList, "recordList", "@Param(\"recordList\")"));

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
