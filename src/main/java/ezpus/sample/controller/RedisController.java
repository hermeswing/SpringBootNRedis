package ezpus.sample.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ezpus.sample.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping( "/" )
@RequiredArgsConstructor
public class RedisController {

    private final RedisService redisService;

    @GetMapping( "/{id}" )
    public ResponseEntity<?> getMemberInfo( @PathVariable( "id" ) Long id ) {
        return ResponseEntity.ok( redisService.getMember( id ) );
    }

    @PostMapping( "" )
    public ResponseEntity<?> joinMember( @RequestBody Map<String, Object> member ) {
        log.info("[joinMember] member :: {}", member);
        redisService.saveMember( member );
        return ResponseEntity.ok( "가입 완료" );
    }

    @PutMapping( "" )
    public ResponseEntity<?> updateMember( @RequestBody Map<String, Object> member ) {
        log.info("[updateMember] member :: {}", member);
        redisService.updateMember( member );
        return ResponseEntity.ok( "수정 완료" );
    }

    @DeleteMapping( "/{id}" )
    public ResponseEntity<?> deleteMember( @PathVariable( "id" ) Long id ) {
        redisService.deleteMember( id );
        return ResponseEntity.ok( "삭제 완료" );
    }
}
