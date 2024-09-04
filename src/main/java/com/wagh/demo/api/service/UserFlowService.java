package com.wagh.demo.api.service;

import com.wagh.demo.api.model.UserFlow;
import com.wagh.demo.api.repo.UserFlowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserFlowService {

    @Autowired
    private UserFlowRepository userFlowRepository;

    public UserFlow createUserFlow(UserFlow userFlow) {
        userFlow.setListItemsFromArray(userFlow.getListItemsAsArray());
        return userFlowRepository.save(userFlow);
    }

    public Optional<UserFlow> getUserFlowById(Long id) {
        return userFlowRepository.findById(id);
    }

    public List<UserFlow> getAllUserFlows() {
        return userFlowRepository.findAll();
    }

    public UserFlow updateUserFlow(Long id, UserFlow userFlow) {
        if (!userFlowRepository.existsById(id)) {
            return null; // Or throw an exception
        }
        userFlow.setId(id);
        userFlow.setListItemsFromArray(userFlow.getListItemsAsArray());
        return userFlowRepository.save(userFlow);
    }

    public void deleteUserFlow(Long id) {
        userFlowRepository.deleteById(id);
    }
}
