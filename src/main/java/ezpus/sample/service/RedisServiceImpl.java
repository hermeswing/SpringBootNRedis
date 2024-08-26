package ezpus.sample.service;

import ezpus.sample.domain.Member;
import ezpus.sample.repository.RedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {

    private final RedisRepository redisRepository;

    /**
     * <pre>
     *     value = "Member" :: "Member"라는 이름의 캐시에 메서드의 결과가 저장됩니다.
     *     key = "#id" :: 캐시의 키를 지정합니다. id 파라미터 값이 캐시 키로 사용됩니다.
     *     cacheManager = "cacheManager" :: "cacheManager"라는 이름의 캐시 매니저를 사용
     *     unless = "#result == null" :: 메서드의 결과가 null인 경우 캐시에 저장되지 않도록 설정
     * </pre>
     * @Cacheable
     * @param id
     * @return
     */
    @Override
    @Cacheable( value = "Member", key = "#id", cacheManager = "cacheManager", unless = "#result == null" )
    public Member getMember( Long id ) {
        // 해당 ID에 대한 Member 객체가 존재하지 않으면 예외가 발생하는 것을 방지 하기 위해
        // .orElse(null)
        return redisRepository.findById( id ).orElse( null );
    }

    @Override
    @Transactional
    public void saveMember( Map<String, Object> param ) {
        Member member = new Member( (String) param.get( "name" ) );
        redisRepository.save( member );
    }

    @Override
    @Transactional
    public Member updateMember( Map<String, Object> param ) {

        Long id = 0L;

        if( param.get( "id" ) instanceof String ) {
            id = Long.parseLong( (String) param.get( "id" ) );
        } else if( param.get( "id" ) instanceof Integer ) {
            id = Long.valueOf( ( (Integer) param.get( "id" ) ).longValue() );
        }

        Optional<Member> member = redisRepository.findById( id );
        Member updateMember = member.get();
        updateMember.updateMember( param );

        return redisRepository.save( updateMember );
    }

    @Override
    @Transactional
    public void deleteMember( Long id ) {
        Optional<Member> member = redisRepository.findById( id );
        redisRepository.delete( member.get() );
    }
}
