package com.wip.bool.userbox.domain;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wip.bool.cmmn.type.OrderType;
import com.wip.bool.cmmn.type.SortType;
import com.wip.bool.userbox.dto.UserBoxSongDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static com.wip.bool.music.song.domain.QSongDetail.songDetail;
import static com.wip.bool.userbox.domain.QUserBox.userBox;
import static com.wip.bool.userbox.domain.QUserBoxSong.userBoxSong;

@Repository
@RequiredArgsConstructor
public class UserBoxSongRepository {

    private final EntityManager entityManager;

    private final JPAQueryFactory queryFactory;

    public UserBoxSong save(UserBoxSong userBoxSong) {
        entityManager.persist(userBoxSong);
        return userBoxSong;
    }

    // TODO : UserBox의 user_id ---> index 설정
    // TODO : UserBox와 UserBoxSong간의 INNER JOIN(user_box_id)
    // TODO : UserBoxSong의 song_detail_id ---> index 설정
    // TODO : UserBoxSong와 SongDetail간의 INNER JOIN(song_detail_id)
    // TODO : 드라이빙 테이블은 : SongDetail, 드리븐 테이블 : UserBox, UserBoxSong
    // TODO : 선정이유 : OrderBy 조건을 인덱스로 활용하기 위해서
    // TODO : SongDetail <-> UserBoxSong <-> UserBox
    public List<UserBoxSongDto.UserBoxSongResponse> findAll(Long userId, SortType sortType, OrderType orderType,
                                                            int size, int offset) {
        return queryFactory.select(Projections.constructor(UserBoxSongDto.UserBoxSongResponse.class,
                userBoxSong.id, songDetail.id, songDetail.title, userBoxSong.createDate))
                .from(songDetail)
                .innerJoin(songDetail, userBoxSong.songDetail)
                .innerJoin(userBox, userBoxSong.userBox)
                .where(userBox.user.id.eq(userId))
                .orderBy(getOrder(sortType, orderType))
                .offset(offset)
                .limit(size)
                .fetch();
    }

    public Long delete(UserBoxSong userBoxSong) {
        entityManager.remove(userBoxSong);
        return 1L;
    }

    public Optional<UserBoxSong> findById(Long userBoxSongId) {
        return Optional.ofNullable(entityManager.find(UserBoxSong.class, userBoxSongId));
    }

    private OrderSpecifier getOrder(SortType sortType, OrderType orderType) {

        if(SortType.TITLE.equals(sortType)) {
            return titleOrder(orderType);
        }
        return guitarOrder(orderType);
    }

    private OrderSpecifier titleOrder(OrderType orderType) {
        return OrderType.ASC.equals(orderType) ? songDetail.title.asc() :
        songDetail.title.desc();
    }

    private OrderSpecifier guitarOrder(OrderType orderType) {
        return OrderType.ASC.equals(orderType) ? songDetail.guitarCode.guitarOrder.asc() :
                songDetail.guitarCode.guitarOrder.desc();
    }

}
