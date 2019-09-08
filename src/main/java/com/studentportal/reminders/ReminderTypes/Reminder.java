package com.studentportal.reminders.ReminderTypes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.studentportal.reminders.Senders.SenderType;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "reminders")
@Inheritance(strategy = InheritanceType.JOINED)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value = AssignmentReminder.class, name = "AssignmentReminder"),
        @JsonSubTypes.Type(value = MeetingReminder.class, name = "MeetingReminder")
})
public abstract class Reminder {
    private int reminderNum;
    String title;
    String message;
    Date date;
    List<Integer> targetUserIds;
    SenderType senderType;

    public Reminder() {
        this.senderType = senderType;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "reminder_num")
    public int getReminderNum() {
        return reminderNum;
    }

    public void setReminderNum(int reminderNum) {
        this.reminderNum = reminderNum;
    }

    @Column(name = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "message")
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Temporal(value = TemporalType.DATE)
    @Column(name = "date")
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "target_user_ids")
    public List<Integer> getTargetUserIds() {
        return targetUserIds;
    }

    public void setTargetUserIds(List<Integer> targetUserIds) {
        this.targetUserIds = targetUserIds;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "senderType")
    public SenderType getSenderType() {
        return senderType;
    }

    public void setSenderType(SenderType senderType) {
        this.senderType = senderType;
    }

    public abstract void send();


}
