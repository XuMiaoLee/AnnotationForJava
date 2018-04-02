package processor;

import annotation.DBTable;
import annotation.PrimaryKey;
import table.UserTable;
import utils.IOUtils;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import java.io.Writer;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 执行processor命令
 * javac -encoding UTF-8 -cp out/production/Annotation -processor processor.TableProcessor -d out/production/Annotation -s src/ src/*.java
 */
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes("annotation.*")
public class TableProcessor extends AbstractProcessor
{
    public static final String SUFFIX_NAME = "Helper";
    /*
    用于生成java文件
     */
    private Filer filer;
    private ProcessingEnvironment processingEnv;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv)
    {
        super.init(processingEnv);
        this.processingEnv = processingEnv;
        filer = processingEnv.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv)
    {
        System.out.println("==============PROCESSOR START===============");
        Writer w = null;
        //获取使用了@DBTable注解的类
        Set<? extends Element> tableElementsSet = roundEnv.getElementsAnnotatedWith(DBTable.class);
        for (Element element : tableElementsSet)
        {
            //Element有很多实现类，TypeElement是代表类上或者接口上的
            TypeElement typeElement = (TypeElement) element;
            DBTable table = typeElement.getAnnotation(DBTable.class);
            //声明主键的类型和字段名
            String primaryKeyType = null;
            String primaryKeyName = null;

            //获取类的所有成员
            List<? extends Element> allMembers = processingEnv.getElementUtils().getAllMembers(typeElement);
            for (Element member : allMembers)
            {
                //获取成员上是否使用了@PrimaryKey注解
                PrimaryKey primaryKeyAnnotation = member.getAnnotation(PrimaryKey.class);
                //获取变量上使用了PrimaryKey的注解
                if (primaryKeyAnnotation != null)
                {
                    primaryKeyType = member.asType().toString();
                    primaryKeyName = member.getSimpleName().toString();
                    break;
                }
            }

            //如果没有使用@PrimaryKey，抛异常
            if (primaryKeyName == null)
                throw new RuntimeException(typeElement.getQualifiedName() + " not defined @PrimaryKey");

            try
            {
                //拼接类名
                String className = typeElement.getSimpleName() + SUFFIX_NAME;
                //创建java源文件,当然也有提供创建class文件的，具体看api
                JavaFileObject a = filer.createSourceFile(typeElement.getQualifiedName() + SUFFIX_NAME);
                w = a.openWriter();
                w.write("//\n");
                w.write("// Source code recreated from a .class file by IntelliJ IDEA\n");
                w.write("// (powered by Fernflower decompiler)\n");
                w.write("//\n");
                //声明package
                w.write("package " + getPackageName(typeElement) + ";\n");
                //声明class name
                w.write("public class " + className + "{ \n");
                //写入构造
                w.write("\tpublic " + className + "() { \n\n");
                //在构造中生成建表语句
                w.write("\t\tSystem.out.println(\"create table " + table.tableName() + " "
                        + primaryKeyName + " " + primaryKeyType + " primary key auto_increment" + "\");\n");
                w.write("\t}\n");
                w.write("}");
            } catch (Exception e)
            {
                e.printStackTrace();
            } finally
            {
                IOUtils.close(w);
            }
        }
        System.out.println("==============PROCESSOR END===============");
        return true;
    }

    /**
     * 获取包名
     *
     * @param element
     * @return
     */
    private String getPackageName(Element element)
    {
        return processingEnv.getElementUtils().getPackageOf(element.getEnclosingElement()).toString();
    }

//    @Override
//    public Set<String> getSupportedAnnotationTypes()
//    {
//        Set<String> set = new HashSet<>();
//        set.add("annotation.*");
//        return set;
//    }
//
//    @Override
//    public SourceVersion getSupportedSourceVersion()
//    {
//        return SourceVersion.RELEASE_8;
//    }
}
