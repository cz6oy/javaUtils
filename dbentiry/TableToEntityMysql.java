package com.kayakwise.gray.api.util.dbentiry;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * mysql自动生成
 */
public class TableToEntityMysql{

    /** 数据库连接URL*/ 
    private final static String DB_URL = "jdbc:mysql://kayakcd.f3322.net:23306/vacctdb?useSSL=false&serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8";

    /** 数据库连接驱动*/ 
    private final static String DB_DRIVER = "com.mysql.jdbc.Driver"; 

    /** 数据库用户名*/ 
    private final static String DB_USERNAME = "vacct";

    /** 数据库密码*/ 
    private final static String DB_PASSWORD = "vacct..00";

    /** 生成java实体类存放目录*/
    private final static String FILEDIR = "D:\\make\\Api\\src\\main\\java\\com\\kayak\\api\\modal";
                                           

    /**
     * 生成bean前需修改的值
     */
    /**数据库名称**/
    public static String db_name="vacctdb";
    /**要生成bean的表名**/
    public static String table_name="user";
    /**生成的bean类package值**/
    public static String java_package_path="com.kayak.api.modal";



    public static void main(String[] args) {
        /*
         * 单个表生成bean
         */
//      exportJava(table_name,java_package_path);
//      /**
//       * 批量表生成bean
//       */
        String[] tableArr={
        		"b_cust_mapping"
        };
        exportBeansBatch(tableArr);
    }

    /**
     * <p class="detail">
     * 功能：数据库表批量生成bean
     * </p>
     * @param tableNameArr 数据库表名字符串数组
     */
    public static void exportBeansBatch(String tableNameArr[]){
        for(int i=0; tableNameArr!=null && i<tableNameArr.length; i++){
            try{
                exportJava(tableNameArr[i],java_package_path);
            }catch(Exception e){
                System.out.println("第"+i+"个Bean生成失败，表名："+tableNameArr[i]);
                e.printStackTrace();
            }
        }
    }

