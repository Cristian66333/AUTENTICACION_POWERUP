package co.com.powerup.usecase.user;


import co.com.powerup.model.rol.gateways.RolRepository;
import co.com.powerup.model.user.User;
import co.com.powerup.model.user.gateways.UserRepository;
import co.com.powerup.usecase.user.exceptions.RolExistsException;
import co.com.powerup.usecase.user.exceptions.UserExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RolRepository rolRepository;

    private UserUseCase userUseCase;

    @BeforeEach
    void setUp() {
        //userUseCase = new UserUseCase(userRepository, rolRepository);
    }

    private User buildUser() {

        User u = new User();
        u.setId("123");
        u.setName("Alice");
        u.setEmail("alice@example.com");
        u.setIdRol(10L);
        return u;
    }

    @Test
    void saveUser_success_whenEmailNotExists_andRoleExists() {

        User input = buildUser();
        User saved = buildUser();

        when(userRepository.existsByEmail(input.getEmail())).thenReturn(Mono.just(false));
        when(rolRepository.existsById(input.getIdRol())).thenReturn(Mono.just(true));
        when(userRepository.saveUser(input)).thenReturn(Mono.just(saved));


        StepVerifier.create(userUseCase.saveUser(input))
                .expectNext(saved)
                .verifyComplete();


        InOrder inOrder = inOrder(userRepository, rolRepository);
        inOrder.verify(userRepository).existsByEmail(input.getEmail());
        inOrder.verify(rolRepository).existsById(input.getIdRol());
        inOrder.verify(userRepository).saveUser(input);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void saveUser_error_whenEmailAlreadyExists() {

        User input = buildUser();

        when(userRepository.existsByEmail(input.getEmail())).thenReturn(Mono.just(true));


        StepVerifier.create(userUseCase.saveUser(input))
                .expectError(UserExistsException.class)
                .verify();


        verify(rolRepository, never()).existsById(any());
        verify(userRepository, never()).saveUser(any());
        verify(userRepository).existsByEmail(input.getEmail());
        verifyNoMoreInteractions(userRepository, rolRepository);
    }

    @Test
    void saveUser_error_whenRoleDoesNotExist() {

        User input = buildUser();

        when(userRepository.existsByEmail(input.getEmail())).thenReturn(Mono.just(false));
        when(rolRepository.existsById(input.getIdRol())).thenReturn(Mono.just(false));


        StepVerifier.create(userUseCase.saveUser(input))
                .expectError(RolExistsException.class)
                .verify();


        verify(userRepository).existsByEmail(input.getEmail());
        verify(rolRepository).existsById(input.getIdRol());
        verify(userRepository, never()).saveUser(any());
        verifyNoMoreInteractions(userRepository, rolRepository);
    }
}
