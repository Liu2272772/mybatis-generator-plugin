package org.shtl.mybatis.generator.impl;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

public class CustomAbstractXmlElementGenerator extends AbstractXmlElementGenerator {

    //方法名称
    private static final String FIND_ONE = "finOne";
    private static final String LIST = "list";
    private static final String BATCH_INSERT = "batchInsert";

    @Override
    public void addElements(XmlElement parentElement) {
        //添加查询条件
        addQueryCondition(parentElement);

        //添加findOne方法
        addFindOneOrListXml(parentElement, FIND_ONE);

        //添加list的xml
        addFindOneOrListXml(parentElement, LIST);

        //批量添加
        addBatchInsertXml(parentElement);
    }

    /**
     * 添加查询条件
     * @param parentElement
     */
    private void addQueryCondition(XmlElement parentElement) {
        // 增加base_query
        XmlElement sql = new XmlElement("sql");
        sql.addAttribute(new Attribute("id", "base_query"));

        //在这里添加where条件
        XmlElement selectTrimElement = new XmlElement("trim"); //设置trim标签
        selectTrimElement.addAttribute(new Attribute("prefix", "WHERE"));
        selectTrimElement.addAttribute(new Attribute("prefixOverrides", "AND | OR")); //添加where和and
        StringBuilder sb = new StringBuilder();
        for(IntrospectedColumn introspectedColumn : introspectedTable.getAllColumns()) {
            XmlElement selectNotNullElement = new XmlElement("if"); //$NON-NLS-1$
            sb.setLength(0);
            sb.append("null != ");
            sb.append(introspectedColumn.getJavaProperty());
            selectNotNullElement.addAttribute(new Attribute("test", sb.toString()));
            sb.setLength(0);
            // 添加and
            sb.append(" and ");
            // 添加别名t
            sb.append("t.");
            sb.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
            // 添加等号
            sb.append(" = ");
            sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn));
            selectNotNullElement.addElement(new TextElement(sb.toString()));
            selectTrimElement.addElement(selectNotNullElement);
        }
        sql.addElement(selectTrimElement);
        parentElement.addElement(sql);
    }

    /**
     * 添加findOne 或 list方法
     *
     * @param parentElement
     */
    private void addFindOneOrListXml(XmlElement parentElement, String id) {
        StringBuilder sb = new StringBuilder();
        // 公用select
        sb.setLength(0);
        sb.append("select ");
        sb.append("t.* ");
        sb.append("from ");
        sb.append(introspectedTable.getFullyQualifiedTableNameAtRuntime());
        sb.append(" t");
        TextElement selectText = new TextElement(sb.toString());

        // 公用include
        XmlElement include = new XmlElement("include");
        include.addAttribute(new Attribute("refid", "base_query"));

        // 增加find
        XmlElement find = new XmlElement("select");
        find.addAttribute(new Attribute("id", id));
        find.addAttribute(new Attribute("resultMap", "BaseResultMap"));
        if (introspectedTable.getRules().generateRecordWithBLOBsClass()) {
            find.addAttribute(new Attribute("parameterType", introspectedTable.getRecordWithBLOBsType()));
        } else {
            find.addAttribute(new Attribute("parameterType", introspectedTable.getBaseRecordType()));
        }

        find.addElement(selectText);
        find.addElement(include);
        parentElement.addElement(find);
    }

    /**
     * 添加批量添加
     *
     * @param parentElement XmlElement
     */
    private void addBatchInsertXml(XmlElement parentElement) {
        //插入xml
        XmlElement insertXml = new XmlElement("insert");
        insertXml.addAttribute(new Attribute("id", BATCH_INSERT));
        if (introspectedTable.getRules().generateRecordWithBLOBsClass()) {
            insertXml.addAttribute(new Attribute("parameterType", introspectedTable.getRecordWithBLOBsType()));
        } else {
            insertXml.addAttribute(new Attribute("parameterType", introspectedTable.getBaseRecordType()));
        }

        //sql列名
        StringBuilder sqlsb = new StringBuilder();
        for(IntrospectedColumn introspectedColumn : introspectedTable.getAllColumns()) {
            sqlsb.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
            sqlsb.append(",");
        }
        sqlsb.deleteCharAt(sqlsb.length() - 1);

        //值列
        StringBuilder valsb = new StringBuilder();
        valsb.append("(");
        for(IntrospectedColumn introspectedColumn : introspectedTable.getAllColumns()) {
            valsb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn,"item."));
            valsb.append(",");
        }
        valsb.deleteCharAt(valsb.length() - 1);
        valsb.append(")");

        //循环xml
        XmlElement forEachXml = new XmlElement("foreach");
        forEachXml.addAttribute(new Attribute("collection", "recordList"));
        forEachXml.addAttribute(new Attribute("item", "item"));
        forEachXml.addAttribute(new Attribute("separator", ","));
        forEachXml.addElement(new TextElement(valsb.toString()));

        StringBuilder insert = new StringBuilder();
        insert.append("insert into ");
        insert.append(introspectedTable.getFullyQualifiedTableNameAtRuntime());
        insert.append("(");
        insert.append(sqlsb.toString());
        insert.append(") values");

        insertXml.addElement(new TextElement(insert.toString()));
        insertXml.addElement(forEachXml);
        parentElement.addElement(insertXml);
    }
}
