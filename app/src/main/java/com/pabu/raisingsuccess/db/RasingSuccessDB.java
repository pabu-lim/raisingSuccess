package com.pabu.raisingsuccess.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.pabu.raisingsuccess.models.CharacterModel;
import com.pabu.raisingsuccess.models.GoalModel;
import com.pabu.raisingsuccess.models.ToDoModel;

import java.util.ArrayList;

public class RasingSuccessDB extends SQLiteOpenHelper {

    private SQLiteDatabase db;

    private Context context;
    public static final String DATABASE_NAME = "rasingSuccess.db";
    public static final int DATABASE_VERSION = 3;

    //todo 테이블
    public static final String todoTbl = "todo_list";
    public static final String todoId = "todo_id";
    public static final String todoTask = "todo_task";
    public static final String todoStatus = "todo_status";
    public static final String todoCompletionDate = "todo_completion_date";
    public static final String todoCompletionLevel = "todo_completion_level";

    //goal 테이블
    public static final String goalTbl = "goal_list";
    public static final String goalId = "goal_id";
    public static final String goalBig = "goal_big";
    public static final String goalSmall = "goal_small";
    public static final String goalStartDate = "goal_start_date";
    public static final String goalEndDate = "goal_end_date";

    //character 테이블
    public static final String characterTbl = "character_list";
    public static final String characterId = "character_id";
    public static final String characterName = "character_name";
    public static final String characterLevel = "character_Level";
    public static final String characterExp = "character_exp";

    //LevelIndex 테이블
    public static final String levelIdxTbl = "levelIdx_list";
    public static final String levelIdxId = "levelIdxid";
    public static final String levelIdxLevel = "levelIdx_level";
    public static final String levelIdxExp = "levelIdx_exp";

    public RasingSuccessDB(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // todo 테이블 추가
        String query = "CREATE TABLE " + todoTbl +
                " (" + todoId + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                todoTask + " TEXT, " +
                todoStatus + " INTEGER, " +
                todoCompletionDate + " TEXT, " +
                todoCompletionLevel + " INTEGER)";
        db.execSQL(query);

        // goal 테이블 추가
        query = "CREATE TABLE IF NOT EXISTS goal_list(goal_id INTEGER PRIMARY KEY AUTOINCREMENT, goal_big TEXT, goal_small TEXT, goal_start_date TEXT,goal_end_date TEXT)";
        db.execSQL(query);

        // levelIndex 테이블 추가
        query = "CREATE TABLE IF NOT EXISTS levelIdx_list(levelIdx_id INTEGER PRIMARY KEY, levelIdx_level INTEGER, levelIdx_exp INTEGER)";
        db.execSQL(query);

        //character 테이블 추가
        query = "CREATE TABLE IF NOT EXISTS character_list(character_id INTEGER PRIMARY KEY AUTOINCREMENT,character_name TEXT, character_level INTEGER, character_exp INTEGER)";
        db.execSQL(query);

        // Character테이블에 초기 데이터 입력
        String insertDefaultCharacterValues = "INSERT INTO " + characterTbl + " (" +
                characterName + ", " +
                characterLevel + ", " +
                characterExp + ") VALUES ('성공이', 1, 0);";
        db.execSQL(insertDefaultCharacterValues);

        // LevelIndex 테이블에 초기 데이터 입력
        int[] levelIndex = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};
        int[] expIndex = {20, 24, 28, 32, 44, 48, 52, 56, 76, 80, 84, 88, 124, 128, 132, 136};

        for (int i = 0; i < levelIndex.length; i++) {
            ContentValues initialValues = new ContentValues();
            initialValues.put(levelIdxLevel, levelIndex[i]);
            initialValues.put(levelIdxExp, expIndex[i]);
            db.insert(levelIdxTbl, null, initialValues);
        }
    }
    // 캐릭터 정보 가져오기
