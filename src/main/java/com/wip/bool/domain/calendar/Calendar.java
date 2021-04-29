package com.wip.bool.domain.calendar;

import com.wip.bool.cmmn.BaseEntity;
import com.wip.bool.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "calendar")
public class Calendar extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "caledar_id")
    private Long id;

    @Column(name = "calendar_date", nullable = false)
    private LocalDate calendarDate;

    @Column(name = "calendar_content", length = 150, nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

}
