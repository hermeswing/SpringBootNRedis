package ezpus.sample.service;

import java.util.Map;

import ezpus.sample.domain.Member;

public interface RedisService {
    void saveMember( Map<String, Object> member );

    Member updateMember( Map<String, Object> member );

    Member getMember( Long id );

    void deleteMember( Long id );
}
