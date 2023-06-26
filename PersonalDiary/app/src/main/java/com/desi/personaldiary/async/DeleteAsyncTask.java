package com.desi.personaldiary.async;

import android.os.AsyncTask;

import com.desi.personaldiary.databases.DiaryDao;
import com.desi.personaldiary.models.Diary;

public class DeleteAsyncTask extends AsyncTask<Diary, Void, Void> {

    private static final String TAG ="DeleteAsynctask";

    private DiaryDao mDiaryDao;

    public DeleteAsyncTask(DiaryDao dao){
        mDiaryDao = dao;
    }

    @Override
    protected Void doInBackground(Diary...diaries){
        mDiaryDao.delete(diaries);
        return null;
    }
}
