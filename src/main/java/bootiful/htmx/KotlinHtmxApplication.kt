package bootiful.htmx

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxResponse
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import java.util.concurrent.atomic.AtomicInteger

@SpringBootApplication
open class KotlinHtmxApplication

fun main(args: Array<String>) {
    runApplication<KotlinHtmxApplication>(*args)
}

@Controller
@RequestMapping("/todos")
internal class KotlinTodoController {
    private val todos: MutableList<KotlinTodo> = mutableListOf()

    init {
        this.todos.add(KotlinTodos.todo("Kotlin is awesome"))
    }

    @GetMapping
    fun todos(model: Model): String {
        model.addAttribute("todos", this.todos)
        return "todos"
    }

    @PostMapping
    fun add(
        @RequestParam("new-todo") newTodo: String?,
        model: Model,
    ): HtmxResponse {
        this.todos.add(KotlinTodos.todo(newTodo))
        model.addAttribute("todos", this.todos)
        return HtmxResponse
            .builder()
            .view("todos :: todos")
            .view("todos :: todos-form")
            .build()
    }


    @ResponseBody
    @DeleteMapping(produces = [MediaType.TEXT_HTML_VALUE], path = ["/{todoId}"])
    fun delete(@PathVariable todoId: Int?): String {
        this.todos
            .filter { t: KotlinTodo? -> t!!.id == todoId }
            .forEach { o: KotlinTodo? -> this.todos.remove(o) }
        return ""
    }
}

internal data class KotlinTodo(val id: Int?, val title: String?)

internal object KotlinTodos {
    private val id = AtomicInteger(0)

    fun todo(title: String?): KotlinTodo {
        return KotlinTodo(id.incrementAndGet(), title)
    }
}