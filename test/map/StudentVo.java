package map;


import com.anyang.mapper.Mapping;
import com.anyang.mapper.Source;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/15.
 */
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
