package ru.practicum.explorewithme.controllers.adminapi;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.user.NewUserRequest;
import ru.practicum.explorewithme.dto.user.UserDto;
import ru.practicum.explorewithme.dto.user.UserShortDto;
import ru.practicum.explorewithme.service.UserService;
import ru.practicum.explorewithme.statistics.StatisticsClient;
import ru.practicum.explorewithme.utils.PaginationUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
        statistics.registerHit(request);
        return service.getAll(ids, PaginationUtils.create(from, size));
    }

    @PostMapping
    public UserDto create(@RequestBody NewUserRequest user, HttpServletRequest request) {
        statistics.registerHit(request);
        return service.add(user);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id, HttpServletRequest request) {
        statistics.registerHit(request);
        service.delete(id);
    }
}
