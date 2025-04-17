package com.example.todochatbot.service;

import com.example.todochatbot.entity.Todo;
import com.example.todochatbot.repository.TodoRepository;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class TodoService {
    
    private static final Logger logger = LoggerFactory.getLogger(TodoService.class);
    private final TodoRepository todoRepository;
    private final GeminiService geminiService;

    @Autowired
    public TodoService(TodoRepository todoRepository, GeminiService geminiService) {
        this.todoRepository = todoRepository;
        this.geminiService = geminiService;
    }

    public List<Todo> getAllTodos() {
        logger.info("Lấy danh sách tất cả công việc");
        return todoRepository.findAllByOrderByCreatedAtDesc();
    }

    public Todo createTodo(@Valid Todo todo) {
        logger.info("Tạo công việc mới: {}", todo.getTitle());
        validateTodo(todo);
        setDefaultValues(todo);
        return todoRepository.save(todo);
    }

    public Todo createTodoWithAI(String prompt) {
        logger.info("Tạo công việc mới với AI. Prompt: {}", prompt);
        String suggestion = geminiService.generateTodoSuggestion(prompt);
        
        Todo todo = new Todo();
        todo.setTitle(suggestion);
        setDefaultValues(todo);
        
        logger.info("AI đã tạo công việc: {}", suggestion);
        return todoRepository.save(todo);
    }

    public Todo updateTodo(Long id, @Valid Todo todoDetails) {
        logger.info("Cập nhật công việc có ID: {}", id);
        Todo todo = getTodoById(id);
        
        validateTodo(todoDetails);
        updateTodoFields(todo, todoDetails);
        
        return todoRepository.save(todo);
    }

    public void deleteTodo(Long id) {
        logger.info("Xóa công việc có ID: {}", id);
        Todo todo = getTodoById(id);
        todoRepository.delete(todo);
        logger.info("Đã xóa công việc có ID: {}", id);
    }

    public Todo getTodoById(Long id) {
        return todoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy công việc với ID: " + id));
    }

    public List<Todo> getCompletedTodos() {
        logger.info("Lấy danh sách công việc đã hoàn thành");
        return todoRepository.findByCompletedOrderByCreatedAtDesc(true);
    }

    public List<Todo> getUncompletedTodos() {
        logger.info("Lấy danh sách công việc chưa hoàn thành");
        return todoRepository.findByCompletedOrderByCreatedAtDesc(false);
    }

    private void validateTodo(Todo todo) {
        if (todo.getTitle() == null || todo.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Tiêu đề công việc không được để trống");
        }
        if (todo.getTitle().length() > 255) {
            throw new IllegalArgumentException("Tiêu đề công việc không được vượt quá 255 ký tự");
        }
        if (todo.getDescription() != null && todo.getDescription().length() > 1000) {
            throw new IllegalArgumentException("Mô tả công việc không được vượt quá 1000 ký tự");
        }
    }

    private void setDefaultValues(Todo todo) {
        LocalDateTime now = LocalDateTime.now();
        todo.setCreatedAt(now);
        todo.setUpdatedAt(now);
        if (todo.getDueDate() == null) {
            todo.setDueDate(now.plusDays(1));
        }
        todo.setCompleted(false);
    }

    private void updateTodoFields(Todo todo, Todo todoDetails) {
        todo.setTitle(todoDetails.getTitle());
        todo.setDescription(todoDetails.getDescription());
        todo.setDueDate(todoDetails.getDueDate());
        todo.setCompleted(todoDetails.isCompleted());
        todo.setUpdatedAt(LocalDateTime.now());
    }
}