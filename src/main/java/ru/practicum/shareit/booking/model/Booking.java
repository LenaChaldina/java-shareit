package ru.practicum.shareit.booking.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.apache.el.stream.Optional;
import ru.practicum.shareit.enums.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "bookings", schema = "public")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    Long id;
    //дата и время начала бронирования;
    @Column(name = "start_booking")
    LocalDateTime start;
    //дата и время конца бронирования
    @Column(name = "end_booking")
    LocalDateTime end;
    //вещь, которую пользователь бронирует
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    Item item;
    //пользователь, который осуществляет бронирование
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User booker;
    //статус бронирования
    @Enumerated(EnumType.STRING)
    Status status;

    /*@JoinTable(
            name = "items", schema = "public",

            joinColumns = @JoinColumn(
                    name = "booking_id"
            ),foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT),

            inverseJoinColumns = @JoinColumn(
                    name = "user_id"
            ),inverseForeignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)
    )*/
    @ManyToMany
    List<User> users = new ArrayList<>();

    public Booking(LocalDateTime start, LocalDateTime end, Item item, User booker) {
        this.start = start;
        this.end = end;
        this.item = item;
        this.booker = booker;
    }

    public Booking() {
    }
}