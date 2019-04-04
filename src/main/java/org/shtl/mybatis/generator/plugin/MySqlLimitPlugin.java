package org.shtl.mybatis.generator.plugin;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

import java.util.List;

/**
 * MySQL分页插件
 *
 * @author ZhaoLong Liu
 * @since 2018/5/13
 */
public class MySqlLimitPlugin extends PluginAdapter {

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    /**
     * 为每个Example类添加limit和offset属性已经set、get方法
     */
    @Override
    public boolean modelExampleClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        PrimitiveTypeWrapper integerWrapper = FullyQualifiedJavaType.getIntInstance().getPrimitiveTypeWrapper();
        PrimitiveTypeWrapper longWrapper = PrimitiveTypeWrapper.getLongInstance();

        //添加每页数量
        Field pageSize = new Field();
        pageSize.setName("pageSize");
        pageSize.setVisibility(JavaVisibility.PRIVATE);
        pageSize.setType(integerWrapper);
        pageSize.setInitializationString("10");
        topLevelClass.addField(pageSize);

        Method setPageSize = new Method();
        setPageSize.setVisibility(JavaVisibility.PUBLIC);
        setPageSize.setName("setPageSize");
        setPageSize.addParameter(new Parameter(integerWrapper, "pageSize"));
        setPageSize.addBodyLine("this.pageSize = pageSize;");
        setPageSize.addBodyLine("this.offset = pageNo * pageSize;");
        topLevelClass.addMethod(setPageSize);

        Method getPageSize = new Method();
        getPageSize.setVisibility(JavaVisibility.PUBLIC);
        getPageSize.setReturnType(integerWrapper);
        getPageSize.setName("getPageSize");
        getPageSize.addBodyLine("return pageSize;");
        topLevelClass.addMethod(getPageSize);

        //偏移量
        Field offset = new Field();
        offset.setName("offset");
        offset.setVisibility(JavaVisibility.PRIVATE);
        offset.setType(longWrapper);
        offset.setInitializationString("0l");
        topLevelClass.addField(offset);

        Method setOffset = new Method();
        setOffset.setVisibility(JavaVisibility.PUBLIC);
        setOffset.setName("setOffset");
        setOffset.addParameter(new Parameter(longWrapper, "offset"));
        setOffset.addBodyLine("this.offset = offset;");
        topLevelClass.addMethod(setOffset);

        Method getOffset = new Method();
        getOffset.setVisibility(JavaVisibility.PUBLIC);
        getOffset.setReturnType(longWrapper);
        getOffset.setName("getOffset");
        getOffset.addBodyLine("return offset;");
        topLevelClass.addMethod(getOffset);

        //页码
        Field pageNo = new Field();
        pageNo.setName("pageNo");
        pageNo.setVisibility(JavaVisibility.PRIVATE);
        pageNo.setType(longWrapper);
        pageNo.setInitializationString("0l");
        topLevelClass.addField(pageNo);

        Method setPageNo = new Method();
        setPageNo.setVisibility(JavaVisibility.PUBLIC);
        setPageNo.setName("setPageNo");
        setPageNo.addParameter(new Parameter(longWrapper, "pageNo"));
        setPageNo.addBodyLine("this.pageNo = pageNo;");
        setPageNo.addBodyLine("this.offset = pageNo * pageSize;");
        topLevelClass.addMethod(setPageNo);

        Method getPageNo = new Method();
        getPageNo.setVisibility(JavaVisibility.PUBLIC);
        getPageNo.setReturnType(longWrapper);
        getPageNo.setName("getPageNo");
        getPageNo.addBodyLine("return pageNo;");
        topLevelClass.addMethod(getPageNo);
        return true;
    }

    /**
     * 为Mapper.xml的selectByExample添加limit
     */
    @Override
    public boolean sqlMapSelectByExampleWithoutBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        addLimitAndOffset(element, introspectedTable);
        return true;
    }

    @Override
    public boolean sqlMapSelectByExampleWithBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        addLimitAndOffset(element, introspectedTable);
        return true;
    }

    /**
     * 添加分页
     *
     * @param element           XmlElement
     * @param introspectedTable IntrospectedTable
     */
    private void addLimitAndOffset(XmlElement element, IntrospectedTable introspectedTable) {
        XmlElement ifLimitNotNullElement = new XmlElement("if");
        ifLimitNotNullElement.addAttribute(new Attribute("test", "pageSize != null"));

        XmlElement ifOffsetNotNullElement = new XmlElement("if");
        ifOffsetNotNullElement.addAttribute(new Attribute("test", "offset != null"));
        ifOffsetNotNullElement.addElement(new TextElement("limit ${offset}, ${pageSize}"));
        ifLimitNotNullElement.addElement(ifOffsetNotNullElement);

        XmlElement ifOffsetNullElement = new XmlElement("if");
        ifOffsetNullElement.addAttribute(new Attribute("test", "offset == null"));
        ifOffsetNullElement.addElement(new TextElement("limit ${pageSize}"));
        ifLimitNotNullElement.addElement(ifOffsetNullElement);

        element.addElement(ifLimitNotNullElement);
    }
}
