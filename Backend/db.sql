-- Create movie booking database
create database movie_booking_db;
use movie_booking_db;

--  Create tables
create table tb_movie (id varchar(255) not null, cast varchar(255) not null, country varchar(255) not null, description varchar(1500) not null, director varchar(255) not null, genre varchar(255) not null, language varchar(255) not null, poster_url varchar(255) not null, rating varchar(255) not null, release_date date not null, runtime integer not null, title varchar(255) not null, trailer_url varchar(255) not null, primary key (id));
create table tb_secret_questions (id bigint not null, question varchar(255) not null, primary key (id));
create table tb_showing (id varchar(255) not null, booked_seats integer not null, show_time varchar(255) not null, total_seats integer not null, movie_id varchar(255), theater_id varchar(255), primary key (id));
create table tb_threater (id varchar(255) not null, location varchar(255) not null, name varchar(255) not null, primary key (id));
create table tb_ticket_booking (id varchar(255) not null, num_seats integer, ticket_seats varchar(255), showing_id varchar(255), user_id varchar(255), primary key (id));
create table tb_user (user_id varchar(255) not null, answer varchar(255) not null, email varchar(255), first_name varchar(255) not null, last_name varchar(255) not null, password varchar(255) not null, role varchar(255), secret_question_id bigint not null, primary key (user_id));
alter table tb_user add constraint UK_4vih17mube9j7cqyjlfbcrk4m unique (email);
alter table tb_showing add constraint FK26vv0mcu7tcbkwtjrn4jmpckv foreign key (movie_id) references tb_movie (id);
alter table tb_showing add constraint FK45gqqa6kglo1k538vahu8hwuw foreign key (theater_id) references tb_threater (id);
alter table tb_ticket_booking add constraint FK5ydxtewpmeysan1tw4nw8jmhv foreign key (showing_id) references tb_showing (id);
alter table tb_ticket_booking add constraint FKmsdnxq0bsa75b59evlogae5ia foreign key (user_id) references tb_user (user_id);
alter table tb_user add constraint FK7ymkwe8exl395boykl5ibsdno foreign key (secret_question_id) references tb_secret_questions (id);

-- Insert data into tb_movie table
insert into tb_movie(id,title,description,release_date,runtime,genre,language,country,director,cast,rating,poster_url,trailer_url) values('M28d95dfe-974c-4462-bc40-44650c185cf6','Spider-Man: Across The Spider-Verse','Miles Morales returns for the next chapter of the Spider-Verse saga, Spider-Man: Across the Spider-Verse. After reuniting with Gwen Stacy, Brooklyns full-time, friendly neighborhood Spider-Man is catapulted across the Multiverse, where he encounters a team of Spider-People charged with protecting its very existence. But when the heroes clash on how to handle a new threat, Miles finds himself pitted against the other Spiders and must redefine what it means to be a hero so he can save the people he loves most.','2023-06-01',140,'Action, Adventure, Animation','English, Hindi, Tamil, Telugu','USA','Joaquim Dos Santos','Shameik Moore, Hailee Steinfeld, Shubman Gill, Jake Johnson, Oscar Isaac','9.3','https://assets-in.bmscdn.com/iedb/movies/images/mobile/thumbnail/xlarge/spider-man-across-the-spider-verse-et00347275-1684425314.jpg','https://www.youtube.com/watch?v=shW9i6k8cB0');
insert into tb_movie (id, title, description, release_date, runtime, genre, language, country, director, cast, rating, poster_url, trailer_url) values ('M3c056f1b-d2b8-4f3e-9a6b-f0ca0e8e51af', 'Fast X', 'Dom Toretto and his family must confront a terrifying new enemy who`s fueled by revenge.', '2023-05-18', 134, 'Action, Adventure, Crime, Thriller', 'English', 'USA', 'Louis Leterrier', 'Vin Diesel, Jason Momoa, John Cena, Jason Statham, Tyrese Gibson', '8.8', 'https://assets-in.bmscdn.com/iedb/movies/images/mobile/thumbnail/xlarge/fast-x-et00122562-1683110461.jpg', 'https://www.youtube.com/watch?v=sT7Wab-8QS8');
insert into tb_movie (id, title, description, release_date, runtime, genre, language, country, director, cast, rating, poster_url, trailer_url) values ('M7a1d7675-fac7-4a3a-8fb5-5d5f2fc48b81', 'To Catch A Killer', 'A talented but troubled cop is recruited by the FBI to help track down a murderer.', '2023-06-02', 121, 'Crime, Mystery, Thriller', 'English', 'USA', 'Damian Szifron', 'Shailene Woodley, Ben Mendelsohn, Ralph Ineson', '7.6', 'https://assets-in.bmscdn.com/iedb/movies/images/mobile/thumbnail/xlarge/to-catch-a-killer-et00356927-1684151622.jpg', 'https://www.youtube.com/watch?v=DXs8BJsnxU8');
insert into tb_movie (id, title, description, release_date, runtime, genre, language, country, director, cast, rating, poster_url, trailer_url) values ('M9f7be510-052d-4398-8d24-330d7bde158a', 'Guardians of Galaxy Vol. 3','Our beloved band of misfits are settling into life on Knowhere. But it isn\'t long before their lives are upended by the echoes of Rocket\'s turbulent past. Peter Quill, still reeling from the loss of Gamora, must rally his team around him on a dangerous mission to save Rocket\'s life - a mission that, if not completed successfully, could quite possibly lead to the end of the Guardians as we know them.','2023-05-05',150,'Action,Adventure,Comedy,Scifi','English,Hindi,Tamil,Telugu','USA','James Gunn','Chris Pratt, Zoe Saldana, Dave Baustita, Karen Gillan, Pom Klementieff, Vin Diesel','9.3','https://assets-in.bmscdn.com/iedb/movies/images/mobile/thumbnail/xlarge/guardians-of-the-galaxy-vol-3-et00310794-1683529214.jpg','https://www.youtube.com/watch?v=LH7KPMsN7nw');

