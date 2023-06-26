package com.desi.personaldiary.databases;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.desi.personaldiary.models.Diary;

import java.util.List;

@Dao
public interface DiaryDao {
    @Insert
    long[] insertDiary(Diary... diaries);

    @Delete
    int delete(Diary... diaries);

    @Query("SELECT * FROM diaries")
    LiveData<List<Diary>> getDiaries();

    @Query("SELECT * FROM diaries WHERE title LIKE :title")
    List<Diary> getDiaryWithCustomQuery(String title);
}
