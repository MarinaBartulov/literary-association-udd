insert into role (name) values ('ROLE_READER');
insert into role (name) values ('ROLE_WRITER');
insert into role (name) value ('ROLE_BOARD_MEMBER');
insert into role (name) value ('ROLE_ADMIN');
insert into role (name) value ('ROLE_EDITOR');
insert into role (name) value ('ROLE_LECTURER');

insert into permission (name) values ('create_order');

insert into role_permissions (role_id, permission_id) values (1,1);

insert into merchant (merchant_name, merchant_email, activated, error_url, failed_url, success_url)
values ('Vulkan knjizare', 'sb-nsr1z4072854@business.example.com', true, 'https://localhost:3000/error', 'https://localhost:3000/failed', 'https://localhost:3000/success');

insert into merchant (merchant_name, merchant_email, activated, error_url, failed_url, success_url)
values ('Laguna', 'laguna@gmail.com', true, 'https://localhost:3000/error', 'https://localhost:3000/failed', 'https://localhost:3000/success');

-- sifra: reader123, username: reader123
insert into user (type, email, password, username, first_name, last_name, city, country, verified, enabled, beta_reader, penalty_points)
value ('Reader', 'reader1@gmail.com', '$2a$10$MgS2lefNxeyaDHxP/inYO.D0G5bkS8OX5RbAj7MJgghT16n6dQwIe', 'reader123', 'Bojana', 'Kliska', 'Beska', 'Srbija', true, true, true, 3);

-- sifra boardmember
insert into user (type, email, password, username, first_name, last_name, city, country, verified, enabled)
value ('BoardMember', 'board.member1@gmail.com', '$2a$10$U0MOxpLw1mEOI/sJbPQfxOmCrnSQlhSHhT5oWW.EVFvL5ahoEoXFu', 'boardMember1', 'Pera', 'Peric', 'NS', 'Srbija', true, true);

insert into user (type, email, password, username, first_name, last_name, city, country, verified, enabled)
value ('BoardMember', 'board.member2@gmail.com', '$2a$10$U0MOxpLw1mEOI/sJbPQfxOmCrnSQlhSHhT5oWW.EVFvL5ahoEoXFu', 'boardMember2', 'Mika', 'Mikic', 'NS', 'Srbija', true, true);

-- sifra: reader123, username: admin123
insert into user (type, email, password, username, first_name, last_name, city, country, verified, enabled)
value ('Admin', 'admin@gmail.com', '$2a$10$MgS2lefNxeyaDHxP/inYO.D0G5bkS8OX5RbAj7MJgghT16n6dQwIe', 'admin123', 'Mika', 'Mikic', 'NS', 'Srbija', true, true);

-- sifra: reader123, username: editor123
insert into user (type, email, password, username, first_name, last_name, city, country, verified, enabled)
value ('Editor', 'editor123@maildrop.cc', '$2a$10$MgS2lefNxeyaDHxP/inYO.D0G5bkS8OX5RbAj7MJgghT16n6dQwIe', 'editor123', 'Pera', 'Peric', 'NS', 'Srbija', true, true);

-- sifra: reader123, username: writer123
insert into user (type, email, password, username, first_name, last_name, city, country, verified, enabled)
    value ('Writer', 'car.insurance.praksa777@gmail.com', '$2a$10$MgS2lefNxeyaDHxP/inYO.D0G5bkS8OX5RbAj7MJgghT16n6dQwIe', 'writer123', 'Pera', 'Peric', 'NS', 'Srbija', true, true);

insert into user_roles (user_id, role_id) values (1,1);
insert into user_roles (user_id, role_id) values (2,3);
insert into user_roles (user_id, role_id) values (3,3);
insert into user_roles (user_id, role_id) values (4,4); -- admin
insert into user_roles (user_id, role_id) values (5,5); -- editor
insert into user_roles (user_id, role_id) values (6,2); -- writer


