package proto.exam.map.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import proto.exam.map.model.Hero;
import proto.exam.map.model.Map;

import java.util.Arrays;
import java.util.List;

@Controller
public class MapController {
    @GetMapping("/")
    public String map() {
        return "map"; // correspond à "map.html"
    }

}
