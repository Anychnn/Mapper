
import com.anyang.mapper.Mapper;
import com.anyang.mapper.MapperFactory;
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

    @Test
    public void test() throws Exception{
        Mapper mapper= MapperFactory.createMapper();
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

}
