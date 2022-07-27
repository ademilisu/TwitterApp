package demo.twitter.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

	@GetMapping(value = { "/", "/login", "/signup", "/home", "/profile/**", "/relation/**", "/tweet/**" })
	public String index() {
		return "index.html";
	}

}
