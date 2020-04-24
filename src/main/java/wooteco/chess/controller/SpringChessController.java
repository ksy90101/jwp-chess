package wooteco.chess.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import wooteco.chess.service.ChessService;

@Controller
public class SpringChessController {

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
		model.mergeAttributes(service.getBoard(gameId));
		return "chess";
	}
}
