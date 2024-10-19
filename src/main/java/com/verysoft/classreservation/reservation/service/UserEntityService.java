package com.verysoft.classreservation.reservation.service;

import com.verysoft.classreservation.reservation.dto.UserDto;
import com.verysoft.classreservation.reservation.entity.UserEntity;
import com.verysoft.classreservation.reservation.entity.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserEntityService implements UserDetailsService {

    private final UserEntityRepository userEntityRepository;

    public boolean validateUser(UserDto userDto, UserEntity userEntity) {
        return userEntity.getPassword().equals(userDto.getPassword());
    }

    public void createUser(UserDto userDto) {
        if (userExists(userDto.getUsername())) {
            throw new DuplicateKeyException(userDto.getUsername());
        }
        userEntityRepository.save(new UserEntity(userDto.getUsername(), userDto.getPassword(), userDto.getRoles()));
    }

    public boolean userExists(String username) {
        return userEntityRepository.existsById(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userEntityRepository.findById(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
