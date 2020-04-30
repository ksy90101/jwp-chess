package wooteco.chess.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.google.gson.Gson;
import wooteco.chess.domain.position.Position;
import wooteco.chess.service.ChessService;

@Controller
public class SpringChessController {
	private static final Gson GSON = new Gson();

	private ChessService service;

	public SpringChessController(ChessService service) {
		this.service = service;
	}

	@GetMapping("/")
	public String renderStart() {
		return "index";
	}

	@GetMapping("/chess")
	public String renderBoard(@RequestParam("game_id") String gameId, Model model) {
		service.initialize(gameId);
		model.addAllAttributes(service.getBoard(gameId));
		return "chess";
	}

	@PutMapping("/api/piece/{game_id}")
	@ResponseBody
	public String updateBoard(@PathVariable String game_id, @RequestBody Map<String, String> moveInfo) {
		String from = moveInfo.get("from");
		String to = moveInfo.get("to");
		service.move(game_id, Position.of(from), Position.of(to));
		return GSON.toJson(from + " " + to);
	}

	@ResponseStatus(value = HttpStatus.CONFLICT)
	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseBody
	public String handleException(IllegalArgumentException e) {
		return e.getMessage();
	}

	@GetMapping("/status")
	public String renderResult(@RequestParam("game_id") String gameId, Model model) {
		model.addAllAttributes(service.getResult(gameId));
		return "status";
	}
}
