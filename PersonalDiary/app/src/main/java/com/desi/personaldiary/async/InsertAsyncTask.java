package com.desi.personaldiary.async;

import android.os.AsyncTask;

import androidx.loader.content.AsyncTaskLoader;

import com.desi.personaldiary.databases.DiaryDao;
import com.desi.personaldiary.models.Diary;

public class InsertAsyncTask extends AsyncTask<Diary, Void, Void> {

    private DiaryDao mDiaryDao;

    public InsertAsyncTask(DiaryDao diaryDao){
        mDiaryDao = diaryDao;
    }

    @Override
    protected Void doInBackground(Diary... diaries) {
        mDiaryDao.insertDiary(diaries);
        return null;
    }
}
