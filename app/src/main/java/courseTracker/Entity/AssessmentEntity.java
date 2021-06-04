package courseTracker.Entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


@Entity(tableName = "assessment_table")
public class AssessmentEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "start_date")
    private String startDate;

    @ColumnInfo(name = "end_date")
    private String endDate;

    @ColumnInfo(name = "type")
    private String type;

    @ColumnInfo(name = "sRemind")
    private boolean startReminder;



    @ColumnInfo (name = "eRemind")
    private boolean endReminder;

    @ForeignKey(entity = CourseEntity.class, parentColumns = "id", childColumns = "course_id")
    @ColumnInfo(name = "course_id")
    private int courseId;

    @Ignore
    public AssessmentEntity() {
    }

    public AssessmentEntity(@NonNull String title, String startDate, String endDate, @NonNull String type, int courseId, boolean startReminder, boolean endReminder ){
        this.title = title;
        this.endDate = endDate;
        this.startDate = startDate;
        this.type = type;
        this.courseId = courseId;
        this.startReminder = startReminder;
        this.endReminder = endReminder;
    }


    public boolean isStartReminder() {
        return startReminder;
    }

    public void setStartReminder(boolean startReminder) {
        this.startReminder = startReminder;
    }

    public boolean isEndReminder() {
        return endReminder;
    }

    public void setEndReminder(boolean endReminder) {
        this.endReminder = endReminder;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }


}
