package edu.unlv.cs.rebelhotel.web;

import edu.unlv.cs.rebelhotel.domain.RandomValidationBatch;
import org.springframework.roo.addon.web.mvc.controller.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebScaffold(path = "randomvalidationbatches", formBackingObject = RandomValidationBatch.class)
@RequestMapping("/randomvalidationbatches")
@Controller
public class RandomValidationBatchController {
}
