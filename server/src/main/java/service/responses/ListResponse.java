package service.responses;

import model.GameData;

import java.util.ArrayList;

public record ListResponse(ArrayList<GameData> games) {
    @Override
    public ArrayList<GameData> games() {
        return games;
    }
}
