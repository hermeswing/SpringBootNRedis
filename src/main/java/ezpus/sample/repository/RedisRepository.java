package ezpus.sample.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ezpus.sample.domain.Member;

public interface RedisRepository extends JpaRepository<Member, Long> {
}
