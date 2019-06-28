package com.tw.api.unit.test.controller;

import com.tw.api.unit.test.domain.todo.Todo;
import com.tw.api.unit.test.domain.todo.TodoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TodoController.class)
@ActiveProfiles(profiles = "test")
class TodoControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TodoRepository todoRepository;

    @Test
    void getAll() throws Exception {
        //given
        when(todoRepository.getAll()).thenReturn(Arrays.asList(
                new Todo(0, "1", false, 1),
                new Todo(1, "2", false, 3)));
        //when
        ResultActions result = mvc.perform(get("/todos"));
        //then
        String except = "[{\"id\":0,\"title\":\"1\",\"completed\":false,\"order\":1,\"url\":\"\"},{\"id\":1,\"title\":\"2\",\"completed\":false,\"order\":3,\"url\":\"\"}]";
        result.andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().string(is(except)));
    }

    @Test
    void getTodo() throws Exception {
        //given
        when(todoRepository.findById(1)).thenReturn(Optional.of(new Todo(1, "2", false, 3)));
        //when
        ResultActions result = mvc.perform(get("/todos/1"));
        //then
        String except = "{\"id\":1,\"title\":\"2\",\"completed\":false,\"order\":3,\"url\":\"\"}";
        result.andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().string(is(except)));
    }

    @Test
    void getTodoNotFound() throws Exception {
        //given
        when(todoRepository.findById(1)).thenReturn(Optional.empty());
        //when
        ResultActions result = mvc.perform(get("/todos/1"));
        //then
        result.andExpect(status().isNotFound());
    }
}