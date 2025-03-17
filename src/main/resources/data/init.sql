create database db_springProject;
create user 'user_springProject'@'localhost' identified by '1234';
grant all privileges on db_springProject.* to 'user_springProject'@'localhost';