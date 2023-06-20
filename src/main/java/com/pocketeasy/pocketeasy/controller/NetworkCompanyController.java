package com.pocketeasy.pocketeasy.controller;

import com.pocketeasy.pocketeasy.domain.Subscription;
import com.pocketeasy.pocketeasy.model.NetworkCompanyDTO;
import com.pocketeasy.pocketeasy.repos.SubscriptionRepository;
import com.pocketeasy.pocketeasy.service.NetworkCompanyService;
import com.pocketeasy.pocketeasy.util.CustomCollectors;
import com.pocketeasy.pocketeasy.util.WebUtils;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.data.domain.Sort;
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
@RequestMapping("/networkCompanys")
public class NetworkCompanyController {

    private final NetworkCompanyService networkCompanyService;
    private final SubscriptionRepository subscriptionRepository;

    public NetworkCompanyController(final NetworkCompanyService networkCompanyService,
            final SubscriptionRepository subscriptionRepository) {
        this.networkCompanyService = networkCompanyService;
        this.subscriptionRepository = subscriptionRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("subscriptionsValues", subscriptionRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Subscription::getId, Subscription::getSubscriptionName)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("networkCompanys", networkCompanyService.findAll());
        return "networkCompany/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("networkCompany") final NetworkCompanyDTO networkCompanyDTO) {
        return "networkCompany/add";
    }

    @PostMapping("/add")
    public String add(
            @ModelAttribute("networkCompany") @Valid final NetworkCompanyDTO networkCompanyDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasFieldErrors("companyName") && networkCompanyService.companyNameExists(networkCompanyDTO.getCompanyName())) {
            bindingResult.rejectValue("companyName", "Exists.networkCompany.companyName");
        }
        if (bindingResult.hasErrors()) {
            return "networkCompany/add";
        }
        networkCompanyService.create(networkCompanyDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("networkCompany.create.success"));
        return "redirect:/networkCompanys";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final UUID id, final Model model) {
        model.addAttribute("networkCompany", networkCompanyService.get(id));
        return "networkCompany/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final UUID id,
            @ModelAttribute("networkCompany") @Valid final NetworkCompanyDTO networkCompanyDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        final NetworkCompanyDTO currentNetworkCompanyDTO = networkCompanyService.get(id);
        if (!bindingResult.hasFieldErrors("companyName") &&
                !networkCompanyDTO.getCompanyName().equalsIgnoreCase(currentNetworkCompanyDTO.getCompanyName()) &&
                networkCompanyService.companyNameExists(networkCompanyDTO.getCompanyName())) {
            bindingResult.rejectValue("companyName", "Exists.networkCompany.companyName");
        }
        if (bindingResult.hasErrors()) {
            return "networkCompany/edit";
        }
        networkCompanyService.update(id, networkCompanyDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("networkCompany.update.success"));
        return "redirect:/networkCompanys";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final UUID id, final RedirectAttributes redirectAttributes) {
        final String referencedWarning = networkCompanyService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            networkCompanyService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("networkCompany.delete.success"));
        }
        return "redirect:/networkCompanys";
    }

}
