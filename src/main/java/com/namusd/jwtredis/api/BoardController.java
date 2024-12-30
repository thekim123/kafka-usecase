package com.namusd.jwtredis.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.namusd.jwtredis.handler.ex.EntityNotFoundException;
import com.namusd.jwtredis.model.dto.BoardDto;
import com.namusd.jwtredis.model.entity.Board;
import com.namusd.jwtredis.model.entity.User;
import com.namusd.jwtredis.persistence.mapper.BoardMapper;
import com.namusd.jwtredis.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class BoardController {

    private final BoardMapper boardMapper;
    private final UserRepository userRepository;

    @PostMapping("/board")
    public ResponseEntity<?> writeBoard(@RequestBody BoardDto.PostRequest dto) {
        boardMapper.insert(dto.toEntity());
        return ResponseEntity.created(URI.create("/api/board")).build();
    }

    @GetMapping("/board/{boardId}")
    public ResponseEntity<?> getBoard(@PathVariable Long boardId) {
        Board board = boardMapper.selectById(boardId);
        BoardDto.Response response = board.toDto();
        User user = userRepository.findById(board.getAuthorId()).orElseThrow(() -> new EntityNotFoundException("게시글 없음."));
        response.withAuthor(user.toDto());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/board-all")
    public ResponseEntity<?> getAllBoard() {
        List<Board> boardList = boardMapper.getAll();

        List<BoardDto.Response> response = boardList.stream().map(board -> {
            BoardDto.Response res = board.toDto();
            User user = userRepository.findById(board.getAuthorId()).orElseThrow(() -> new EntityNotFoundException("작성자가 없음"));
            res.withAuthor(user.toDto());
            return res;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }


}
