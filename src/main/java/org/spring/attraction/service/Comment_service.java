package org.spring.attraction.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.spring.attraction.repository.Comment_repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
@AllArgsConstructor
public class Comment_service {

    @Autowired
    public Comment_repository repository;
}
