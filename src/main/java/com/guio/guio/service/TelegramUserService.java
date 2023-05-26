package com.guio.guio.service;

import com.guio.guio.entity.TelegramUser;
import com.guio.guio.repository.TelegramUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TelegramUserService {
    @Autowired
    private TelegramUserRepository userRepository;

    public TelegramUser addUser(TelegramUser todoUser) {
        return userRepository.save(todoUser);
    }

    public TelegramUser login (String token){
        return userRepository.findUserByTelegramToken(token);
    }
}
