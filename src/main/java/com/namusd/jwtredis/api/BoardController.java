package com.namusd.jwtredis.api;

import com.namusd.jwtredis.config.auth.PrincipalDetails;
import com.namusd.jwtredis.handler.ex.EntityNotFoundException;
import com.namusd.jwtredis.model.dto.BoardDto;
import com.namusd.jwtredis.model.entity.Board;
import com.namusd.jwtredis.model.entity.User;
import com.namusd.jwtredis.repository.BoardRepository;
import com.namusd.jwtredis.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class BoardController {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    @PostMapping("/board")
    public ResponseEntity<?> writeBoard(
            Authentication auth,
            @RequestBody BoardDto.PostRequest dto
    ) {
        User author = ((PrincipalDetails) auth.getPrincipal()).getUser();
        boardRepository.save(dto.toEntity(author));
        return ResponseEntity.created(URI.create("/api/board")).build();
    }

    @GetMapping("/board/{boardId}")
    public ResponseEntity<?> getBoard(@PathVariable Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new EntityNotFoundException("Board not found"));
        BoardDto.Response response = board.toDto();
        User user = userRepository.findById(board.getAuthor().getId()).orElseThrow(() -> new EntityNotFoundException("게시글 없음."));
        response.withAuthor(user.toDto());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/board-all")
    public ResponseEntity<?> getAllBoard() {
        List<Board> boardList = boardRepository.findAll();

        List<BoardDto.Response> response = boardList.stream().map(board -> {
            BoardDto.Response res = board.toDto();
            User user = userRepository.findById(board.getAuthor().getId()).orElseThrow(() -> new EntityNotFoundException("작성자가 없음"));
            res.withAuthor(user.toDto());
            return res;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }


}
