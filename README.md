# Todo Chatbot với Gemini AI

Ứng dụng Todo List tích hợp với Gemini AI để tự động tạo các task dựa trên mô tả bằng ngôn ngữ tự nhiên.

## Công nghệ sử dụng

- Spring Boot 3.2.3
- Spring Data JPA
- MySQL
- Swagger UI
- Google Gemini AI
- Lombok

## Yêu cầu hệ thống

- Java 17 trở lên
- MySQL 8.0 trở lên
- Maven
- Gemini API Key

## Cài đặt và Chạy ứng dụng

1. Clone repository:
```bash
git clone https://github.com/yourusername/todo-chatbot.git
cd todo-chatbot
```

2. Cấu hình database trong `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/todo_db?createDatabaseIfNotExist=true
spring.datasource.username=your_username
spring.datasource.password=your_password
```

3. Cấu hình Gemini API trong `src/main/resources/application.properties`:
```properties
gemini.api.key=YOUR_GEMINI_API_KEY
```

4. Build và chạy ứng dụng:
```bash
mvn clean install
mvn spring-boot:run
```

5. Truy cập Swagger UI: http://localhost:8080/swagger-ui.html

## API Endpoints

### Todo Operations

#### Lấy tất cả tasks
```http
GET /api/todos
```

#### Lấy task theo ID
```http
GET /api/todos/{id}
```

#### Tạo task mới
```http
POST /api/todos
Content-Type: application/json

{
    "title": "Tiêu đề task",
    "description": "Mô tả chi tiết",
    "dueDate": "2024-04-20T10:00:00"
}
```

#### Tạo task với AI
```http
POST /api/todos/generate
Content-Type: application/json

{
    "prompt": "Tạo một task về việc học Spring Boot"
}
```

#### Cập nhật task
```http
PUT /api/todos/{id}
Content-Type: application/json

{
    "title": "Tiêu đề mới",
    "description": "Mô tả mới",
    "completed": true
}
```

#### Xóa task
```http
DELETE /api/todos/{id}
```

#### Lấy tasks đã hoàn thành
```http
GET /api/todos/completed
```

#### Lấy tasks chưa hoàn thành
```http
GET /api/todos/uncompleted
```

## Cấu trúc Project

```
src
├── main
│   ├── java
│   │   └── com
│   │       └── example
│   │           └── todochatbot
│   │               ├── TodoChatbotApplication.java
│   │               ├── config
│   │               │   └── SwaggerConfig.java
│   │               ├── controller
│   │               │   └── TodoController.java
│   │               ├── entity
│   │               │   └── Todo.java
│   │               ├── exception
│   │               │   └── ResourceNotFoundException.java
│   │               ├── repository
│   │               │   └── TodoRepository.java
│   │               └── service
│   │                   ├── GeminiService.java
│   │                   └── TodoService.java
│   └── resources
│       └── application.properties
```

## Đóng góp

1. Fork repository
2. Tạo branch mới (`git checkout -b feature/amazing-feature`)
3. Commit thay đổi (`git commit -m 'Add some amazing feature'`)
4. Push lên branch (`git push origin feature/amazing-feature`)
5. Tạo Pull Request

## License

[MIT](https://choosealicense.com/licenses/mit/)