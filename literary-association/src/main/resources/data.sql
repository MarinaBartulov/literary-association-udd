insert into role (name) values ('ROLE_READER');
insert into role (name) values ('ROLE_WRITER');
insert into role (name) value ('ROLE_BOARD_MEMBER');
insert into role (name) value ('ROLE_ADMIN');
insert into role (name) value ('ROLE_EDITOR');
insert into role (name) value ('ROLE_LECTURER');

insert into permission (name) values ('create_order');
insert into role_permissions (role_id, permission_id) values (1,1);

-- merchants
insert into merchant (merchant_name, merchant_email, activated, error_url, failed_url, success_url)
values ('Vulkan knjizare', 'sb-nsr1z4072854@business.example.com', true, 'https://localhost:3000/error', 'https://localhost:3000/failed', 'https://localhost:3000/success');
insert into merchant (merchant_name, merchant_email, activated, error_url, failed_url, success_url)
values ('Laguna', 'laguna@gmail.com', true, 'https://localhost:3000/error', 'https://localhost:3000/failed', 'https://localhost:3000/success');

-- beta-readers (sifra: reader123)
insert into user (type, email, password, username, first_name, last_name, city, country, verified, enabled, beta_reader, penalty_points)
value ('Reader', 'reader1@gmail.com', '$2a$10$MgS2lefNxeyaDHxP/inYO.D0G5bkS8OX5RbAj7MJgghT16n6dQwIe', 'reader1', 'Petar', 'Petrovic', 'Novi Sad', 'Serbia', true, true, true, 3);
insert into user (type, email, password, username, first_name, last_name, city, country, verified, enabled, beta_reader, penalty_points)
value ('Reader', 'reader2@gmail.com', '$2a$10$MgS2lefNxeyaDHxP/inYO.D0G5bkS8OX5RbAj7MJgghT16n6dQwIe', 'reader2', 'Marko', 'Markovic', 'Belgrade', 'Serbia', true, true, true, 0);
insert into user (type, email, password, username, first_name, last_name, city, country, verified, enabled, beta_reader, penalty_points)
value ('Reader', 'reader3@gmail.com', '$2a$10$MgS2lefNxeyaDHxP/inYO.D0G5bkS8OX5RbAj7MJgghT16n6dQwIe', 'reader3', 'Nikola', 'Nikolic', 'Leskovac', 'Serbia', true, true, true, 0);


-- board members (sifra: boardmember)
insert into user (type, email, password, username, first_name, last_name, city, country, verified, enabled)
value ('BoardMember', 'board.member1@gmail.com', '$2a$10$U0MOxpLw1mEOI/sJbPQfxOmCrnSQlhSHhT5oWW.EVFvL5ahoEoXFu', 'boardMember1', 'Pera', 'Peric', 'NS', 'Srbija', true, true);
insert into user (type, email, password, username, first_name, last_name, city, country, verified, enabled)
value ('BoardMember', 'board.member2@gmail.com', '$2a$10$U0MOxpLw1mEOI/sJbPQfxOmCrnSQlhSHhT5oWW.EVFvL5ahoEoXFu', 'boardMember2', 'Mika', 'Mikic', 'NS', 'Srbija', true, true);

-- admin (sifra: reader123)
insert into user (type, email, password, username, first_name, last_name, city, country, verified, enabled)
value ('Admin', 'admin@gmail.com', '$2a$10$MgS2lefNxeyaDHxP/inYO.D0G5bkS8OX5RbAj7MJgghT16n6dQwIe', 'admin123', 'Mika', 'Mikic', 'NS', 'Srbija', true, true);

-- editor (sifra: reader123)
insert into user (type, email, password, username, first_name, last_name, city, country, verified, enabled)
value ('Editor', 'editor123@maildrop.cc', '$2a$10$MgS2lefNxeyaDHxP/inYO.D0G5bkS8OX5RbAj7MJgghT16n6dQwIe', 'editor123', 'Pera', 'Peric', 'NS', 'Srbija', true, true);

