# Mapper
支持注解的JavaBean映射工具，减少get和set的代码，提高代码整洁性

##Demo
```java

import com.anyang.mapper.Mapper;
import com.anyang.mapper.MapperFactory;
import com.anyang.mapper.MappingException;
import map.Course;
import map.Student;
import map.StudentVo;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Anyang on 2017/3/2.
 */
public class MappingTest {
    //init
    //如果A 和 B 都是JavaBean
    //默认转换条件: 属性名相同  并且属性的类型也相同
    Mapper mapper= MapperFactory.createMapper();

    //简单属性复制  类型相同 有get set方法
    @Test
    public void simplePropertyTest() throws MappingException {
        Student student = new Student();
        student.setId("id");
        StudentVo dest = (StudentVo) mapper.map(student,StudentVo.class);
        Assert.assertEquals(student.getId(),dest.getId());
    }


    //Student中有List<Course>属性  转换到 StudentVo 里面的List<CourseVo> 取决条件如下:
    //需要在 CourseVo 类上添加该注解 : @Source(source = Course.class)
    @Test
    public void test2() throws Exception{
        Student student = new Student();
        student.setId("id");
        student.setEntityName("name");
        List<Course> courseList=new ArrayList<>();
        Course course=new Course();
        course.setCourseId("courseId1");
        course.setCourseName("courseName A");
        courseList.add(course);
        Course course2=new Course();
        course2.setCourseId("courseId2");
        course2.setCourseName("courseName B");
        courseList.add(course2);
        student.setCourseList(courseList);
        StudentVo dest = (StudentVo) mapper.map(student,StudentVo.class);

        Assert.assertEquals(dest.getCourseList().get(0).getCourseId(),course.getCourseId());
        Assert.assertEquals(dest.getCourseList().get(0).getCourseName(),course.getCourseName());
        Assert.assertEquals(dest.getCourseList().get(1).getCourseId(),course2.getCourseId());
        Assert.assertEquals(dest.getCourseList().get(1).getCourseName(),course2.getCourseName());
    }


    /**
     *  转换器测试
     *  对于需要进行处理的属性 需要用转换器进行转换 需要在对应的属性上用注解表示的转换器的类
     *  转换器有两种： SingleConverter： 一对一属性转换  和 CompoundConverter： 对象 属性转换  CompoundConverter 用法见下个测试方法
     *  @Mapping(source = "studentType",converter = StudentTypeConverter.class)
     *  private StudentVo.StudentVoType studentVoType;
     *  转换器代码：
       public static class StudentTypeConverter implements SingleConverter<Student.StudentType,StudentVoType>{

            @Override
            public StudentVoType convert(Student.StudentType studentType) throws MappingException {
                switch (studentType){
                    case A:
                        return StudentVoType.VoA;
                    case B:
                        return StudentVoType.VoB;
                    case C:
                        return StudentVoType.VoC;
                    default:
                        return null;
                }
            }
        }
     **/
    @Test
    public void converterTest() throws MappingException {
        Student student=new Student();
        student.setStudentType(Student.StudentType.A);
        StudentVo dest = (StudentVo) mapper.map(student,StudentVo.class);
        Assert.assertEquals(dest.getStudentVoType(), StudentVo.StudentVoType.VoA);
    }

    /**
     * 复杂属性转换器
     * @throws MappingException
     *
     * 在对应属性上写明用到的转换类  不需要source属性  因为数据来源为整个对象
     * @Mapping(converter = StudentCompountPropertyConverter.class)
     * private String compundProperty;
     *
     * 转换器代码：
     * public static class StudentCompountPropertyConverter implements CompoundConverter<Student,String>{

            @Override
            public String convert(Student student) throws MappingException {
                return student.getId()+":"+student.getStudentType();
            }
        }
     */
    @Test
    public void compoundConverterTest() throws MappingException {
        Student student=new Student();
        student.setId("studentId-1");
        student.setStudentType(Student.StudentType.A);
        StudentVo dest = (StudentVo) mapper.map(student,StudentVo.class);
        Assert.assertEquals(dest.getCompundProperty()+"", "studentId-1:A");
    }

}

```

