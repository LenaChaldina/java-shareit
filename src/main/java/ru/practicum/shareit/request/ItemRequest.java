package ru.practicum.shareit.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "requests", schema = "public")
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    Long id;
    //текст запроса, содержащий описание требуемой вещи
    String description;
    //пользователь, создавший запрос
    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_ud")
    User requester;
    //владелец вещи
    LocalDateTime created;

    public ItemRequest() {

    }
}