-- writer (sifra: reader123, username: writer123)
insert into user (type, email, password, username, first_name, last_name, city, country, verified, enabled)
value ('Writer', 'car.insurance.praksa777@gmail.com', '$2a$10$MgS2lefNxeyaDHxP/inYO.D0G5bkS8OX5RbAj7MJgghT16n6dQwIe', 'writer123', 'Pera', 'Peric', 'Novi Sad', 'Serbia', true, true);

-- lectures
insert into user (type, email, password, username, first_name, last_name, city, country, verified, enabled)
value ('Lecturer', 'lecturer1@gmail.com', '$2a$10$MgS2lefNxeyaDHxP/inYO.D0G5bkS8OX5RbAj7MJgghT16n6dQwIe', 'lecturer1', 'Pera', 'Peric', 'NS', 'Srbija', true, true);
insert into user (type, email, password, username, first_name, last_name, city, country, verified, enabled)
value ('Lecturer', 'lecturer2@gmail.com', '$2a$10$MgS2lefNxeyaDHxP/inYO.D0G5bkS8OX5RbAj7MJgghT16n6dQwIe', 'lecturer2', 'Milan', 'Peric', 'NS', 'Srbija', true, true);
insert into user (type, email, password, username, first_name, last_name, city, country, verified, enabled)
value ('Lecturer', 'lecturer3@gmail.com', '$2a$10$MgS2lefNxeyaDHxP/inYO.D0G5bkS8OX5RbAj7MJgghT16n6dQwIe', 'lecturer3', 'Jovan', 'Peric', 'NS', 'Srbija', true, true);

-- writers (sifra: reader123, username: writer1234 id: 12)
insert into user (type, email, password, username, first_name, last_name, city, country, verified, enabled)
values ('Writer', 'bojka.slike@gmail.com', '$2a$10$MgS2lefNxeyaDHxP/inYO.D0G5bkS8OX5RbAj7MJgghT16n6dQwIe', 'writer1234', 'Nikola', 'Nikolic', 'NS', 'Srbija', true, true);

-- editors (sifra: reader123)
-- insert into user (type, email, password, username, first_name, last_name, city, country, verified, enabled)
-- value ('Editor', 'prvieditor@maildrop.cc', '$2a$10$MgS2lefNxeyaDHxP/inYO.D0G5bkS8OX5RbAj7MJgghT16n6dQwIe', 'editor1', 'Prvi', 'Prvic', 'NS', 'Srbija', true, true);
-- insert into user (type, email, password, username, first_name, last_name, city, country, verified, enabled)
-- value ('Editor', 'drugieditor@maildrop.cc', '$2a$10$MgS2lefNxeyaDHxP/inYO.D0G5bkS8OX5RbAj7MJgghT16n6dQwIe', 'editor12', 'Drugi', 'Drugic', 'NS', 'Srbija', true, true);
-- insert into user (type, email, password, username, first_name, last_name, city, country, verified, enabled)
-- value ('Editor', 'trecieditor@maildrop.cc', '$2a$10$MgS2lefNxeyaDHxP/inYO.D0G5bkS8OX5RbAj7MJgghT16n6dQwIe', 'editor1234', 'Treci', 'Trecic', 'NS', 'Srbija', true, true);
-- insert into user (type, email, password, username, first_name, last_name, city, country, verified, enabled)
-- value ('Editor', 'cetvrtieditor@maildrop.cc', '$2a$10$MgS2lefNxeyaDHxP/inYO.D0G5bkS8OX5RbAj7MJgghT16n6dQwIe', 'editor12345', 'Cetvrti', 'Cetvrtic', 'NS', 'Srbija', true, true);

insert into user (type, email, password, username, first_name, last_name, city, country, verified, enabled, beta_reader, penalty_points)
    value ('Reader', 'reader4@gmail.com', '$2a$10$MgS2lefNxeyaDHxP/inYO.D0G5bkS8OX5RbAj7MJgghT16n6dQwIe', 'reader4', 'Ana', 'Anic', 'Vranje', 'Serbia', true, true, true, 0);