insert into genre (name, description) value ('Thriller', 'Thrillers are characterized by fast pacing, frequent action, and resourceful heroes who must thwart the plans of more-powerful and better-equipped villains. Literary devices such as suspense, red herrings and cliffhangers are used extensively.');
insert into genre (name, description) value ('Romance', 'Romance fiction is smart, fresh and diverse. Two basic elements comprise every romance novel: a central love story and an emotionally satisfying and optimistic ending.');
insert into genre (name, description) value ('Drama', 'Drama is a mode of fictional representation through dialogue and performance. It is one of the literary genres, which is an imitation of some action. It contains conflict of characters, particularly the ones who perform in front of audience on the stage.');
insert into genre (name, description) value ('Crime', 'The crime genre includes the broad selection of books on criminals and the court system, but the most common focus is investigations and sleuthing. Mystery novels are usually placed into this category, although there is a separate division for "crime".');
insert into genre (name, description) value ('Biography', 'A biography, or simply bio, is a detailed description of a persons life. It involves more than just the basic facts like education, work, relationships, and death; it portrays a persons experience of these life events.');
insert into genre (name, description) value ('Classic', 'A classic is a novel makes a contribution to literature. Classics come from all cultures and all years, and can reflect a time period, a societal standard or may offer commentary on a subject.');
insert into genre (name, description) value ('Psychology', 'Popular psychology (sometimes shortened as pop psychology or pop psych) is the concepts and theories about human mental life and behavior that are purportedly based on psychology and that find credence among and pass muster with the populace.');


insert into reader_genre (reader_id,genre_id) value (1, 1);
insert into reader_genre (reader_id,genre_id) value (1, 2);

insert into beta_reader_genre (beta_reader_id,genre_id) value (1, 1);
insert into beta_reader_genre (beta_reader_id,genre_id) value (1, 2);

-- sifra: reader123, username: reader1234
insert into user (type, email, password, username, first_name, last_name, city, country, verified, enabled, beta_reader, penalty_points)
value ('Reader', 'reader2@gmail.com', '$2a$10$MgS2lefNxeyaDHxP/inYO.D0G5bkS8OX5RbAj7MJgghT16n6dQwIe', 'reader1234', 'Senka', 'Soic', 'NS', 'Srbija', true, true, true, 4);

-- sifra: reader123, username: reader12345
insert into user (type, email, password, username, first_name, last_name, city, country, verified, enabled, beta_reader, penalty_points)
value ('Reader', 'reader3@gmail.com', '$2a$10$MgS2lefNxeyaDHxP/inYO.D0G5bkS8OX5RbAj7MJgghT16n6dQwIe', 'reader12345', 'Marina', 'Bartulov', 'BG', 'Srbija', true, true, true, 0);

insert into beta_reader_genre (beta_reader_id,genre_id) value (7, 3);
insert into beta_reader_genre (beta_reader_id,genre_id) value (8, 3);

insert into user_roles (user_id, role_id) values (7,1);
insert into user_roles (user_id, role_id) values (8,1);

insert into user (type, email, password, username, first_name, last_name, city, country, verified, enabled)
value ('Lecturer', 'lecturer1@gmail.com', '$2a$10$MgS2lefNxeyaDHxP/inYO.D0G5bkS8OX5RbAj7MJgghT16n6dQwIe', 'lecturer1', 'Pera', 'Peric', 'NS', 'Srbija', true, true);

insert into user (type, email, password, username, first_name, last_name, city, country, verified, enabled)
value ('Lecturer', 'lecturer2@gmail.com', '$2a$10$MgS2lefNxeyaDHxP/inYO.D0G5bkS8OX5RbAj7MJgghT16n6dQwIe', 'lecturer2', 'Milan', 'Peric', 'NS', 'Srbija', true, true);

insert into user (type, email, password, username, first_name, last_name, city, country, verified, enabled)
value ('Lecturer', 'lecturer3@gmail.com', '$2a$10$MgS2lefNxeyaDHxP/inYO.D0G5bkS8OX5RbAj7MJgghT16n6dQwIe', 'lecturer3', 'Jovan', 'Peric', 'NS', 'Srbija', true, true);

insert into user_roles (user_id, role_id) values (9,6);
insert into user_roles (user_id, role_id) values (10,6);
insert into user_roles (user_id, role_id) values (11,6);

insert into book (plagiarism, isbn, num_of_pages, open_access, pdf, price, publishers_address, synopsis, title, year, editor_id, genre_id, lecturer_id, publisher_id, writer_id)
values (false, '2222', 300, true, 'plagiarism-files/KontrolnaTacka.pdf', 13.0, 'Novi Sad, Jevrejska 10', 'dddddddddddddd', 'Kul knjiga 1', '2020', 5, 1, 11, 1, 6);

-- sifra: reader123, username: writer1234 id: 12
insert into user (type, email, password, username, first_name, last_name, city, country, verified, enabled)
values ('Writer', 'bojka.slike@gmail.com', '$2a$10$MgS2lefNxeyaDHxP/inYO.D0G5bkS8OX5RbAj7MJgghT16n6dQwIe', 'writer1234', 'Nikola', 'Nikolic', 'NS', 'Srbija', true, true);

