package ru.practicum.shareit.item.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "items", schema = "public")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    Long id;
    String name;
    String description;
    //статус о том, доступна или нет вещь для аренды
    Boolean available;
    //владелец вещи
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    User owner;
    //если вещь была создана по запросу другого пользователя, то в этом поле будет храниться ссылка на соответствующий запрос
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id")
    ItemRequest itemRequest;
    @OneToMany()
    @JoinColumn(name = "item_id")
    List<Booking> bookings;
    @OneToMany()
    @JoinColumn(name = "item_id")
    List<Comment> comments;

    public Item(Long id, String name, String description, Boolean available, User owner) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.owner = owner;
    }

    public Item(String name, String description, Boolean available, User owner) {
        this.name = name;
        this.description = description;
        this.available = available;
        this.owner = owner;
    }

    public Item() {
    }
}
