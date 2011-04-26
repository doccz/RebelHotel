package edu.unlv.cs.rebelhotel.web;

import edu.unlv.cs.rebelhotel.domain.Progress;
import org.springframework.roo.addon.web.mvc.controller.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebScaffold(path = "progresses", formBackingObject = Progress.class)
@RequestMapping("/progresses")
@Controller
public class ProgressController {
}
