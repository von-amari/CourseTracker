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
public interface TermDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(TermEntity termEntity);

    @Query("DELETE FROM term_table")
    void deleteAll();

    //ORDER BY start_date AS
    @Query("SELECT * FROM term_table C")
    LiveData<List<TermEntity>>getAllTerms();

    @Query("SELECT * FROM term_table WHERE term_table.id == :id")
    LiveData<TermEntity> get(int id);

    @Update
    void update(TermEntity termEntity);

    @Delete
    void delete(TermEntity termEntity);
}
