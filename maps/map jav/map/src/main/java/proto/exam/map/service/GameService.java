package proto.exam.map.service;

import proto.exam.map.model.Grid;
import proto.exam.map.model.Hero;
import proto.exam.map.request.GridUpdateRequest;
import proto.exam.map.request.HeroMoveRequest;

import java.util.List;
import java.util.ArrayList;

public class GameService {

    private final Grid grid = new Grid();
    private final List<Hero> heroes = new ArrayList<>();

    public Grid getGrid() {
        return grid;
    }

    public void updateGrid(GridUpdateRequest updateRequest) {
        grid.setRows(updateRequest.getRows());
        grid.setCols(updateRequest.getCols());
        grid.setSquareSize(updateRequest.getSquareSize());
        // Met à jour les obstacles et autres éléments selon la requête
    }

    public List<Hero> getHeroes() {
        return heroes;
    }

    public void moveHero(HeroMoveRequest moveRequest) {
        Hero hero = heroes.stream()
                .filter(h -> h.getId().equals(moveRequest.getHeroId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Hero not found"));

        hero.move(moveRequest.getTargetRow(), moveRequest.getTargetCol());
    }
}

