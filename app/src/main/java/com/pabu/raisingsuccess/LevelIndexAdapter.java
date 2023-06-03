package com.pabu.raisingsuccess;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class LevelIndexAdapter {
    private SQLiteDatabase db;
    private static final String LevelIndexTableName = "levelIdx_list";
    private static final String LevelColumnName = "levelIdx_level";
    private static final String ExpIndexColumnName = "levelIdx_exp";

    public LevelIndexAdapter(Context context) {
        RasingSuccessDB dbHelper = new RasingSuccessDB(context);
        db = dbHelper.getWritableDatabase();
    }

    // 이미 구현된 3. 한 번 만들고 난 후 수정사항 없이 character 상태이 변할 때 레벨업을 위해 경험치가 얼마나 필요한지 참조하기 위한 용도
    public int getLevelExpRequirement(int level) {
        String query = "SELECT " + ExpIndexColumnName + " FROM " + LevelIndexTableName +
                " WHERE " + LevelColumnName + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(level)});

        if (cursor != null && cursor.moveToFirst()) {
            int expColumnIndex = cursor.getColumnIndex(ExpIndexColumnName);

            if (expColumnIndex != -1) {
                int expRequirement = cursor.getInt(expColumnIndex);
                cursor.close();
                return expRequirement;
            } else {
                // 컬럼이름 못찾거나 틀림
                cursor.close();
                return -1;
            }
        } else {
            // 잘못된 결과
            if (cursor != null) {
                cursor.close();
            }
            return -1;
        }

    }
}