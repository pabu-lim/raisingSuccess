package com.pabu.raisingsuccess.models;

public class CharacterModel {
    private int characterId;
    private String characterName;
    private int characterLevel;
    private int characterExp;

    public int getCharacterId() {
        return characterId;
    }

    public void setCharacterId(int characterId) {
        this.characterId = characterId;
    }

    public String getCharacterName() {
        return characterName;
    }

    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }

    public int getCharacterLevel() {
        return characterLevel;
    }

    public void setCharacterLevel(int characterLevel) {
        this.characterLevel = characterLevel;
    }

    public int getCharacterExp() {
        return characterExp;
    }

    public void setCharacterExp(int characterExp) {
        this.characterExp = characterExp;
    }
}
