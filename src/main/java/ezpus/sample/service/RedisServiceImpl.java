package ezpus.sample.service;

import java.util.Map;
import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ezpus.sample.domain.Member;
import ezpus.sample.repository.RedisRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {

    private final RedisRepository redisRepository;

    @Override
    @Cacheable( value = "Member", key = "#id", cacheManager = "cacheManager", unless = "#result == null" )
    public Member getMember( Long id ) {
        Optional<Member> member = redisRepository.findById( id );
        return member.get();
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

        if ( param.get( "id" ) instanceof String ) {
            id = Long.parseLong( (String) param.get( "id" ) );
        } else if ( param.get( "id" ) instanceof Integer ) {
            id = Long.valueOf( ( (Integer) param.get( "id" ) ).longValue() );
        }

        Optional<Member> member     = redisRepository.findById( id );
        Member           updateMember = member.get();
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
