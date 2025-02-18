package proto.exam.map.controller;

import org.springframework.web.bind.annotation.*;
import proto.exam.map.model.Grid;
import proto.exam.map.model.Hero;
import proto.exam.map.request.GridUpdateRequest;
import proto.exam.map.request.HeroMoveRequest;
import proto.exam.map.service.GameService;

import java.util.List;

@RestController
@RequestMapping("/game")
public class GameController {

    private final GameService gameService = new GameService();

    @GetMapping("/grid")
    public Grid getGrid() {
        return gameService.getGrid();
    }

    @PostMapping("/update")
    public void updateGrid(@RequestBody GridUpdateRequest updateRequest) {
        gameService.updateGrid(updateRequest);
    }

    @GetMapping("/heroes")
    public List<Hero> getHeroes() {
        return gameService.getHeroes();
    }

    @PostMapping("/moveHero")
    public void moveHero(@RequestBody HeroMoveRequest moveRequest) {
        gameService.moveHero(moveRequest);
    }
}