    /**
     * <p class="detail">
     * 功能：根据表名称单个生成bean实体类
     * </p>
     * @param tableName 数据库表名
     * @param packageName 生成的bean类顶部包路径(package后面部分)
     */
    public static void exportJava(String tableName,String packageName){
        System.out.println(tableName+"表对应的bean正在生成...");
        TableEntity entity=getTableEntity(tableName);
        String boName=entity.getBoName();
        List columnAttributes=entity.getColumnAttributes();
        StringBuffer buffer =  new StringBuffer();
        String filePath=FILEDIR+"//"+boName+".java";
        try {
            if(packageName==null || "".equals(packageName)){
                buffer.append("\n");
            }else{
                buffer.append("package "+packageName+";\n");
            }
            //buffer.append("import javax.persistence.Column;\n");
            //buffer.append("import static javax.persistence.GenerationType.IDENTITY;\n");
            //buffer.append("import javax.persistence.Entity;\n");
            //buffer.append("import javax.persistence.GeneratedValue;\n");
            //buffer.append("import javax.persistence.Id;\n");
            //buffer.append("import javax.persistence.Table;\n");
            //buffer.append("import java.util.Date;\n");

            buffer.append("/**\n*\n *  自动生成代码，表名：" + tableName + "\n*\n */\n\n");
            
            //buffer.append("\n@Entity\n");
            //buffer.append("@Table(name =\" "+tableName+"\")\n");
            buffer.append(" public class "+boName+" implements java.io.Serializable{\n");

            writeBOMessage(buffer,entity);
            writeSetGet(buffer,columnAttributes);

            buffer.append("}\n");
            File beanDir=new File(FILEDIR);
            if(!beanDir.isDirectory()){
                beanDir.mkdir();
            }
            File beanFile=new File(filePath);
            if(!beanFile.exists()){
                beanFile.createNewFile();
            }
            OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(filePath),"UTF-8");
            out.write(buffer.toString());
            out.flush();
            out.close();
            System.out.println(tableName+"表对应的bean生成成功："+filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    /**
     * 功能：获取数据库所有表名
     * @param tableName
     * @return
     * @author zhaoy
     * @date 2017年11月2日
     */
    public static TableEntity getTableEntity(String tableName){
        TableEntity entity=new TableEntity();
        List columnAttributes=new ArrayList();
        //当前用户有权限查看的所有表的表名
        PreparedStatement pstm=null;
        Connection con=null;
        ResultSetMetaData rsm=null; 
        String sql="SELECT * FROM "+tableName+" limit 0,1";

        try {
            con=getConnection();
            DatabaseMetaData dbMeta = con.getMetaData();
           
            //ResultSet pkRSet = dbMeta.getPrimaryKeys(null,null,tableName);
            //String primaryKey="";
            //while(pkRSet.next()){
            //    primaryKey=pkRSet.getString(4);
            //    break;
            //}
            //entity.setPrimaryKeyName(primaryKey);
            //entity.setPrimaryIdName(ColumnMapAttribute.getAttibutreNameFormColumn(primaryKey));
            pstm = con.prepareStatement(sql);
            pstm.executeQuery();
            rsm=pstm.getMetaData();
            for(int i=1;i<=rsm.getColumnCount();i++){
                ColumnAttribute columnAttribute=new ColumnAttribute();
                // 字段名
                String columnName=rsm.getColumnName(i);
                columnAttribute.setColumnName(columnName);
                //if(columnName.equals(primaryKey)){
                //    columnAttribute.setPK(true);
                //}else{
                    columnAttribute.setPK(false);
                //}
                // 字段类型
                String columnType=rsm.getColumnTypeName(i);
                if("CLOB".equals(columnType)){
                    continue;
                }
                columnAttribute.setColumType(columnType);
                // 字段长度
                int columnSize=rsm.getPrecision(i);
                columnAttribute.setLength(columnSize);
                // 获取指定列的小数点右边的位数。
                int scale=rsm.getScale(i);
                //System.out.println("字段名称： "+rsm.getColumnLabel(i)+"\n\t sql类型编号："+rsm.getColumnType(i)+"\n\t sql类型名称："+rsm.getColumnTypeName(i)+"\n\t scale:"+rsm.getScale(i)+"\n\t sql类型精确度："+rsm.getPrecision(i)+"\n\t 最大字符个数："+rsm.getColumnDisplaySize(i)+"\n\t java类型："+rsm.getColumnClassName(i));
                columnAttribute.setScale(scale);
                // 是否必填
                int isNullable=rsm.isNullable(i);
                columnAttribute.setNullable(isNullable);
                columnAttribute.setAttributeName(ColumnMapAttribute.getAttibutreNameFormColumn(columnName));
                columnAttribute.setAttributeType(ColumnMapAttribute.getAttributeType(columnType,columnSize,scale));
                // 字段描述
                String coments=getColumnComments(con,tableName,columnName);
                columnAttribute.setComment(coments);

                columnAttributes.add(columnAttribute);
            }
            entity.setBoName(ColumnMapAttribute.getBONameFormTable(tableName));
            entity.setColumnAttributes(columnAttributes);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally{
            closeConn(pstm,null,con);
        }
        return entity;
    }
    /**
     * 功能：获得字段说明
     * @param con
     * @param tableName
     * @param columnName
     * @return
     * @throws SQLException
     * @throws Exception 
     * @date 2017年11月2日
     */
    private static String getColumnComments(Connection con, String tableName, String columnName) throws SQLException {
        //String sql=" select t.TABLE_NAME,t.COLUMN_NAME,t.comments  from user_col_comments t where t.table_name=? and t.column_name=? ";
        String sql="SELECT t.`TABLE_NAME`,t.`COLUMN_NAME`,t.`COLUMN_COMMENT` FROM information_schema.columns t WHERE t.`TABLE_SCHEMA`='"+db_name+"' AND t.table_name='"+tableName+"' and t.`COLUMN_NAME`='"+columnName+"' ORDER BY t.table_schema,t.table_name";
        System.out.println(sql);
        PreparedStatement pstm=con.prepareStatement(sql);

        ResultSet rt = pstm.executeQuery();
        String colDesc="";
        if(rt.next()){
            colDesc=rt.getString(3);
        }
        return colDesc;
    }
    /**
     * 功能：创建文件夹并返回写入此文件的FileWriter对象
     * @param name
     * @return
     */
    private static FileWriter createFile(String name){
        File dir=new File(FILEDIR);
        if(!dir.exists()){
            dir.mkdirs();
        }
        String fileName=FILEDIR+"//"+name+".java";
        System.out.println(fileName);
        File file=new File(fileName);
        FileWriter writer=null;
        try {
            if(!file.exists()){
                file.createNewFile();
            }
            writer=new FileWriter(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return writer;
    }
    /**
     * 功能：写入字段定义及构造函数-默认无参数，全部参数2个
     * @param writer
     * @param entity
     * @throws IOException
     */
    private static void writeBOMessage(StringBuffer writer,TableEntity entity) throws IOException{
        String boName=entity.getBoName();
        String paimaryIdName=entity.getPrimaryIdName();
        String parimaryIdType="";
        List columnAttributes=entity.getColumnAttributes();
        // 所有成员变量的定义
        for(int i=0;columnAttributes!=null && i<columnAttributes.size();i++){
            writer.append("\n");
            ColumnAttribute columnAttribute=(ColumnAttribute)columnAttributes.get(i);
            if(paimaryIdName!=null && paimaryIdName.equals(columnAttribute.getAttributeName())){
                parimaryIdType=columnAttribute.getAttributeType();
            }
            writer.append("      /** "+columnAttribute.getComment()+" */\n");
            writer.append("      private "+columnAttribute.getAttributeType()+" "+columnAttribute.getAttributeName()+";\n");
        }
        writer.append("\n");
        // 无参构造方法
        writer.append("      public "+boName+" (){\n");
        writer.append("          super(); \n      }\n");
        // 全部参数构造方法
        // 主键构造方法
        if(paimaryIdName!=null && !paimaryIdName.equals("")){
            writer.append("      public "+boName+"("+parimaryIdType+" "+paimaryIdName+"){\n");
            writer.append("         this."+paimaryIdName+"="+paimaryIdName+";\n");
            writer.append("      }\n");

        }
    }
    /**
     * 功能：bo类set和get方法的生成，包括各个属性
     * @param writer
     * @param columnAttributes
     * @throws IOException
     */
    private static void writeSetGet(StringBuffer writer,List columnAttributes) throws IOException{
        for(int i=0;i<columnAttributes.size();i++){
            writer.append("\n");
            ColumnAttribute columnAttribute=(ColumnAttribute)columnAttributes.get(i);
            String attributeName=columnAttribute.getAttributeName();
            String attributeNameUP=ColumnMapAttribute.getFristUp(attributeName);
            String attributeType=columnAttribute.getAttributeType();
            //set方法
            if(((ColumnAttribute)columnAttributes.get(i)).isPK()){
              //  writer.append("\t   @Id\n");
              //  writer.append("\t   @GeneratedValue(strategy = IDENTITY)\n");
              //  writer.append("\t   @Column(name = \"id\", unique = true, nullable = false)\n");
            }else{
              //  writer.append("\t   @Column(name =\""+((ColumnAttribute)columnAttributes.get(i)).getColumnName()+"\")\n");
            }
            writer.append("       public "+attributeType+" get"+attributeNameUP+"(){\n");
            writer.append("              return this."+attributeName+";\n");
            writer.append("       }");
            writer.append("\n");
            //get方法
            writer.append("       public void set"+attributeNameUP+"("+attributeType+" "+attributeName+"){\n");
            writer.append("              this."+attributeName+"="+attributeName+";\n");
            writer.append("       }");
            writer.append("\n");
        }
    }
    /**
     * 功能：关闭数据库链接
     * @param stmt
     * @param rt
     * @param dbConn
     */
    public static void closeConn(Statement stmt,ResultSet rt,Connection dbConn){
        try {
            if(stmt!=null && stmt.isClosed()){
                stmt.close();
            }
            if(rt!=null && rt.isClosed()){
                rt.close();
            }
            if(dbConn!=null && dbConn.isClosed()){
                dbConn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * 功能：获得数据库链接
     * @return
     *
     */
    public static Connection getConnection(){
        Connection conn = null; 
        try{ 
            //注册驱动
            Class.forName(DB_DRIVER); 
            conn = DriverManager.getConnection(DB_URL,DB_USERNAME,DB_PASSWORD); 
        }catch(Exception ex){ 
            ex.printStackTrace(); 
        } 
        return conn; 
    }

}