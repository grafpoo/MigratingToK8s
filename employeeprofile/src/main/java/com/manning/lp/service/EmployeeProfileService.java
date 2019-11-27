package com.manning.lp.service;

import com.manning.lp.data.EmployeeProfileRepository;
import com.manning.lp.domain.EmployeeProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmployeeProfileService {
    @Autowired
    private EmployeeProfileRepository repository;

    @Transactional
    public EmployeeProfile getProfile(Long id) throws ResourceNotFoundException {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @Transactional
    public void saveProfile(EmployeeProfile profile) {
        repository.save(profile);
    }

    @Transactional
    public void deleteProfile(Long id) {
        repository.deleteById(id);
    }
}

