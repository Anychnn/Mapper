package map;


import com.anyang.mapper.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/15.
 */
@Source(source = Student.class)
public class StudentVo {

    public enum StudentVoType{
        VoA,VoB,VoC
    }

    private  String id;

    @Mapping(source = "entityName")
    private String name;

    private List<CourseVo> courseList = new ArrayList<>();

    @Mapping(source = "studentType",converter = StudentTypeConverter.class)
    private StudentVoType studentVoType;

    @Mapping(converter = StudentCompountPropertyConverter.class)
    private String compundProperty;

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


    public StudentVoType getStudentVoType() {
        return studentVoType;
    }

    public void setStudentVoType(StudentVoType studentVoType) {
        this.studentVoType = studentVoType;
    }

    public String getCompundProperty() {
        return compundProperty;
    }

    public void setCompundProperty(String compundProperty) {
        this.compundProperty = compundProperty;
    }

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

    public static class StudentCompountPropertyConverter implements CompoundConverter<Student,String>{

        @Override
        public String convert(Student student) throws MappingException {
            return student.getId()+":"+student.getStudentType();
        }
    }
}