insert into user_roles (user_id, role_id) values (1,1); -- reader
insert into user_roles (user_id, role_id) values (2,1); -- reader
insert into user_roles (user_id, role_id) values (3,1); -- reader
insert into user_roles (user_id, role_id) values (4,3); -- board member
insert into user_roles (user_id, role_id) values (5,3); -- board member
insert into user_roles (user_id, role_id) values (6,4); -- admin
insert into user_roles (user_id, role_id) values (7,5); -- editor
insert into user_roles (user_id, role_id) values (8,2); -- writer
insert into user_roles (user_id, role_id) values (9,6); -- lecturer
insert into user_roles (user_id, role_id) values (10,6); -- lecturer
insert into user_roles (user_id, role_id) values (11,6); -- lecturer
insert into user_roles (user_id, role_id) values (12,2); -- writer
-- insert into user_roles (user_id, role_id) values (13,5); -- editor
-- insert into user_roles (user_id, role_id) values (14,5); -- editor
-- insert into user_roles (user_id, role_id) values (15,5); -- editor
-- insert into user_roles (user_id, role_id) values (16,5); -- editor
insert into user_roles (user_id, role_id) values (13,1);


insert into genre (name, description) value ('Triler', 'Thrillers are characterized by fast pacing, frequent action, and resourceful heroes who must thwart the plans of more-powerful and better-equipped villains. Literary devices such as suspense, red herrings and cliffhangers are used extensively.');
insert into genre (name, description) value ('Tehnologija', 'Books in the technology nonfiction genre are about the scientific knowledge and practical purposes used to control and adapt to one''s natural environment.');
insert into genre (name, description) value ('Drama', 'Drama is a mode of fictional representation through dialogue and performance. It is one of the literary genres, which is an imitation of some action. It contains conflict of characters, particularly the ones who perform in front of audience on the stage.');
insert into genre (name, description) value ('Medicina', 'Books in the medical and medicine nonfiction genre are about medical practices and medicines used to treat illnesses. The books in this genre are often written by medical professionals, such as doctors; pharmacists too, in the instance of medicine.');
insert into genre (name, description) value ('Biografija', 'A biography, or simply bio, is a detailed description of a persons life. It involves more than just the basic facts like education, work, relationships, and death; it portrays a persons experience of these life events.');
insert into genre (name, description) value ('Sociologija', 'There are various writing genres within sociology. These genres include: social issue analyses, article critiques, quantitative research designs, quantitative research papers, qualitative research designs, and qualitative research papers.');
insert into genre (name, description) value ('Psihologija', 'Popular psychology (sometimes shortened as pop psychology or pop psych) is the concepts and theories about human mental life and behavior that are purportedly based on psychology and that find credence among and pass muster with the populace.');

-- genres for readers
-- reader1 : Thriller, Technology, Drama
-- reader2 : Medicine, Biography, Sociology
-- reader3 : Drama, Sociology, Psychology
-- reader4 : Technology, Medicine, Psychology
insert into reader_genre (reader_id,genre_id) value (1, 1);
insert into reader_genre (reader_id,genre_id) value (1, 2);
insert into reader_genre (reader_id,genre_id) value (1, 3);
insert into reader_genre (reader_id,genre_id) value (2, 4);
insert into reader_genre (reader_id,genre_id) value (2, 5);
insert into reader_genre (reader_id,genre_id) value (2, 6);
insert into reader_genre (reader_id,genre_id) value (3, 3);
insert into reader_genre (reader_id,genre_id) value (3, 6);
insert into reader_genre (reader_id,genre_id) value (3, 7);
insert into reader_genre (reader_id,genre_id) value (13, 2);
insert into reader_genre (reader_id,genre_id) value (13, 4);
insert into reader_genre (reader_id,genre_id) value (13, 7);

