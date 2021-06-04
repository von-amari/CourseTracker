package courseTracker.Utils;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import courseTracker.DAO.AssessmentDAO;
import courseTracker.DAO.CourseDAO;
import courseTracker.DAO.TermDAO;
import courseTracker.Entity.AssessmentEntity;
import courseTracker.Entity.CourseEntity;
import courseTracker.Entity.TermEntity;

@Database(entities = {TermEntity.class, CourseEntity.class, AssessmentEntity.class}, version = 1, exportSchema = false)

public abstract class TermRoomDatabase extends RoomDatabase {
    public abstract TermDAO termDAO();
    public abstract CourseDAO courseDAO();
    public abstract AssessmentDAO assessmentDAO();

    public static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor
            = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    private static volatile TermRoomDatabase INSTANCE;


    private static final RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback() {
                @Override
                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                    super.onCreate(db);

                    databaseWriteExecutor.execute(() -> {

                        TermDAO termDAO = INSTANCE.termDAO();
                        termDAO.deleteAll();

                        CourseDAO courseDAO = INSTANCE.courseDAO();
                        courseDAO.deleteAll();

                        AssessmentDAO assessmentDAO = INSTANCE.assessmentDAO();
                        assessmentDAO.deleteAll();


                        TermEntity newTerm = new TermEntity("Term 1", "5/1/2021", "10/31/2021");
                        termDAO.insert(newTerm);

                        newTerm = new TermEntity("Term 2", "11/1/2021", "4/30/2022");
                        termDAO.insert(newTerm);

                        newTerm = new TermEntity("Term 3", "5/1/2022", "10/31/2022");
                        termDAO.insert(newTerm);
                    });
                }
            };

    public static TermRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (TermRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TermRoomDatabase.class, "term_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

} // end termroomdb

