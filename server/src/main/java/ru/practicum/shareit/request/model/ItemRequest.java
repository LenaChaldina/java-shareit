package ru.practicum.shareit.request.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "requests", schema = "public")
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    Long id;
    //текст запроса, содержащий описание требуемой вещи
    String description;
    //пользователь, создавший запрос
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    User requester;
    LocalDateTime created;
    @OneToMany(mappedBy = "itemRequest")
    List<Item> items = new ArrayList<>();

    public ItemRequest(String description, User requester, LocalDateTime created, List<Item> items) {
        this.description = description;
        this.requester = requester;
        this.created = created;
        this.items = items;
    }
}
