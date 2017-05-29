package map;

import java.util.List;

/**
 * Created by Administrator on 2017/3/15.
 */
public class Student {
    public enum StudentType{
        A,B,C
    }
    private String id;
    private String entityName;
    private List<Course> courseList;
    private StudentType studentType;

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

    public StudentType getStudentType() {
        return studentType;
    }

    public void setStudentType(StudentType studentType) {
        this.studentType = studentType;
    }

}
