insert into users(id, join_date, name, password, ssn, grade) values (1001, now(), 'User1', 'test111', '111111-1111111', 'vip');
insert into users(id, join_date, name, password, ssn, grade) values (1002, now(), 'User2', 'test222', '111111-1111112', 'normal');
insert into users(id, join_date, name, password, ssn, grade) values (1003, now(), 'User3', 'test333', '111111-1111113', 'normal');

insert into post(description, user_id) values ('My first post', 1001);
insert into post(description, user_id) values ('My second post', 1001);