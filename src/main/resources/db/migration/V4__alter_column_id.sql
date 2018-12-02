alter table parking_lot drop primary key;
alter table parking_boy drop primary key;
alter table parking_lot alter column id bigint not null identity;
alter table parking_boy alter column id bigint not null identity;