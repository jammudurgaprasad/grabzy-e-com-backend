package com.example.ecombackend.customer.service;

import com.example.ecombackend.customer.models.Address;
import com.example.ecombackend.customer.repository.AddressRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressService {

    private final AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public Address save(Address address) {
        return addressRepository.save(address);
    }

    public List<Address> findAll() {
        return addressRepository.findAll();
    }

    public Optional<Address> findById(Long id) {
        return addressRepository.findById(id);
    }

    public void deleteById(Long id) {
        addressRepository.deleteById(id);
    }

    public List<Address> findByUserId(Long userId) {
        return addressRepository.findByUserId(userId);
    }
}