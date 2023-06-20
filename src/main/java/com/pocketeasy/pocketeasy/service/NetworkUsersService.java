package com.pocketeasy.pocketeasy.service;

import com.pocketeasy.pocketeasy.domain.NetworkCompany;
import com.pocketeasy.pocketeasy.domain.NetworkUsers;
import com.pocketeasy.pocketeasy.domain.Orders;
import com.pocketeasy.pocketeasy.model.NetworkUsersDTO;
import com.pocketeasy.pocketeasy.repos.NetworkCompanyRepository;
import com.pocketeasy.pocketeasy.repos.NetworkUsersRepository;
import com.pocketeasy.pocketeasy.repos.OrdersRepository;
import com.pocketeasy.pocketeasy.util.NotFoundException;
import com.pocketeasy.pocketeasy.util.WebUtils;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class NetworkUsersService {

    private final NetworkUsersRepository networkUsersRepository;
    private final NetworkCompanyRepository networkCompanyRepository;
    private final OrdersRepository ordersRepository;

    public NetworkUsersService(final NetworkUsersRepository networkUsersRepository,
            final NetworkCompanyRepository networkCompanyRepository,
            final OrdersRepository ordersRepository) {
        this.networkUsersRepository = networkUsersRepository;
        this.networkCompanyRepository = networkCompanyRepository;
        this.ordersRepository = ordersRepository;
    }

    public List<NetworkUsersDTO> findAll() {
        final List<NetworkUsers> networkUserss = networkUsersRepository.findAll(Sort.by("id"));
        return networkUserss.stream()
                .map(networkUsers -> mapToDTO(networkUsers, new NetworkUsersDTO()))
                .toList();
    }

    public NetworkUsersDTO get(final UUID id) {
        return networkUsersRepository.findById(id)
                .map(networkUsers -> mapToDTO(networkUsers, new NetworkUsersDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final NetworkUsersDTO networkUsersDTO) {
        final NetworkUsers networkUsers = new NetworkUsers();
        mapToEntity(networkUsersDTO, networkUsers);
        return networkUsersRepository.save(networkUsers).getId();
    }

    public void update(final UUID id, final NetworkUsersDTO networkUsersDTO) {
        final NetworkUsers networkUsers = networkUsersRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(networkUsersDTO, networkUsers);
        networkUsersRepository.save(networkUsers);
    }

    public void delete(final UUID id) {
        networkUsersRepository.deleteById(id);
    }

    private NetworkUsersDTO mapToDTO(final NetworkUsers networkUsers,
            final NetworkUsersDTO networkUsersDTO) {
        networkUsersDTO.setId(networkUsers.getId());
        networkUsersDTO.setEmail(networkUsers.getEmail());
        networkUsersDTO.setPassword(networkUsers.getPassword());
        networkUsersDTO.setActive(networkUsers.getActive());
        networkUsersDTO.setNetworkCompanyId(networkUsers.getNetworkCompanyId() == null ? null : networkUsers.getNetworkCompanyId().getId());
        return networkUsersDTO;
    }

    private NetworkUsers mapToEntity(final NetworkUsersDTO networkUsersDTO,
            final NetworkUsers networkUsers) {
        networkUsers.setEmail(networkUsersDTO.getEmail());
        networkUsers.setPassword(networkUsersDTO.getPassword());
        networkUsers.setActive(networkUsersDTO.getActive());
        final NetworkCompany networkCompanyId = networkUsersDTO.getNetworkCompanyId() == null ? null : networkCompanyRepository.findById(networkUsersDTO.getNetworkCompanyId())
                .orElseThrow(() -> new NotFoundException("networkCompanyId not found"));
        networkUsers.setNetworkCompanyId(networkCompanyId);
        return networkUsers;
    }

    public boolean emailExists(final String email) {
        return networkUsersRepository.existsByEmailIgnoreCase(email);
    }

    public boolean passwordExists(final String password) {
        return networkUsersRepository.existsByPasswordIgnoreCase(password);
    }

    public String getReferencedWarning(final UUID id) {
        final NetworkUsers networkUsers = networkUsersRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Orders networkUsersOrders = ordersRepository.findFirstByNetworkUsers(networkUsers);
        if (networkUsersOrders != null) {
            return WebUtils.getMessage("networkUsers.orders.networkUsers.referenced", networkUsersOrders.getId());
        }
        return null;
    }

}
