package com.pabu.raisingsuccess.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pabu.raisingsuccess.adapters.CharacterAdapter;
import com.pabu.raisingsuccess.R;
import com.pabu.raisingsuccess.adapters.ToDoAdapter;
import com.pabu.raisingsuccess.activitiies.GoalActivity;
import com.pabu.raisingsuccess.db.RasingSuccessDB;
import com.pabu.raisingsuccess.models.CharacterModel;
import com.pabu.raisingsuccess.models.ToDoModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class ScheduleFragment extends Fragment {

    private ArrayList<ToDoModel> taskList;
    private ToDoAdapter adapter;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private FloatingActionButton fab_end;
    private  FloatingActionButton fab_goal;
    private EditText todoText;
    private Button addBtn;

    private ArrayList<CharacterModel> characterList;
    private CharacterAdapter adapter_character;



    //DB
    private RasingSuccessDB db;

    //입력 레이아웃
    private LinearLayout bottomLayout;

    //할일 ID
    private int gId = 0;

    public ScheduleFragment() {
        // Required empty public constructor
    }
    private Context context;
    // Rest of the code...

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_schedule, container, false);

        //DB 연결
        db = new RasingSuccessDB(context);

        //초기화
        taskList = new ArrayList<>();
        characterList = new ArrayList<>();
        bottomLayout = rootView.findViewById(R.id.bottom_section);
        todoText = rootView.findViewById(R.id.todo_text);
        addBtn = rootView.findViewById(R.id.add_btn);
        fab_end = rootView.findViewById(R.id.fab_end);
        fab = rootView.findViewById(R.id.fab);
        fab_goal = rootView.findViewById(R.id.fab_goal);

        //recyclerView 설정
        recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //adapter 설정
        adapter = new ToDoAdapter(db);
        adapter.setTasks(taskList);
        //adapter 적용
        recyclerView.setAdapter(adapter);

        //조회
        selectData();

        //목표등록버튼
        fab_goal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), GoalActivity.class);
                startActivity(intent);
            }
        });

        //등록모드
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewMode("할일등록");
            }
        });

        //하루끝 버튼
        fab_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // completionDate가 null이고 체크된 할 일 목록의 ID들을 얻습니다.
                ArrayList<Integer> checkedTaskIds = adapter.getCheckedTasksIds();

                // 현재 캐릭터 레벨값 가져오기
                CharacterModel character = db.getCharacterModel();
                int currentCharacterLevel = character.getCharacterLevel();

                // 현재시간 얻기
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentTime = sdf.format(new Date());

                // 체크된 할 일들의 completion_date를 현재시간으로 설정하고, completion_level을 현재 캐릭터 레벨로 설정합니다.
                for (Integer taskId : checkedTaskIds) {
                    ToDoModel task = db.getTask(taskId);
                    task.setCompletionDate(currentTime);
                    task.setCompletionLevel(currentCharacterLevel);
                    db.updateDayTask(taskId, currentTime, currentCharacterLevel);
                }

                // UI 갱신
                selectData();
            }
        });



        //추가 버튼
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewMode("FAB");

                //입력 데이터
                String text = todoText.getText().toString();

                //할일등록이면 등록 아니면 수정
                if (addBtn.getText().toString().equals("할일등록")) {
                    //데이터 담기
                    ToDoModel task = new ToDoModel();
                    task.setTask(text);
                    task.setStatus(0);

                    //할일 추가
                    db.AddTask(task);

                    //조회 및 리셋
                    selectReset("할일등록");
                } else { //수정일때
                    //할일 수정
                    db.updateTask(gId, text);

                    //조회 및 리셋
                    selectReset("수정");
                }

                //키보드 내리기
                hideKeyboard(todoText);
            }
        });

        //할일 입력체크
        todoText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().equals("")) {
                    //버튼 비활성화
                    addBtn.setEnabled(false);

                    //버튼 텍스트색 : 회색
                    addBtn.setTextColor(Color.GRAY);
                } else {
                    //버튼 활성화
                    addBtn.setEnabled(true);

                    //버튼 텍스트색 : 검정색
                    addBtn.setTextColor(Color.BLACK);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        //스와이프 기능(수정, 삭제)
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                switch (direction) {
                    //삭제 기능
                    case ItemTouchHelper.LEFT:
                        //할일 ID 변수에 담기
                        int id = taskList.get(position).getId();

                        //아이템 삭제
                        adapter.removeItem(position);

                        //DB에서 삭제
                        db.deleteTask(id);

                        break;
                    //수정 기능
                    case ItemTouchHelper.RIGHT:
                        viewMode("수정");

                        String task = taskList.get(position).getTask();
                        gId = taskList.get(position).getId();

                        //입력창에 수정할 할일 넣기
                        todoText.setText(task);

                        addBtn.setText("수정");

                        break;
                }
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder,
                        dX, dY, actionState, isCurrentlyActive)
                        .addSwipeLeftBackgroundColor(Color.RED)      //왼쪽 스와이프 배경색 설정
                        .addSwipeLeftActionIcon(R.drawable.ic_delete)//왼쪽 스와이프 아이콘 설정
                        .addSwipeLeftLabel("삭제")                    //왼쪽 스와이프 라벨 설정
                        .setSwipeLeftLabelColor(Color.WHITE)         //왼쪽 스와이프 라벨 색상
                        .addSwipeRightBackgroundColor(Color.BLUE)    //오른쪽 스와이프 배경색 설정
                        .addSwipeRightActionIcon(R.drawable.ic_edit) //오른쪽 스와이프 아이콘 설정
                        .addSwipeRightLabel("수정")                   //오른쪽 스와이프 라벨 설정
                        .setSwipeRightLabelColor(Color.WHITE)        //오른쪽 스와이프 라벨 색상
                        .create()
                        .decorate();

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }).attachToRecyclerView(recyclerView);

        return rootView;
    }

    //데이터 조회
    public void selectData() {
        // 전체 할일 가져오기
        taskList = db.getAllTasks();

        // 정렬
        Collections.reverse(taskList);

        // 담기
        adapter.setTasks(taskList);    // 목록 체인지
        adapter.notifyDataSetChanged();

        // 캐릭터 레벨 및 경험치 데이터 갱신
        updateCharacterData();
    }

    private void updateCharacterData() {
        // 데이터베이스로부터 캐릭터 정보 가져오기
        CharacterModel character = db.getCharacterModel();

    }
    //조회 및 리셋
    public void selectReset(String type) {
        //조회
        selectData();

        //초기화
        todoText.setText("");

        //등록이 아니면 등록으로 변경
        if (!type.equals("할일등록")) {
            addBtn.setText("할일등록");
        }
    }

    //화면 상태
    public void viewMode(String type) {
        //입력하고 나면 입력창 사라지고 FAB 보여줌
        if (type.equals("FAB")) {
            //입력창 숨김
            bottomLayout.setVisibility(View.GONE);

            //FAB 보여줌
            fab.setVisibility(View.VISIBLE);

        } else { //FAB 누르면 입력창 보여주고 FAB 사라짐
            //입력창 보여줌
            bottomLayout.setVisibility(View.VISIBLE);

            //FAB 숨김
            fab.setVisibility(View.INVISIBLE);
        }
    }

    //키보드 숨김
    private void hideKeyboard(EditText editText) {
        InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        //키보드 숨김
        manager.hideSoftInputFromWindow(editText.getApplicationWindowToken(), 0);
    }

}



