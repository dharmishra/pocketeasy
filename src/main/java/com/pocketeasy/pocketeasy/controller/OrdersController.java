package com.pocketeasy.pocketeasy.controller;

import com.pocketeasy.pocketeasy.domain.NetworkUsers;
import com.pocketeasy.pocketeasy.model.OrdersDTO;
import com.pocketeasy.pocketeasy.model.Status;
import com.pocketeasy.pocketeasy.repos.NetworkUsersRepository;
import com.pocketeasy.pocketeasy.service.OrdersService;
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
@RequestMapping("/orderss")
public class OrdersController {

    private final OrdersService ordersService;
    private final NetworkUsersRepository networkUsersRepository;

    public OrdersController(final OrdersService ordersService,
            final NetworkUsersRepository networkUsersRepository) {
        this.ordersService = ordersService;
        this.networkUsersRepository = networkUsersRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("statusValues", Status.values());
        model.addAttribute("networkUsersValues", networkUsersRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(NetworkUsers::getId, NetworkUsers::getEmail)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("orderss", ordersService.findAll());
        return "orders/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("orders") final OrdersDTO ordersDTO) {
        return "orders/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("orders") @Valid final OrdersDTO ordersDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasFieldErrors("orderDate") && ordersService.orderDateExists(ordersDTO.getOrderDate())) {
            bindingResult.rejectValue("orderDate", "Exists.orders.orderDate");
        }
        if (!bindingResult.hasFieldErrors("orderItem") && ordersService.orderItemExists(ordersDTO.getOrderItem())) {
            bindingResult.rejectValue("orderItem", "Exists.orders.orderItem");
        }
        if (bindingResult.hasErrors()) {
            return "orders/add";
        }
        ordersService.create(ordersDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("orders.create.success"));
        return "redirect:/orderss";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final UUID id, final Model model) {
        model.addAttribute("orders", ordersService.get(id));
        return "orders/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final UUID id,
            @ModelAttribute("orders") @Valid final OrdersDTO ordersDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        final OrdersDTO currentOrdersDTO = ordersService.get(id);
        if (!bindingResult.hasFieldErrors("orderDate") &&
                !ordersDTO.getOrderDate().equals(currentOrdersDTO.getOrderDate()) &&
                ordersService.orderDateExists(ordersDTO.getOrderDate())) {
            bindingResult.rejectValue("orderDate", "Exists.orders.orderDate");
        }
        if (!bindingResult.hasFieldErrors("orderItem") &&
                !ordersDTO.getOrderItem().equalsIgnoreCase(currentOrdersDTO.getOrderItem()) &&
                ordersService.orderItemExists(ordersDTO.getOrderItem())) {
            bindingResult.rejectValue("orderItem", "Exists.orders.orderItem");
        }
        if (bindingResult.hasErrors()) {
            return "orders/edit";
        }
        ordersService.update(id, ordersDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("orders.update.success"));
        return "redirect:/orderss";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final UUID id, final RedirectAttributes redirectAttributes) {
        ordersService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("orders.delete.success"));
        return "redirect:/orderss";
    }

}
