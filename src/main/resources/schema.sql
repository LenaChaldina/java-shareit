delete from public.bookings;
ALTER SEQUENCE public.bookings_booking_id_seq RESTART WITH 1;
delete from public.items;
ALTER SEQUENCE public.items_item_id_seq RESTART WITH 1;
delete from public.users;
ALTER SEQUENCE public.users_user_id_seq RESTART WITH 1;

/*INSERT INTO public.users(user_id, email, name)
VALUES (1, 'updateName@user.com', 'updateName');
INSERT INTO public.users(user_id, email, name)
VALUES (4, 'user@user.com', 'user');
INSERT INTO public.items(
    item_id, available, description, name, user_id, request_id)
VALUES (1, true, 'Аккумуляторная дрель + аккумулятор', 'Аккумуляторная дрель', 1, null);
INSERT INTO public.items(
    item_id, available, description, name, user_id, request_id)
VALUES (3, true, 'Тюбик суперклея марки Момент', 'Клей Момент', 4, null);
INSERT INTO public.items(
    item_id, available, description, name, user_id, request_id)
VALUES (2, true, 'Аккумуляторная отвертка', 'Отвертка', 4, null);*/
