package org.springframework.samples.petclinic.feeding;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/feeding")
public class FeedingController {

  private FeedingService feedingService;
  public static final String CREATE_FEEDING = "feedings/createOrUpdateFeedingForm";

  @Autowired
  public FeedingController(FeedingService feedingService) {
    this.feedingService = feedingService;
  }

  @GetMapping("/create")
  public String addFeeding(ModelMap model) {
    model.put("feeding", new Feeding());
    return CREATE_FEEDING;
  }
  
  @PostMapping("/create")
  public String saveFeeding(@Valid Feeding feeding, BindingResult bindingResult, ModelMap model) throws UnfeasibleFeedingException {
    if (bindingResult.hasErrors()) {
      model.put("message", bindingResult.getFieldErrors());
      return CREATE_FEEDING;
    } else {
      Feeding newFeeding = new Feeding();
      BeanUtils.copyProperties(feeding, newFeeding);
      try {
        Feeding createdFeeding = feedingService.save(newFeeding);
        model.put("message", "Feeding " + createdFeeding.getId() + " succesfully created");
        return "redirect:/welcome";
      } catch (UnfeasibleFeedingException e) {
        bindingResult.rejectValue("feedingType", "Cannot associate that pet to the type of feeding you wanted to");
        throw new UnfeasibleFeedingException();
      }
    }
  }

}
