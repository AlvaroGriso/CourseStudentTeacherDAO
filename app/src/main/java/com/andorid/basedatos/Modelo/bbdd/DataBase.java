package com.andorid.basedatos.Modelo.bbdd;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.andorid.basedatos.Modelo.Course;
import com.andorid.basedatos.Modelo.Dao.CourseDao;
import com.andorid.basedatos.Modelo.Dao.StudentDao;
import com.andorid.basedatos.Modelo.Dao.TeacherDao;
import com.andorid.basedatos.Modelo.Student;
import com.andorid.basedatos.Modelo.Teacher;

@Database(entities = {Teacher.class,
        Student.class,
        Course.class}, version = 1, exportSchema = false)
public abstract class DataBase extends RoomDatabase {

    private static volatile DataBase INSTANCE;

    public abstract TeacherDao teacherDao();

    public abstract StudentDao studentDao();

    public abstract CourseDao courseDao();

    public static DataBase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (DataBase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            DataBase.class, Constants.NAME_DATA_BASE)
                            .addCallback(sRoomDatabaseCallback)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static Callback sRoomDatabaseCallback =
            new Callback() {
                @Override
                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                    super.onOpen(db);
                }
            };

}