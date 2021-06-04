package courseTracker.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import courseTracker.Entity.CourseEntity;
import courseTracker.Entity.TermEntity;

@Dao
public interface CourseDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(CourseEntity courseEntity);

    @Query("DELETE FROM course_table")
    void deleteAll();

    @Query("SELECT * FROM course_table WHERE course_table.id == :id")
    LiveData<CourseEntity> get(int id);

    @Query("SELECT * FROM course_table WHERE course_table.term_id ==:termId")
    LiveData<List<CourseEntity>> getAssociatedCourses(int termId);

    @Update
    void update(CourseEntity courseEntity);

    @Delete
    void delete(CourseEntity CourseEntity);
}
