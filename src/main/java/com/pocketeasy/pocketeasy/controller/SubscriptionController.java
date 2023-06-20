package com.pocketeasy.pocketeasy.controller;

import com.pocketeasy.pocketeasy.model.SubscriptionDTO;
import com.pocketeasy.pocketeasy.service.SubscriptionService;
import com.pocketeasy.pocketeasy.util.WebUtils;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    public SubscriptionController(final SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("subscriptions", subscriptionService.findAll());
        return "subscription/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("subscription") final SubscriptionDTO subscriptionDTO) {
        return "subscription/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("subscription") @Valid final SubscriptionDTO subscriptionDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "subscription/add";
        }
        subscriptionService.create(subscriptionDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("subscription.create.success"));
        return "redirect:/subscriptions";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final UUID id, final Model model) {
        model.addAttribute("subscription", subscriptionService.get(id));
        return "subscription/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final UUID id,
            @ModelAttribute("subscription") @Valid final SubscriptionDTO subscriptionDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "subscription/edit";
        }
        subscriptionService.update(id, subscriptionDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("subscription.update.success"));
        return "redirect:/subscriptions";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final UUID id, final RedirectAttributes redirectAttributes) {
        final String referencedWarning = subscriptionService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            subscriptionService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("subscription.delete.success"));
        }
        return "redirect:/subscriptions";
    }

}
