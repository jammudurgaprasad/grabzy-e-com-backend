package com.example.ecombackend.deliveryagent.service;

import com.example.ecombackend.deliveryagent.models.DeliveryAgent;
import com.example.ecombackend.deliveryagent.repository.DeliveryAgentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeliveryAgentService {

    private final DeliveryAgentRepository deliveryAgentRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public DeliveryAgentService(DeliveryAgentRepository deliveryAgentRepository) {
        this.deliveryAgentRepository = deliveryAgentRepository;
    }


    public DeliveryAgent registerAgent(DeliveryAgent agent) {
        Optional<DeliveryAgent> existing = deliveryAgentRepository.findByGmail(agent.getGmail());
        if (existing.isPresent()) {
            throw new RuntimeException("Agent with this email already exists.");
        }
        agent.setPassword(passwordEncoder.encode(agent.getPassword()));
        return deliveryAgentRepository.save(agent);
    }



    public DeliveryAgent loginAgent(DeliveryAgent credentials) {
        Optional<DeliveryAgent> optional = deliveryAgentRepository.findByGmail(credentials.getGmail());
        if (optional.isPresent()) {
            DeliveryAgent agent = optional.get();
            if (passwordEncoder.matches(credentials.getPassword(), agent.getPassword())) {
                return agent;
            }
        }
        throw new RuntimeException("Invalid email or password");
    }
    public List<DeliveryAgent> getAllAgents() {
        return deliveryAgentRepository.findAll();
    }

    public Optional<DeliveryAgent> getAgentById(Long id) {
        return deliveryAgentRepository.findById(id);
    }

    public DeliveryAgent approveAgent(Long id) {
        Optional<DeliveryAgent> optional = deliveryAgentRepository.findById(id);
        if (optional.isPresent()) {
            DeliveryAgent agent = optional.get();
            agent.setApproved(true);
            return deliveryAgentRepository.save(agent);
        }
        return null;
    }

    public void deleteAgent(Long id) {
        deliveryAgentRepository.deleteById(id);
    }

    public DeliveryAgent unapproveAgent(Long id) {
        Optional<DeliveryAgent> optional = deliveryAgentRepository.findById(id);
        if (optional.isPresent()) {
            DeliveryAgent agent = optional.get();
            agent.setApproved(false);  // just set approved to false
            return deliveryAgentRepository.save(agent);
        }
        return null;
    }

}
