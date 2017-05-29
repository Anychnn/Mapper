package map;


import com.anyang.mapper.Source;

/**
 * Created by Administrator on 2017/3/15.
 */
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
