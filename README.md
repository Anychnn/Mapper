# Mapper
支持注解的JavaBean映射工具，减少get和set的代码，提高代码整洁性

##Demo
定义一个Student类
```
public class Student {
    private String id;
    private String entityName;
    private List<Course> courseList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public List<Course> getCourseList() {
        return courseList;
    }

    public void setCourseList(List<Course> courseList) {
        this.courseList = courseList;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id='" + id + '\'' +
                ", entityName='" + entityName + '\'' +
                '}';
    }
}

```

定义一个需要转换的Vo类
```
@Source(source = Student.class)
public class StudentVo {

    private  String id;

    @Mapping(source = "entityName")
    private String name;

    private List<CourseVo> courseList = new ArrayList<>();


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CourseVo> getCourseList() {
        return courseList;
    }

    public void setCourseList(List<CourseVo> courseList) {
        this.courseList = courseList;
    }

    @Override
    public String toString() {
        return "StudentVo{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", courseList=" + courseList +
                '}';
    }
}

```
Student里有一个List<Course>
StudentVo有一个List<CourseVo> courses=new ArrayList();//这里需要指定容器类型
```
public class Course {
    private String courseName;
    private String courseId;

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }
}


@Source(source = Course.class)
public class CourseVo {
    private String courseName;
    private String courseId;


    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }



}
```###转换

```
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
```
