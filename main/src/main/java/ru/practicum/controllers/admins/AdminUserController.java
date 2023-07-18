package ru.practicum.controllers.admins;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.users.dto.UserInDto;
import ru.practicum.users.dto.UserOutDto;
import ru.practicum.users.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/admin/users")
@Validated
public class AdminUserController {

    private final UserService userService;

    @Autowired
    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public UserOutDto addUser(@Valid @RequestBody UserInDto inDto) {
        UserOutDto result = userService.add(inDto);
        log.info("Создан пользователь {}", result);
        return result;
    }

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<UserOutDto> getUsers(
            @RequestParam Optional<List<Long>> ids,
            @PositiveOrZero(message = "Параметр from должен быть больше или равен нулю.")
            @RequestParam(value = "from", defaultValue = "0") int from,
            @Positive(message = "Параметр size должен быть больше нуля.")
            @RequestParam(value = "size", defaultValue = "10") int size) {
        List<UserOutDto> result = userService.get(ids, from, size);
        log.info("Получены пользователи {}", result);
        return result;
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteUserById(@PathVariable(value = "userId") long userId) {
        userService.deleteById(userId);
        log.info("Удален пользователь id: {}", userId);

    }

}