-- genres for beta-readers
-- beta-reader1 : Thriller, Technology, Drama, Medicine
-- beta-reader2 : Thriller, Medicine, Biography, Sociology
-- beta-reader3 : Thriller, Drama, Sociology, Psychology
-- beta-reader4 : Technology, Medicine, Biography, Psychology
insert into beta_reader_genre (beta_reader_id,genre_id) value (1, 1);
insert into beta_reader_genre (beta_reader_id,genre_id) value (1, 2);
insert into beta_reader_genre (beta_reader_id,genre_id) value (1, 3);
insert into beta_reader_genre (beta_reader_id,genre_id) value (1, 4);
insert into beta_reader_genre (beta_reader_id,genre_id) value (2, 1);
insert into beta_reader_genre (beta_reader_id,genre_id) value (2, 4);
insert into beta_reader_genre (beta_reader_id,genre_id) value (2, 5);
insert into beta_reader_genre (beta_reader_id,genre_id) value (2, 6);
insert into beta_reader_genre (beta_reader_id,genre_id) value (3, 1);
insert into beta_reader_genre (beta_reader_id,genre_id) value (3, 3);
insert into beta_reader_genre (beta_reader_id,genre_id) value (3, 6);
insert into beta_reader_genre (beta_reader_id,genre_id) value (3, 7);
insert into beta_reader_genre (beta_reader_id,genre_id) value (13, 2);
insert into beta_reader_genre (beta_reader_id,genre_id) value (13, 4);
insert into beta_reader_genre (beta_reader_id,genre_id) value (13, 5);
insert into beta_reader_genre (beta_reader_id,genre_id) value (13, 7);


insert into book (plagiarism, isbn, num_of_pages, open_access, pdf, price, publishers_address, synopsis, title, year, editor_id, genre_id, lecturer_id, publisher_id, writer_id)
value (false, '1234567891234', 100, true, 'files-uploaded/LiterarnoUdruzenje.pdf',10.59, 'Bulevar oslobodjenja 34, Novi Sad', 'Sada dopunjena novim istraživanjima, ova sjajna knjiga promenila je milione života svojim uvidom u razvojni mentalni sklop. Nakon decenija istraživanja svetski poznat univerzitetski psiholog dr Kerol S. Dvek otkrila je jednostavnu, ali revolucionarnu ideju: moć mentalnog sklopa. Pokazuje nam kako način na koji razmišljamo o svojim talentima i sposobnostima može dramatično uticati na uspeh u školi, sportu, umetnosti, kao i na gotovo svako područje ljudskog života.','Literarno udruženje','2015',7,7,9,1,8);

insert into book (plagiarism, isbn, num_of_pages, open_access, pdf, price, publishers_address, synopsis, title, year, editor_id, genre_id, lecturer_id, publisher_id, writer_id)
value (false, '1234567333333', 150, false, 'files-uploaded/KontrolnaTacka.pdf',17.50, 'Bulevar Mihajla Pupina 23, Novi Sad', 'Triler koji istražuje dubine ljudske psihe i njene tamne strane koje ne želimo da priznamo. Nemačka, 1994. U šumi koja guta svojim moćnim jelama varošice regije Baden-Virtemberg, na Badnje veče, inspektor Jirgen Fišer biće svedok scene koju neće zaboraviti do kraja života. Sneg je iznenada prestao da pada, i u dubokoj tišini koja ga je okruživala, pred njegovim očima pojavio se proplanak, savršen krug među jelama, a u njegovom centru crvena barica.','Kontrolna tacka','2015',7,1,9,2,8);

insert into book (plagiarism, isbn, num_of_pages, open_access, pdf, price, publishers_address, synopsis, title, year, editor_id, genre_id, lecturer_id, publisher_id, writer_id)
values (false, '3726384923721', 300, true, 'files-uploaded/Kriptovalute.pdf', 13.0, 'Jevrejska 10, Novi Sad', 'dddddddddddddd', 'Kriptovalute i Blockchain', '2020', 7, 2, 11, 1, 8);

insert into book (plagiarism, isbn, num_of_pages, open_access, pdf, price, publishers_address, synopsis, title, year, editor_id, genre_id, lecturer_id, publisher_id, writer_id)
values (false, '3382746309821', 300, true, 'files-uploaded/DrustveniAspektiCOVID19.pdf', 12.0, 'Bulevar Nikole Tesle 4, Beograd', 'hhhh', 'Društveni aspekti - COVID19 ', '2017', 7, 6, 10, 1, 12);

insert into book (plagiarism, isbn, num_of_pages, open_access, pdf, price, publishers_address, synopsis, title, year, editor_id, genre_id, lecturer_id, publisher_id, writer_id)
values (false, '1173829384923', 300, true, 'files-uploaded/ClanakODijabetesu.pdf', 15.0, 'Knez Mihajlova 45, Beograd', 'lll', 'Dijabetes - šećerna bolest', '2020', 7, 4, 9, 1, 8);
