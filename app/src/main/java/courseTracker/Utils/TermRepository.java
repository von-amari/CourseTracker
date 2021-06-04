package courseTracker.Utils;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import courseTracker.DAO.TermDAO;
import courseTracker.Entity.TermEntity;

public class TermRepository {

    private TermDAO termDAO;
    private LiveData<List<TermEntity>> allTerms;

    public TermRepository(Application application) {
        TermRoomDatabase db = TermRoomDatabase.getDatabase(application);
        termDAO = db.termDAO();
        allTerms = termDAO.getAllTerms();
    }

    public LiveData<List<TermEntity>> getAllData() { return allTerms; }
    public void insert(TermEntity termEntity) {
        TermRoomDatabase.databaseWriteExecutor.execute(() -> termDAO.insert(termEntity));
    }
    public LiveData<TermEntity> get(int id) {
        return termDAO.get(id);
    }

    public void update(TermEntity termEntity) {
        TermRoomDatabase.databaseWriteExecutor.execute(() -> termDAO.update(termEntity));
    }
    public void delete(TermEntity termEntity) {
        TermRoomDatabase.databaseWriteExecutor.execute(() -> termDAO.delete(termEntity));
    }
}
