package com.kaminsky;

import com.kaminsky.controller.ViewController;
import com.kaminsky.entity.User;
import com.kaminsky.repository.OrderRepository;
import com.kaminsky.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ViewController.class)
public class ViewControllerTests {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    OrderRepository orderRepository;

    @MockBean
    UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        when(userRepository.findAll()).thenReturn(new ArrayList<>());

        User user = new User();
        user.setId(1L);
        user.setEmail("johndoe@gmail.com");
        user.setName("John Doe");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        when(userRepository.existsById(1L)).thenReturn(true);
    }


    @Test
    public void testGet() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testGetById() throws Exception {
        mockMvc.perform(get("/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("johndoe@gmail.com"));

    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(delete("/1"))
                .andExpect(status().isOk());

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testPost() throws Exception {
        String newUserJson = """
                {
                    "name": "Jane Doe",
                    "email": "janedoe@gmail.com"
                }
                """;

        mockMvc.perform(post("/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newUserJson))
                        .andExpect(status().isOk());


        verify(userRepository, times(1)).save(argThat(user ->
                user.getName().equals("Jane Doe") &&
                        user.getEmail().equals("janedoe@gmail.com")));
    }

}
