package ezpus.sample.domain;

import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter // getter를 자동으로 생성합니다.
@Table( name = "member" )
public class Member {
    @Id
    @Column( name = "id" )
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    @Column( name = "name" )
    private String name;

    /**
     * @param username
     */
    public Member( String name ) {
        this.name = name;
    }

    public void updateMember( Map<String, Object> param ) {
        this.name = (String) param.get( "name" );
    }
}
