INSERT INTO category(name) VALUES ('Eurogames');
INSERT INTO category(name) VALUES ('Ameritrash');
INSERT INTO category(name) VALUES ('Familiar');

INSERT INTO author(name, nationality) VALUES ('Alan R. Moon', 'US');
INSERT INTO author(name, nationality) VALUES ('Vital Lacerda', 'PT');
INSERT INTO author(name, nationality) VALUES ('Simone Luciani', 'IT');
INSERT INTO author(name, nationality) VALUES ('Perepau Llistosella', 'ES');
INSERT INTO author(name, nationality) VALUES ('Michael Kiesling', 'DE');
INSERT INTO author(name, nationality) VALUES ('Phil Walker-Harding', 'US');

INSERT INTO game(title, age, category_id, author_id) VALUES ('On Mars', '14', 1, 2);
INSERT INTO game(title, age, category_id, author_id) VALUES ('Aventureros al tren', '8', 3, 1);
INSERT INTO game(title, age, category_id, author_id) VALUES ('1920: Wall Street', '12', 1, 4);
INSERT INTO game(title, age, category_id, author_id) VALUES ('Barrage', '14', 1, 3);
INSERT INTO game(title, age, category_id, author_id) VALUES ('Los viajes de Marco Polo', '12', 1, 3);
INSERT INTO game(title, age, category_id, author_id) VALUES ('Azul', '8', 3, 5);

INSERT INTO client(name) VALUES ('Marcos Martinez');
INSERT INTO client(name) VALUES ('Alvaro Acevedo');
INSERT INTO client(name) VALUES ('Emilio Dominguez');

INSERT INTO loan(loan_date, return_date, game_id, client_id) VALUES ('2026-04-10', '2026-04-20', 1, 1);
INSERT INTO loan(loan_date, return_date, game_id, client_id) VALUES ('2026-04-12', '2026-04-22', 2, 1);
INSERT INTO loan(loan_date, return_date, game_id, client_id) VALUES ('2026-04-15', '2026-04-25', 3, 2);
INSERT INTO loan(loan_date, return_date, game_id, client_id) VALUES ('2026-04-15', '2026-04-25', 4, 2);
INSERT INTO loan(loan_date, return_date, game_id, client_id) VALUES ('2026-04-20', '2026-04-30', 1, 2);

-- usuario con hash de ejemplo para la contraseña "admin"
INSERT INTO users(username, password_hash) VALUES ('admin', '$2a$10$ygR/J4My3fMEKrl2n5ihbuTzuvDxGNCpWR.UxIX4PGV0LM254I.L2');