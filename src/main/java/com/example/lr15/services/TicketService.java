package com.example.lr15.services;

import com.example.lr15.specifications.OrganizationSpecifications;
import com.example.lr15.entities.SpisokTicket;
import com.example.lr15.repositories.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {
    private final TicketRepository repository;

    @Autowired
    public TicketService(TicketRepository repository) {
        this.repository = repository;
    }

    public SpisokTicket getById(Integer id) {
        SpisokTicket ticket = repository.findById(id).orElse(null);
        if(ticket == null) throw new UsernameNotFoundException("");
        return ticket;
    }

    public Page<SpisokTicket> getAllTickets(Pageable pageable) {
        return repository.findAll(pageable);
    }
    public Page<SpisokTicket> getAllTickets(String passenger, String route, Integer price, Pageable pageable) {
        Specification<SpisokTicket> specification = Specification
                .where(OrganizationSpecifications.hasName(passenger))
                .and(OrganizationSpecifications.hasRoutes(route))
                .and(OrganizationSpecifications.hasPrice(price));
        return repository.findAll(specification, pageable);
    }

    public void add(SpisokTicket spisokticket) {
        repository.save(spisokticket);
    }

    public void delete(SpisokTicket spisokTicket) {
        repository.delete(spisokTicket);
    }

    public void update(SpisokTicket exist, SpisokTicket updated) {
        if (!updated.getPassenger().isBlank()) exist.setPassenger(updated.getPassenger());
        if (!updated.getRoute().isBlank()) exist.setRoute(updated.getRoute());
        if (updated.getPrice() != null) exist.setPrice(updated.getPrice());
        repository.save(exist);
    }
    public List<SpisokTicket> getAllTickets() {
        return repository.findAll();
    }
}
