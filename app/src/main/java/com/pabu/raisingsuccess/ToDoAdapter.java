package com.pabu.raisingsuccess;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder>{

    private ArrayList<ToDoModel> todoList = new ArrayList<>();

    private RasingSuccessDB db;

    public ToDoAdapter(RasingSuccessDB db) {

        this.db = db;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.task_list, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ToDoModel item = todoList.get(position);

        holder.mCheckBox.setText(item.getTask());
        holder.mCheckBox.setChecked(toBoolean(item.getStatus()));

        // 체크 상태에 따라 취소선 표시
        if (item.getStatus() == 1) {
            holder.mCheckBox.setPaintFlags(holder.mCheckBox.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.mCheckBox.setPaintFlags(holder.mCheckBox.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }

        // 체크박스 체크 이벤트
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                // 체크 상태면 1, 아니면 0
                if (isChecked) {
                    db.updateStatus(item.getId(), 1);
                    holder.mCheckBox.setPaintFlags(holder.mCheckBox.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    db.updateStatus(item.getId(), 0);
                    holder.mCheckBox.setPaintFlags(holder.mCheckBox.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                }
            }
        });
    }
    
    //체크된 투두리스트 아이디 가져오기
    public ArrayList<Integer> getCheckedTasksIds() {
        ArrayList<Integer> checkedTaskIds = new ArrayList<>();
        for (ToDoModel task : todoList) {
            if (task.getStatus() == 1 && task.getCompletionDate() == null) {
                checkedTaskIds.add(task.getId());
            }
        }
        return checkedTaskIds;
    }


    /**
     * 체크상태 boolean으로 변경
     * @param n 상태값
     * @return
     */
    private boolean toBoolean(int n){

        return n != 0;
    }

    /**
     * 리스트에 데이터 담기
     * @param todoList 할일 리스트
     */
    public void setTasks(ArrayList<ToDoModel> todoList){

        this.todoList = todoList;
        notifyDataSetChanged();
    }

    /**
     * 할일 삭제
     * @param position 할일 위치
     */
    public void removeItem(int position){
        todoList.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * 할일 갯수
     * @return 갯수
     */
    @Override
    public int getItemCount() {
        return todoList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        CheckBox mCheckBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mCheckBox = itemView.findViewById(R.id.m_check_box);
        }
    }
}