// 캐릭터 정보 가져오기
    public CharacterModel getCharacterModel() {
        Log.d("RasingSuccessDB", "getCharacter() is called");
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(characterTbl, null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            CharacterModel character = new CharacterModel();

            int columnIndex;

            columnIndex = cursor.getColumnIndex(characterId);
            if(columnIndex != -1) {
                character.setCharacterId(cursor.getInt(columnIndex));
            } else {
                Log.d("RasingSuccessDB", "getCharacter() is called OMMMMGGGGId");
                // Handle the case where the characterId column is not found
            }

            columnIndex = cursor.getColumnIndex(characterName);
            if(columnIndex != -1) {
                character.setCharacterName(cursor.getString(columnIndex));
            } else {
                Log.d("RasingSuccessDB", "getCharacter() is called OMMMMGGGGName");
                // Handle the case where the characterName column is not found
            }

            columnIndex = cursor.getColumnIndex(characterLevel);
            if(columnIndex != -1) {
                character.setCharacterLevel(cursor.getInt(columnIndex));
            } else {
                Log.d("RasingSuccessDB", "getCharacter() is called OMMMMGGGGLevel");
                // Handle the case where the characterLevel column is not found
            }

            columnIndex = cursor.getColumnIndex(characterExp);
            if(columnIndex != -1) {
                character.setCharacterExp(cursor.getInt(columnIndex));
            } else {
                Log.d("RasingSuccessDB", "getCharacter() is called OMMMMGGGGExp");
                // Handle the case where the characterExp column is not found
            }
            System.out.println(character);
            cursor.close();
           // db.close();
            return character;
        }

        return null;
    }

    // 캐릭터 정보 업데이트
    public int updateCharacterModel(CharacterModel character) {
        Log.d("RasingSuccessDB", "updateCharacterModel() is called");
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(characterName, character.getCharacterName());
        values.put(characterLevel, character.getCharacterLevel());
        values.put(characterExp, character.getCharacterExp());

        int rowCount = db.update(characterTbl, values, characterId + "=?",
                new String[] {String.valueOf(character.getCharacterId())});
      //  db.close();
        return rowCount;
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 기존 테이블 삭제
        db.execSQL("DROP TABLE IF EXISTS " + todoTbl);
        db.execSQL("DROP TABLE IF EXISTS " + goalTbl);
        db.execSQL("DROP TABLE IF EXISTS " + characterTbl);
        db.execSQL("DROP TABLE IF EXISTS " + levelIdxTbl);

        // 새로운 테이블 생성
        onCreate(db);
    }


    public void openDatabase(){

        db = this.getWritableDatabase();
    }

    /**
     * 할일 전체 가져오기
     * @return
     */
    public ArrayList<ToDoModel> getAllTasks(){
                        ArrayList<ToDoModel> taskList = new ArrayList<>();

                        Cursor cursor = null;
                        String query = "SELECT * FROM " + todoTbl + " WHERE " + todoCompletionDate + " IS NULL";
                        db = this.getReadableDatabase();

                        if (db != null) {
                            cursor = db.rawQuery(query, null);

                            if (cursor != null) {
                                int columnIndexId = cursor.getColumnIndex(todoId);
                                int columnIndexTask = cursor.getColumnIndex(todoTask);
                                int columnIndexStatus = cursor.getColumnIndex(todoStatus);

                                while (cursor.moveToNext()) {
                                    ToDoModel task = new ToDoModel();

                    task.setId(cursor.getInt(columnIndexId));
                    task.setTask(cursor.getString(columnIndexTask));
                    task.setStatus(cursor.getInt(columnIndexStatus));

                    taskList.add(task);
                }

                cursor.close();
            }
        }

        return taskList;
    }
