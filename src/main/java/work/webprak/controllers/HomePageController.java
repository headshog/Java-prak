package work.webprak.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomePageController {

    @RequestMapping(value = { "/", "/index"})
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/workers" )
    public String workers() {
        return "workers";
    }

    @RequestMapping(value = "/posts")
    public String posts() {
        return "posts";
    }

    @RequestMapping(value = "/subdivisions")
    public String subdivisions() {
        return "subdivisions";
    }
}

/* 
1. Поисковик можно сделать как обычный пост запрос на ту же страницу,
   просто с параметрами из поискового запроса.
2. Форма добавления - аналогична репе. Только будет сразу параметры из инпут-полей заноситься сразу в базу.
    <form method="get" action="/editPerson">
        <button id="addPersonButton" type="submit" class="btn btn-primary">Добавить человека</button>
    </form>
3. Таблица делается аналогично (persons.html). Для поддтаблиц все-таки будет отдельные страницы.
   Их удаление строк таблицы - гет запрос, который удаляет строку. Модификация - тот же гет запрос
   с параметрами из соотв. строки.
*/