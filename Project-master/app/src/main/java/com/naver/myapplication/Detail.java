package com.naver.myapplication;



import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Detail extends Activity implements OnClickListener {
    MyDBHelper mDBHelper;
    int mId;
    String today;
    EditText editDate, editTitle, editTime, editEnd, editPlace, editMemo;
    private static final int DATABASE_VERSION = 1;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);

        editDate = (EditText) findViewById(R.id.editdate);
        editTitle = (EditText) findViewById(R.id.edittitle);
        editTime = (EditText) findViewById(R.id.edittime);
        editEnd = (EditText) findViewById(R.id.editend);
        editPlace = (EditText) findViewById(R.id.editplace);
        editMemo = (EditText) findViewById(R.id.editmemo);

        Intent intent = getIntent();
        mId = intent.getIntExtra("ParamID", -1);
        today = intent.getStringExtra("ParamDate");

        mDBHelper = new MyDBHelper(this, "Today.db", null, 1);

        if (mId == -1) {
            editDate.setText(today);
        } else {
            SQLiteDatabase db = mDBHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM today WHERE _id='" + mId
                    + "'", null);

            if (cursor.moveToNext()) {
                editTitle.setText(cursor.getString(1));
                editDate.setText(cursor.getString(2));
                editTime.setText(cursor.getString(3));
                editEnd.setText(cursor.getString(4));
                editPlace.setText(cursor.getString(5));
                editMemo.setText(cursor.getString(6));
            }
            mDBHelper.close();
        }

        Button btn1 = (Button) findViewById(R.id.btnsave);
        btn1.setOnClickListener(this);
        Button btn2 = (Button) findViewById(R.id.btndel);
        btn2.setOnClickListener(this);
        Button btn3 = (Button) findViewById(R.id.btncancel);
        btn3.setOnClickListener(this);

        if (mId == -1) {
            btn2.setVisibility(View.INVISIBLE);

        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        final SQLiteDatabase db = mDBHelper.getWritableDatabase();
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        switch (v.getId()) {
            case R.id.btnsave:
                if (mId != -1) {
                    db.execSQL("UPDATE today SET title='"
                            + editTitle.getText().toString() + "',date='"
                            + editDate.getText().toString() + "', time='"
                            + editTime.getText().toString() + "', memo='"
                            + editEnd.getText().toString() + "', end='"
                            + editPlace.getText().toString() + "', place='"
                            + editMemo.getText().toString() + "' WHERE _id='" + mId
                            + "';");
                } else {
                    db.execSQL("INSERT INTO today VALUES(null, '"
                            + editTitle.getText().toString() + "', '"
                            + editDate.getText().toString() + "', '"
                            + editTime.getText().toString() + "', '"
                            + editEnd.getText().toString() + "', '"
                            + editPlace.getText().toString() + "', '"
                            + editMemo.getText().toString() + "');");
                }
                mDBHelper.close();
                setResult(RESULT_OK);
                finish();
                break;
            case R.id.btndel:

                if (mId != -1) {

                    mDBHelper = new MyDBHelper(this, "Today.db", null, 1);
                    builder.setTitle("내용");
                    builder.setMessage("삭제하시겠습니까?");
                    builder.setPositiveButton("삭제", new AlertDialog.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            deleteView(db);

                        }
                    });
                    builder.setNegativeButton("취소",null);
                    builder.show();

                }

                setResult(RESULT_OK);
                break;

            case R.id.btncancel:
                setResult(RESULT_CANCELED);
                finish();
                break;
        }
        // finish();
    }

    public void deleteView(SQLiteDatabase db) {
        db.execSQL("DELETE FROM today WHERE _id='" + mId + "';");
        mDBHelper.close();
        finish();
    }
}

