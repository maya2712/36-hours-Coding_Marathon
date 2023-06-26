package com.desi.personaldiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.desi.personaldiary.databases.DiaryRepository;
import com.desi.personaldiary.models.Diary;

public class DiaryDetailsActivity extends AppCompatActivity implements
        View.OnTouchListener,
        GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener,
        View.OnClickListener,
        TextWatcher {

    private static final String TAG = "DiaryDetailsActivity";
    private static final int EDIT_MODE_ENABLED = 1;
    private static final int EDIT_MODE_DISABLED = 0;
    private EditText et_diary_content;
    private EditText et_toolbar_edit;
    private TextView text_toolbar_view;
    private RelativeLayout rl_back, rl_check;
    private ImageButton ib_check, ib_back;
    private TextView titleView;
    private EditText editView;
    private int diaryMode;
    private boolean isNewDiary;
    private GestureDetector gestureDetector;
    private DiaryRepository diaryRepository;
    private Diary finalDiary;


    private Diary diary;

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("mode", diaryMode);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        diaryMode = savedInstanceState.getInt("mode");
        if (diaryMode == EDIT_MODE_ENABLED) {
            enableEditMode();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_details);

        Toolbar toolbar = findViewById(R.id.detailToolbar);
        //setSupportActionBar(toolbar);

        diaryRepository = new DiaryRepository(this);

        et_diary_content = findViewById(R.id.contentDiary);
        et_toolbar_edit = findViewById(R.id.title_edit);
        text_toolbar_view = findViewById(R.id.title_view);

        rl_back = findViewById(R.id.back_button);
        rl_check = findViewById(R.id.check_button);
        ib_back = findViewById(R.id.toolbar_back_button);
        ib_check = findViewById(R.id.toolbar_check_button);
        titleView = findViewById(R.id.title_view);
        editView = findViewById(R.id.title_edit);



        if (getIncomingIntent()) {
            // Diary Lama (view mode)
            setDiaryProperties();
            disableEditMode();
        } else {
            // Diary baru (edit mode)
            setNewDiaryProperties();
            enableEditMode();
        }
        setListener();
    }

    private void hideVirtualKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void enableEditMode() {
        rl_back.setVisibility(View.GONE);
        rl_check.setVisibility(View.VISIBLE);

        titleView.setVisibility(View.GONE);
        editView.setVisibility(View.VISIBLE);

        diaryMode = EDIT_MODE_ENABLED;
        enableContentInteraction();
    }

    private void enableContentInteraction() {
        et_diary_content.setKeyListener(new EditText(this).getKeyListener());
        et_diary_content.setFocusable(true);
        et_diary_content.setFocusableInTouchMode(true);
        et_diary_content.setCursorVisible(true);
        et_diary_content.clearFocus();

    }

    private void disableEditMode() {
        rl_back.setVisibility(View.VISIBLE);
        rl_check.setVisibility(View.GONE);

        titleView.setVisibility(View.VISIBLE);
        editView.setVisibility(View.GONE);

        diaryMode = EDIT_MODE_DISABLED;
        disableContentInteraction();

        //bandingkan nilai diary baru dan lama
        String temp = et_diary_content.getText().toString();
        temp = temp.replace("\n", "");
        temp = temp.replace("", "");
        if(temp.length()>0) {
            finalDiary.setTitle(editView.getText().toString());
            finalDiary.setContent(et_diary_content.getText().toString());
            String timestap = "Juni 2023";
            finalDiary.setTimestamp(timestap);

            if (!finalDiary.getContent().equals(diary.getContent()) ||
                    !finalDiary.getTitle().equals(diary.getTitle())){
                saveChanges();
            }
        }

    }

    private void disableContentInteraction() {
        et_diary_content.setKeyListener(null);
        et_diary_content.setFocusable(false);
        et_diary_content.setFocusableInTouchMode(false);
        et_diary_content.setCursorVisible(false);
        et_diary_content.clearFocus();
    }

    private void setListener() {
        et_diary_content.setOnTouchListener(this);
        gestureDetector = new GestureDetector(this, this);
        gestureDetector.setOnDoubleTapListener(this);
        ib_check.setOnClickListener(this);
        titleView.setOnClickListener(this);
        ib_back.setOnClickListener(this);
        editView.addTextChangedListener(this);
    }

    private void setDiaryProperties() {
        et_toolbar_edit.setText(diary.getTitle());
        text_toolbar_view.setText(diary.getTitle());
        et_diary_content.setText(diary.getContent());
    }

    private void setNewDiaryProperties() {
        et_toolbar_edit.setText("New Diary");
        text_toolbar_view.setText("New Diary");

        diary = new Diary();
        finalDiary = new Diary();
        diary.setTimestamp("Diary Title");
        finalDiary.setTitle("Diary Title");
    }

    private void saveChanges() {
        String temp = et_diary_content.getText().toString();
        temp = temp.replace("\n", "");
        temp = temp.replace(" ", "");
        if (temp.length() > 0) {
            finalDiary.setTitle(editView.getText().toString());
            finalDiary.setContent(et_diary_content.getText().toString());
            String timestamp = "Juni 2023";
            finalDiary.setTimestamp(timestamp);

            if (!finalDiary.getContent().equals(diary.getContent()) ||
                    !finalDiary.getTitle().equals(diary.getTitle())) {
                if (isNewDiary) {
                    diaryRepository.insertDiaryTask(finalDiary);
                }
            }
        }
        disableEditMode();
    }

    private void saveNewDiary(){
        diaryRepository.insertDiaryTask(finalDiary);
    }


    private boolean getIncomingIntent() {
        if (getIntent().hasExtra("diary")) {
            diary = getIntent().getParcelableExtra("diary");
            finalDiary = getIntent().getParcelableExtra("diary");
            diaryMode = EDIT_MODE_DISABLED;
            isNewDiary = false;
            return true;
        }
        isNewDiary = true;
        return false;
    }

//    private boolean saveChanges() {
//        if (isNewDiary) {
//            String title = et_toolbar_edit.getText().toString();
//            String content = et_diary_content.getText().toString();
//
//            // Lakukan penyimpanan ke database atau penyimpanan data di sini
//
//            Toast.makeText(this, "Diary baru disimpan", Toast.LENGTH_SHORT).show();
//            return true;
//        } else {
//            // Perbarui diary yang ada dengan data yang diubah
//
//            String title = et_toolbar_edit.getText().toString();
//            String content = et_diary_content.getText().toString();
//
//            // Lakukan pembaruan data di sini
//
//            Toast.makeText(this, "Diary diperbarui", Toast.LENGTH_SHORT).show();
//            return true;
//        }
//    }

    @Override
    public void onBackPressed() {
        if (diaryMode == EDIT_MODE_ENABLED) {
            saveChanges();
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_view:
                enableEditMode();
                et_toolbar_edit.requestFocus();
                et_toolbar_edit.setSelection(et_toolbar_edit.length());
                break;
            case R.id.toolbar_check_button:
                disableEditMode();
                hideVirtualKeyboard();
                saveChanges();
                break;
            case R.id.toolbar_back_button:
                onBackPressed();
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        enableEditMode();
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        titleView.setText(charSequence.toString());

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
