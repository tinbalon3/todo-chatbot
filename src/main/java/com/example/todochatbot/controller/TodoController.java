package com.example.todochatbot.controller;

import com.example.todochatbot.entity.Todo;
import com.example.todochatbot.service.TodoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
@CrossOrigin(origins = "*")
@Tag(name = "Todo API", description = "API quản lý công việc cần làm với tích hợp AI")
public class TodoController {

    private final TodoService todoService;

    @Autowired
    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping
    @Operation(summary = "Lấy danh sách tất cả công việc",
               description = "Trả về danh sách tất cả công việc, sắp xếp theo thời gian tạo giảm dần")
    @ApiResponse(responseCode = "200", description = "Thành công",
                content = @Content(schema = @Schema(implementation = Todo.class)))
    public ResponseEntity<List<Todo>> getAllTodos() {
        return ResponseEntity.ok(todoService.getAllTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy thông tin một công việc theo ID")
    @ApiResponse(responseCode = "200", description = "Thành công")
    @ApiResponse(responseCode = "404", description = "Không tìm thấy công việc")
    public ResponseEntity<Todo> getTodoById(
            @Parameter(description = "ID của công việc") @PathVariable Long id) {
        return ResponseEntity.ok(todoService.getTodoById(id));
    }

    @PostMapping
    @Operation(summary = "Tạo công việc mới")
    @ApiResponse(responseCode = "200", description = "Tạo thành công")
    public ResponseEntity<Todo> createTodo(@Valid @RequestBody Todo todo) {
        return ResponseEntity.ok(todoService.createTodo(todo));
    }

    @PostMapping("/generate")
    @Operation(summary = "Tạo công việc mới với sự trợ giúp của AI",
              description = "Sử dụng Gemini AI để gợi ý và tạo công việc mới")
    @ApiResponse(responseCode = "200", description = "Tạo thành công")
    public ResponseEntity<Todo> createTodoWithAI(
            @Parameter(description = "Yêu cầu tạo công việc bằng ngôn ngữ tự nhiên")
            @RequestParam String prompt) {
        return ResponseEntity.ok(todoService.createTodoWithAI(prompt));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật thông tin công việc")
    @ApiResponse(responseCode = "200", description = "Cập nhật thành công")
    @ApiResponse(responseCode = "404", description = "Không tìm thấy công việc")
    public ResponseEntity<Todo> updateTodo(
            @Parameter(description = "ID của công việc") @PathVariable Long id,
            @Valid @RequestBody Todo todo) {
        return ResponseEntity.ok(todoService.updateTodo(id, todo));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa một công việc")
    @ApiResponse(responseCode = "200", description = "Xóa thành công")
    @ApiResponse(responseCode = "404", description = "Không tìm thấy công việc")
    public ResponseEntity<Void> deleteTodo(
            @Parameter(description = "ID của công việc") @PathVariable Long id) {
        todoService.deleteTodo(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/completed")
    @Operation(summary = "Lấy danh sách công việc đã hoàn thành")
    @ApiResponse(responseCode = "200", description = "Thành công")
    public ResponseEntity<List<Todo>> getCompletedTodos() {
        return ResponseEntity.ok(todoService.getCompletedTodos());
    }

    @GetMapping("/uncompleted")
    @Operation(summary = "Lấy danh sách công việc chưa hoàn thành")
    @ApiResponse(responseCode = "200", description = "Thành công")
    public ResponseEntity<List<Todo>> getUncompletedTodos() {
        return ResponseEntity.ok(todoService.getUncompletedTodos());
    }
}