package todo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import todo.entity.Todo;
import todo.repository.TodoRepository;

import java.util.ArrayList;
import java.util.List;

@Controller
public class TodoController {

    @Autowired
    private TodoRepository todoRepository;

    @GetMapping("/todos")
    public String getAll(Model model, @Param("keyword") String keyword) {
        try {
            List<Todo> todos = new ArrayList<Todo>();

            if (keyword == null) {
                todoRepository.findAll().forEach(todos::add);
            } else {
                todoRepository.findByTitleContainingIgnoreCase(keyword).forEach(todos::add);
                model.addAttribute("keyword", keyword);
            }

            model.addAttribute("todos", todos);
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }

        return "home";
    }

    @GetMapping("/todos/new")
    public String addTodo(Model model) {
        Todo todo = new Todo();
        todo.setCompleted(true);

        model.addAttribute("todo", todo);
        model.addAttribute("pageTitle", "Create new Task");

        return "addtask";
    }

    @PostMapping("/todos/save")
    public String saveTodo(Todo todo, RedirectAttributes redirectAttributes) {
        try {
            todoRepository.save(todo);

            redirectAttributes.addFlashAttribute("message", "The Task has been saved successfully!");
        } catch (Exception e) {
            redirectAttributes.addAttribute("message", e.getMessage());
        }

        return "redirect:/todos";
    }

    @GetMapping("/todos/{id}")
    public String editTodo(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Todo todo = todoRepository.findById(id).get();

            model.addAttribute("todo", todo);
            model.addAttribute("pageTitle", "Edit Task (ID: " + id + ")");

            return "addtask";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());

            return "redirect:/";
        }
    }

    @GetMapping("/todos/{id}/completed/{status}")
    public String updateTodoCompletedStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean completed,
                                                Model model, RedirectAttributes redirectAttributes) {
        try {
            todoRepository.updateCompletedStatus(id, completed);

            String status = completed ? "completed" : "disabled";
            String message = "The Task id=" + id + " has been " + status;

            redirectAttributes.addFlashAttribute("message", message);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }

        return "redirect:/todos";
    }

    @GetMapping("/todos/delete/{id}")
    public String deleteTodo(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            todoRepository.deleteById(id);

            redirectAttributes.addFlashAttribute("message", "The Task with id=" + id + " has been deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }

        return "redirect:/todos";
    }
}
