package com.wip.bool.calendar.domain;

import com.wip.bool.cmmn.util.BaseEntity;
import com.wip.bool.cmmn.type.ShareType;
import com.wip.bool.user.domain.User;
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
    @Column(name = "calendar_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "share_type", length = 20, nullable = false)
    private ShareType shareType;

    @Column(name = "calendar_date", nullable = false)
    private LocalDateTime calendarDate;

    @Column(name = "calendar_title", length = 30, nullable = false)
    private String title;

    @Column(name = "calendar_content", length = 150, nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public static Calendar createCalender(String title, String content, LocalDateTime calendarDate, ShareType shareType, User user) {

        Calendar calendar = new Calendar();
        calendar.updateTitle(title);
        calendar.updateContent(content);
        calendar.updateCalendarDate(calendarDate);
        calendar.updateShareType(shareType);
        calendar.updateUser(user);
        return calendar;
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void updateCalendarDate(LocalDateTime calendarDate) {
        this.calendarDate = calendarDate;
    }

    public void updateShareType(ShareType shareType) {
        this.shareType = shareType;
    }

    public void updateUser(User user) {
        this.user = user;
    }

}
