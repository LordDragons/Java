package proto.exam.map.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/heroes")
public class HeroController {
    @GetMapping
    public Resource getHeroes() throws Exception {
        Path filePath = Paths.get("C:/Users/grado/java/maps/map jav/map/heroes.json").toAbsolutePath();
        return new UrlResource(filePath.toUri());
    }
}

