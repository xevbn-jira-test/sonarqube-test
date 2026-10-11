package com.example.sonarqubetest.todo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TodoService {

    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public List<TodoResponse> findAll(Boolean completed) {
        List<Todo> todos = completed == null
                ? todoRepository.findAll()
                : todoRepository.findByCompleted(completed);
        return todos.stream().map(TodoResponse::from).toList();
    }

    public TodoResponse findById(Long id) {
        return TodoResponse.from(getTodo(id));
    }

    @Transactional
    public TodoResponse create(TodoRequest request) {
        Todo todo = new Todo(request.title(), request.description());
        return TodoResponse.from(todoRepository.save(todo));
    }

    @Transactional
    public TodoResponse update(Long id, TodoRequest request) {
        Todo todo = getTodo(id);
        todo.update(request.title(), request.description(), Boolean.TRUE.equals(request.completed()));
        return TodoResponse.from(todoRepository.saveAndFlush(todo));
    }

    @Transactional
    public TodoResponse toggle(Long id) {
        Todo todo = getTodo(id);
        todo.toggle();
        return TodoResponse.from(todoRepository.saveAndFlush(todo));
    }

    @Transactional
    public void delete(Long id) {
        todoRepository.delete(getTodo(id));
    }

    private Todo getTodo(Long id) {
        return todoRepository.findById(id)
                .orElseThrow(() -> new TodoNotFoundException(id));
    }
}
