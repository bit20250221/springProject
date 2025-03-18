create database IF NOT EXISTS db_springproject;
create user 'user_springproject'@'%' identified by '1234';
grant all privileges on db_springproject.* to 'user_springproject'@'%';
FLUSH PRIVILEGES;