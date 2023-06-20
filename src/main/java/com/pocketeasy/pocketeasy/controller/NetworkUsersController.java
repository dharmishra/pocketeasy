package com.pocketeasy.pocketeasy.controller;

import com.pocketeasy.pocketeasy.domain.NetworkCompany;
import com.pocketeasy.pocketeasy.model.NetworkUsersDTO;
import com.pocketeasy.pocketeasy.repos.NetworkCompanyRepository;
import com.pocketeasy.pocketeasy.service.NetworkUsersService;
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
@RequestMapping("/networkUserss")
public class NetworkUsersController {

    private final NetworkUsersService networkUsersService;
    private final NetworkCompanyRepository networkCompanyRepository;

    public NetworkUsersController(final NetworkUsersService networkUsersService,
            final NetworkCompanyRepository networkCompanyRepository) {
        this.networkUsersService = networkUsersService;
        this.networkCompanyRepository = networkCompanyRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("networkCompanyIdValues", networkCompanyRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(NetworkCompany::getId, NetworkCompany::getCompanyName)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("networkUserss", networkUsersService.findAll());
        return "networkUsers/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("networkUsers") final NetworkUsersDTO networkUsersDTO) {
        return "networkUsers/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("networkUsers") @Valid final NetworkUsersDTO networkUsersDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasFieldErrors("email") && networkUsersService.emailExists(networkUsersDTO.getEmail())) {
            bindingResult.rejectValue("email", "Exists.networkUsers.email");
        }
        if (!bindingResult.hasFieldErrors("password") && networkUsersService.passwordExists(networkUsersDTO.getPassword())) {
            bindingResult.rejectValue("password", "Exists.networkUsers.password");
        }
        if (bindingResult.hasErrors()) {
            return "networkUsers/add";
        }
        networkUsersService.create(networkUsersDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("networkUsers.create.success"));
        return "redirect:/networkUserss";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final UUID id, final Model model) {
        model.addAttribute("networkUsers", networkUsersService.get(id));
        return "networkUsers/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final UUID id,
            @ModelAttribute("networkUsers") @Valid final NetworkUsersDTO networkUsersDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        final NetworkUsersDTO currentNetworkUsersDTO = networkUsersService.get(id);
        if (!bindingResult.hasFieldErrors("email") &&
                !networkUsersDTO.getEmail().equalsIgnoreCase(currentNetworkUsersDTO.getEmail()) &&
                networkUsersService.emailExists(networkUsersDTO.getEmail())) {
            bindingResult.rejectValue("email", "Exists.networkUsers.email");
        }
        if (!bindingResult.hasFieldErrors("password") &&
                !networkUsersDTO.getPassword().equalsIgnoreCase(currentNetworkUsersDTO.getPassword()) &&
                networkUsersService.passwordExists(networkUsersDTO.getPassword())) {
            bindingResult.rejectValue("password", "Exists.networkUsers.password");
        }
        if (bindingResult.hasErrors()) {
            return "networkUsers/edit";
        }
        networkUsersService.update(id, networkUsersDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("networkUsers.update.success"));
        return "redirect:/networkUserss";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final UUID id, final RedirectAttributes redirectAttributes) {
        final String referencedWarning = networkUsersService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            networkUsersService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("networkUsers.delete.success"));
        }
        return "redirect:/networkUserss";
    }

}
