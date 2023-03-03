package ru.practicum.explorewithme.controllers.adminapi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.user.NewUserRequest;
import ru.practicum.explorewithme.dto.user.UserDto;
import ru.practicum.explorewithme.dto.user.UserShortDto;
import ru.practicum.explorewithme.service.UserService;
import ru.practicum.explorewithme.statistics.StatisticsClient;
import ru.practicum.explorewithme.utils.PaginationUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class UsersController {
    private final StatisticsClient statistics;
    private final UserService service;

    @GetMapping
    public List<UserShortDto> getAll(@RequestParam List<Long> ids,
                                     @RequestParam(defaultValue = "0") int from,
                                     @RequestParam(defaultValue = "10") int size,
                                     HttpServletRequest request) {
        log.info("Admin: user search");
        statistics.registerHit(request);
        return service.getAll(ids, PaginationUtils.create(from, size));
    }

    @PostMapping
    public UserDto create(@RequestBody @Validated NewUserRequest user, HttpServletRequest request) {
        log.info("Admin: create user: {}", user);
        statistics.registerHit(request);
        var result = service.add(user);
        log.info("Admin: created user: {}", result);
        return result;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id, HttpServletRequest request) {
        log.info("Admin: delete user: {}", id);
        statistics.registerHit(request);
        service.delete(id);
    }
}