insert into book (plagiarism, isbn, num_of_pages, open_access, pdf, price, publishers_address, synopsis, title, year, editor_id, genre_id, lecturer_id, publisher_id, writer_id)
values (false, '2223', 300, true, 'plagiarism-files/KontrolnaTacka2.pdf', 12.0, 'Novi Sad, Jevrejska 10', 'hhhh', 'Kul knjiga 2', '2020', 5, 2, 10, 1, 12);

insert into book (plagiarism, isbn, num_of_pages, open_access, pdf, price, publishers_address, synopsis, title, year, editor_id, genre_id, lecturer_id, publisher_id, writer_id)
values (false, '2224', 300, true, 'plagiarism-files/KontrolnaTacka2.pdf', 15.0, 'Novi Sad, Jevrejska 10', 'lll', 'Kul knjiga 3', '2020', 5, 1, 9, 1, 6);

-- sifra: reader123, username: editor1
insert into user (type, email, password, username, first_name, last_name, city, country, verified, enabled)
value ('Editor', 'prvieditor@maildrop.cc', '$2a$10$MgS2lefNxeyaDHxP/inYO.D0G5bkS8OX5RbAj7MJgghT16n6dQwIe', 'editor1', 'Prvi', 'Prvic', 'NS', 'Srbija', true, true);

-- sifra: reader123, username: editor12
insert into user (type, email, password, username, first_name, last_name, city, country, verified, enabled)
value ('Editor', 'drugieditor@maildrop.cc', '$2a$10$MgS2lefNxeyaDHxP/inYO.D0G5bkS8OX5RbAj7MJgghT16n6dQwIe', 'editor12', 'Drugi', 'Drugic', 'NS', 'Srbija', true, true);

insert into user_roles (user_id, role_id) values (12,2);
insert into user_roles (user_id, role_id) values (13,5);
insert into user_roles (user_id, role_id) values (14,5);

-- sifra: reader123, username: editor1234
insert into user (type, email, password, username, first_name, last_name, city, country, verified, enabled)
value ('Editor', 'trecieditor@maildrop.cc', '$2a$10$MgS2lefNxeyaDHxP/inYO.D0G5bkS8OX5RbAj7MJgghT16n6dQwIe', 'editor1234', 'Treci', 'Trecic', 'NS', 'Srbija', true, true);

-- sifra: reader123, username: editor12345
insert into user (type, email, password, username, first_name, last_name, city, country, verified, enabled)
value ('Editor', 'cetvrtieditor@maildrop.cc', '$2a$10$MgS2lefNxeyaDHxP/inYO.D0G5bkS8OX5RbAj7MJgghT16n6dQwIe', 'editor12345', 'Cetvrti', 'Cetvrtic', 'NS', 'Srbija', true, true);

insert into user_roles (user_id, role_id) values (15,5);
insert into user_roles (user_id, role_id) values (16,5);

insert into book (plagiarism, isbn, num_of_pages, open_access, pdf, price, publishers_address, synopsis, title, year, editor_id, genre_id, lecturer_id, publisher_id, writer_id)
value (false, '1234567891234', 100, true, '/uploaded-files/knjiga1.pdf',10.59, 'Bulevar oslobodjenja 34', 'Sada dopunjena novim istraživanjima, ova sjajna knjiga promenila je milione života svojim uvidom u razvojni mentalni sklop. Nakon decenija istraživanja svetski poznat univerzitetski psiholog dr Kerol S. Dvek otkrila je jednostavnu, ali revolucionarnu ideju: moć mentalnog sklopa. Pokazuje nam kako način na koji razmišljamo o svojim talentima i sposobnostima može dramatično uticati na uspeh u školi, sportu, umetnosti, kao i na gotovo svako područje ljudskog života.','Mentalni sklop','2015',5,7,9,1,6);

insert into book (plagiarism, isbn, num_of_pages, open_access, pdf, price, publishers_address, synopsis, title, year, editor_id, genre_id, lecturer_id, publisher_id, writer_id)
value (false, '1234567333333', 150, false, '/uploaded-files/knjiga2.pdf',17.50, 'Bulevar Mihajla Pupina 23', 'Triler koji istražuje dubine ljudske psihe i njene tamne strane koje ne želimo da priznamo. Nemačka, 1994. U šumi koja guta svojim moćnim jelama varošice regije Baden-Virtemberg, na Badnje veče, inspektor Jirgen Fišer biće svedok scene koju neće zaboraviti do kraja života. Sneg je iznenada prestao da pada, i u dubokoj tišini koja ga je okruživala, pred njegovim očima pojavio se proplanak, savršen krug među jelama, a u njegovom centru crvena barica.','Zora u crnoj sumi','2015',5,1,9,2,6);
