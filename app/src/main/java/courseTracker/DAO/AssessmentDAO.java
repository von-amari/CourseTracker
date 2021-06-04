package courseTracker.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import courseTracker.Entity.AssessmentEntity;
import courseTracker.Entity.CourseEntity;

@Dao
public interface AssessmentDAO {
    //takes care of CRUD operations
    //methods wont have a body

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(AssessmentEntity assessmentEntity);

    @Query("DELETE FROM assessment_table")
    void deleteAll();

    @Query("SELECT * FROM assessment_table WHERE assessment_table.id == :id")
    LiveData<AssessmentEntity> get(int id);

    @Query("SELECT * FROM assessment_table WHERE assessment_table.course_id ==:courseId")
   LiveData<List<AssessmentEntity>> getAssociatedAssessments(int courseId);

    @Update
    void update(AssessmentEntity assessmentEntity);

    @Delete
    void delete(AssessmentEntity assessmentEntity);

}
