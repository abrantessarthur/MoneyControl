package br.com.abrantes.MoneyControl.service;

import br.com.abrantes.MoneyControl.entity.UserEntity;
import br.com.abrantes.MoneyControl.repository.UserRepository;
import br.com.abrantes.MoneyControl.repository.UsersProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public void deleteUser(Integer id) {
        if (userRepository.findById(id).isPresent()) {
            userRepository.deleteById(id);
        }else {
            throw new UsernameNotFoundException("User not found");
        }
    }

    public Page<UsersProjection> getAllUsuariosPageable(Integer page, Integer size){
        return userRepository.getUsersPage(PageRequest.of(page, size));
    }
}
