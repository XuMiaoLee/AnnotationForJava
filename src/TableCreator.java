import annotation.DBTable;
import annotation.PrimaryKey;

public class TableCreator
{

    public void creator(Class<?> clazz) throws ClassNotFoundException
    {
        //获取被注解类的class对象
        Class<?> tableClazz = Class.forName(clazz.getName());
        //获取该类中使用的注解
        DBTable table = tableClazz.getAnnotation(DBTable.class);
        String tableName = table.tableName();
        System.out.println(tableName);
    }

    public void creator2(Class<?> clazz) throws Exception
    {
        String className = clazz.getName() + "Helper";
        Class.forName(className).getConstructor().newInstance();
    }

}