//id로 task 가져오기
public ToDoModel getTask(int id) {
    ToDoModel task = null;
    SQLiteDatabase db = this.getReadableDatabase();

    String query = "SELECT * FROM " + todoTbl + " WHERE " + todoId + "=?";
    Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(id)});

    if (cursor.moveToFirst()) {
        task = new ToDoModel();

        int todoIdIndex = cursor.getColumnIndex(todoId);
        int todoTaskIndex = cursor.getColumnIndex(todoTask);
        int todoStatusIndex = cursor.getColumnIndex(todoStatus);
        int todoCompletionDateIndex = cursor.getColumnIndex(todoCompletionDate);
        int todoCompletionLevelIndex = cursor.getColumnIndex(todoCompletionLevel);

        if (todoIdIndex >= 0) {
            task.setId(cursor.getInt(todoIdIndex));
        }
        if (todoTaskIndex >= 0) {
            task.setTask(cursor.getString(todoTaskIndex));
        }
        if (todoStatusIndex >= 0) {
            task.setStatus(cursor.getInt(todoStatusIndex));
        }
        if (todoCompletionDateIndex >= 0) {
            task.setCompletionDate(cursor.getString(todoCompletionDateIndex));
        }
        if (todoCompletionLevelIndex >= 0) {
            task.setCompletionLevel(cursor.getInt(todoCompletionLevelIndex));
        }
    }

    cursor.close();
    return task;
}
public ArrayList<CharacterModel> getCharacterLive(){
    ArrayList<CharacterModel> characterModels = new ArrayList<>();

    Cursor cursor = null;
    String query = "SELECT * FROM " + characterTbl;
    db = this.getReadableDatabase();

    if (db != null) {
        cursor = db.rawQuery(query, null);

        if (cursor != null) {
            int columnIndexId = cursor.getColumnIndex(characterId);
            int columnIndexName = cursor.getColumnIndex(characterName);
            int columnIndexLevel = cursor.getColumnIndex(characterLevel);
            int columnIndexExp = cursor.getColumnIndex(characterExp);

            while (cursor.moveToNext()) {
                CharacterModel characterModel = new CharacterModel();

                characterModel.setCharacterId(cursor.getInt(columnIndexId));
                characterModel.setCharacterName(cursor.getString(columnIndexName));
                characterModel.setCharacterId(cursor.getInt(columnIndexLevel));
                characterModel.setCharacterId(cursor.getInt(columnIndexExp));
                characterModels.add(characterModel);
            }

            cursor.close();
        }
    }

    return characterModels;
}


    public ArrayList<GoalModel> getAllGoals() {
        ArrayList<GoalModel> goalList = new ArrayList<>();

        Cursor cursor = null;
        String query = "SELECT * FROM " + goalTbl;
        db = this.getReadableDatabase();

        if (db != null) {
            cursor = db.rawQuery(query, null);

            if (cursor != null) {
                int columnIndexId = cursor.getColumnIndex(goalId);
                int columnIndexGoal = cursor.getColumnIndex(goalBig);

                while (cursor.moveToNext()) {
                    GoalModel goal = new GoalModel();

                    goal.setGoalNumber(cursor.getInt(columnIndexId));
                    goal.setBigGoal(cursor.getString(columnIndexGoal));

                    goalList.add(goal);
                }

                cursor.close();
            }
        }

        return goalList;
    }




    /**
     * 할일 추가
     * @param task 할일
     */
    public void AddTask(ToDoModel task) {
        openDatabase();
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(todoTask, task.getTask());
        cv.put(todoStatus, 0);
        // completion_level 값을 설정합니다.

        db.insert(todoTbl, null, cv);
    }

    public void AddGoal(GoalModel goal) {
        openDatabase();
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(goalBig, goal.getBigGoal());
        cv.put(goalSmall, goal.getSmallGoal());
        cv.put(goalStartDate, goal.getGoalStartDate());
        cv.put(goalEndDate, goal.getGoalEndDate());

        db.insert(goalTbl, null, cv);
    }





    /**
     * 할일 상태 수정
     * @param id 아이디
     * @param status 상태
     */
    public void updateStatus(int id, int status){

        openDatabase();
        ContentValues cv = new ContentValues();
        cv.put(todoStatus, status);

        db.update(todoTbl, cv, "todo_id=?", new String[]{String.valueOf(id)});

    }

    /**
     * 할일 수정
     * @param id 아이디
     * @param task 할일
     */
    public void updateTask(int id, String task) {

        openDatabase();
        ContentValues cv = new ContentValues();
        cv.put(todoTask, task);
        db.update(todoTbl, cv, "todo_id=?", new String[]{String.valueOf(id)});

    }
    public void updateDayTask(int id, String completionDate, int completionLevel) {

        openDatabase();
        ContentValues cv = new ContentValues();
        cv.put(todoCompletionDate, completionDate);
        cv.put(todoCompletionLevel, completionLevel);
        cv.put(todoStatus, 1);
        db.update(todoTbl, cv, "todo_id=?", new String[]{String.valueOf(id)});

    }


    /**
     * 할일 삭제
     * @param id 아이디
     */
    public void deleteTask(int id){

        openDatabase();
        db.delete(todoTbl, "todo_id=?" , new String[]{String.valueOf(id)});
    }
}