-- Insert data into tb_theater table
insert into tb_threater(id,location,name) values ('T2b764eb8-7b5c-4e86-a119-d10734dfcc77','3rd Floor, Acropolis Mall, 1858 Rajdanga Main Road, Beside Geetanjali stadium, Kolkata, West Bengal 700107, India','Cinepolis: Acropolis Mall, Kolkata');
insert into tb_threater(id,location,name) values('Tb38ca66e-f538-4cbc-bd9f-f8365c0b2213','76, Ganesh Chandra Avenue, Bow Bazaar, Dharmatala, Near Chandni Chowk Metro Station, Kolkata, West Bengal 700013, India','Hind INOX: Kolkata');
insert into tb_threater(id,location,name) values('T82326e44-37b4-4e10-8b63-41904a6eacc8','City Centre, DC Block I, Sector-1, City Center Mall, Kolkata, West Bengal 700020, India','INOX: City Center, Salt Lake');
insert into tb_threater(id,location,name) values('T71b82fc1-f31e-4bf5-bd7a-526f96bb1417','The Terminus Bulding, Plot No. BG/12, AA-1B, New Town, Rajarhat, Kolkata, West Bengal 700156, India','Miraj Cinemas: Newtown, Kolkata');

-- Insert data into tb_showing table
insert into tb_showing(id,movie_id,theater_id,show_time,total_seats,booked_seats) values('MT1565cd96-3fd2-4f0e-a203-485abdfb2937','M28d95dfe-974c-4462-bc40-44650c185cf6','T2b764eb8-7b5c-4e86-a119-d10734dfcc77','7:15PM',110,0);
insert into tb_showing(id,movie_id,theater_id,show_time,total_seats,booked_seats) values('MT44dcc53d-a33e-42b1-ad81-17f03d2774a5','M28d95dfe-974c-4462-bc40-44650c185cf6','T82326e44-37b4-4e10-8b63-41904a6eacc8','2:00PM',70,0);
insert into tb_showing(id,movie_id,theater_id,show_time,total_seats,booked_seats) values('MT302376dc-5f04-4b28-af01-f8f9477c6ece','M3c056f1b-d2b8-4f3e-9a6b-f0ca0e8e51af','T82326e44-37b4-4e10-8b63-41904a6eacc8','5:00PM',100,0);

-- Inserting data into tb_secret_questions table
insert into tb_secret_questions(id,question) values (1,'What is your mother"s maiden name?');
insert into tb_secret_questions(id,question) values (2,'What is your favorite color?');
insert into tb_secret_questions(id,question) values (3,'What was the name of your first pet?');
insert into tb_secret_questions(id,question) values (4,'In what city were you born?');
insert into tb_secret_questions(id,question) values (5,'What is the name of your favorite teacher?');

-- Inserting data into tb_user table
insert into tb_user (user_id, email, first_name, last_name, password, role, secret_question_id, answer) values ('A346490ab-84c1-4d8b-87b8-92b98337438c','admin@gmail.com','James','Adams','$2a$10$/GMHaM4I0M0Qvcwtmn3RQuNQye33EPIT8aZxE/zU2uMvqjTaezOoO','ADMIN',2,'purple');
insert into tb_user (user_id, email, first_name, last_name, password, role, secret_question_id, answer) values ('A62204f38-ee22-11ed-a05b-0242ac120003','admin2@gmail.com','Jhon','Mendley','$2a$10$9665KAraOXQW8Eul6uenJu78VdImcVnRoIa3CObX4JLWJv9f9E/1q','ADMIN',3,'tom');