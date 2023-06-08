package com.pabu.raisingsuccess.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pabu.raisingsuccess.R;
import com.pabu.raisingsuccess.models.CharacterModel;

import java.util.ArrayList;
import java.util.List;

public class CharacterAdapter extends RecyclerView.Adapter<CharacterAdapter.ViewHolder> {

    private List<CharacterModel> characterList;
    private LevelIndexAdapter levelIndexAdapter;
    private Context context;

    public CharacterAdapter(ArrayList<CharacterModel> characterList, Context context) {
        this.characterList = characterList;
        this.context = context;
        this.levelIndexAdapter = new LevelIndexAdapter(context);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_schedule, parent, false);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        return new ViewHolder(recyclerView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CharacterModel character = characterList.get(position);

        // 캐릭터 정보를 뷰에 설정
        holder.characterName.setText(character.getCharacterName());
        holder.characterLevel.setText("레벨: " + character.getCharacterLevel());
        int requiredExp = levelIndexAdapter.getLevelExpRequirement(character.getCharacterLevel());
        holder.characterExp.setText("경험치: " + character.getCharacterExp() + " / " + requiredExp);
    }


    @Override
    public int getItemCount() {
        return characterList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView characterName, characterLevel, characterExp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            characterName = itemView.findViewById(R.id.character_name);
            characterLevel = itemView.findViewById(R.id.character_level);
            characterExp = itemView.findViewById(R.id.character_exp);
        }
    }
}
