package courseTracker.Entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


@Entity(tableName = "course_table")
public class CourseEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "course_title")
    private String title;

    @ColumnInfo(name = "start_date")
    private String startDate;

    @ColumnInfo(name = "end_date")
    private String endDate;

    @ColumnInfo(name = "status")
    private String status;

    @ColumnInfo(name = "name")
    private String instructorName;

    @ColumnInfo(name = "phone")
    private String instructorPhone;

    @ColumnInfo(name = "email")
    private String instructorEmail;

    @ColumnInfo(name = "sRemind")
    private boolean startReminder;

    @ColumnInfo (name = "eRemind")
    private boolean endReminder;

    @ForeignKey(entity = TermEntity.class, parentColumns = "id", childColumns = "term_id")
    @ColumnInfo(name = "term_id")
    private int termId;


    @ColumnInfo
    private String notes;


    public CourseEntity() {
    }

    public CourseEntity(String title, String startDate, String endDate, String status, String instructorName, String instructorPhone, String instructorEmail, int termId, String notes, boolean sRemind, boolean eRemind){
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.instructorName = instructorName;
        this.instructorPhone = instructorPhone;
        this.instructorEmail = instructorEmail;
        this.termId = termId;
        this.notes = notes;
        this.endReminder = eRemind;
        this.startReminder = sRemind;

    }

public CourseEntity(int id, String notes) {
        this.id = id;
        this.notes = notes;
}

public CourseEntity(boolean startReminder, boolean endReminder){
        this.startReminder = startReminder;
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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public String getInstructorPhone() {
        return instructorPhone;
    }

    public void setInstructorPhone(String instructorPhone) {
        this.instructorPhone = instructorPhone;
    }

    public String getInstructorEmail() {
        return instructorEmail;
    }

    public void setInstructorEmail(String instructorEmail) {
        this.instructorEmail = instructorEmail;
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
    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
    public void setTermId(int termId) {
        this.termId = termId;
    }

    public int getTermId() {
        return termId;
    }

}
