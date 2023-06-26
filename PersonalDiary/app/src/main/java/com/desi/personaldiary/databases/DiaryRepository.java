package com.desi.personaldiary.databases;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.desi.personaldiary.async.DeleteAsyncTask;
import com.desi.personaldiary.async.InsertAsyncTask;
import com.desi.personaldiary.models.Diary;

import java.util.List;

public class DiaryRepository {

    private DiaryDatabase diaryDatabase;

    public DiaryRepository(Context context){
        diaryDatabase = DiaryDatabase.getInstance(context);
    }

    public void insertDiaryTask(Diary diary){
        new InsertAsyncTask(diaryDatabase.getDiaryDao()).execute(diary);
    }

    public void UpdateDiary(Diary diary){
    }

    public LiveData<List<Diary>> retrieveDiaryTask(){
        return diaryDatabase.getDiaryDao().getDiaries();
    }

    public void deteleDiary(Diary diary){
        new DeleteAsyncTask(diaryDatabase.getDiaryDao()).execute(diary);
    }


}
