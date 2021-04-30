package com.wip.bool.domain.calendar;

import com.wip.bool.cmmn.BaseEntity;
import com.wip.bool.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "calendar")
public class Calendar extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "caledar_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "share_type")
    private ShareType shareType;

    @Column(name = "calendar_date", nullable = false)
    private LocalDateTime calendarDate;

    @Column(length = 30, nullable = false)
    private String title;

    @Column(name = "calendar_content", length = 150, nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

}
