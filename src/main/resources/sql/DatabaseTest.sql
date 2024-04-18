DROP DATABASE workers_test;

CREATE DATABASE workers_test;

\connect workers_test

CREATE SCHEMA workers_test_schema;

SET search_path = workers_test_schema, public;

CREATE FUNCTION time_now(offset_hr INT DEFAULT 0) RETURNS timestamp
    LANGUAGE sql IMMUTABLE
    AS $$SELECT date_trunc('minute', now()) + ($1 || ' hours')::INTERVAL;$$;

CREATE TABLE Workers (
  ID SERIAL PRIMARY KEY,
  Name TEXT NOT NULL,
  Address TEXT NOT NULL,
  Graduation TEXT NOT NULL,
  BirthDate TEXT NOT NULL,
  Experience INT NOT NULL DEFAULT 0
);

CREATE TABLE Posts (
  ID SERIAL PRIMARY KEY,
  Name TEXT NOT NULL UNIQUE,
  Responsibilities TEXT NOT NULL
);

CREATE TABLE Subdivisions (
  ID SERIAL PRIMARY KEY,
  HeadSubdID INT DEFAULT NULL,
  DirectorID INT,
  Name TEXT NOT NULL UNIQUE,
  CONSTRAINT head_constraint FOREIGN KEY (HeadSubdID) REFERENCES Subdivisions(ID) MATCH FULL ON DELETE SET NULL,
  CONSTRAINT director_constraint FOREIGN KEY (DirectorID) REFERENCES Workers(ID) MATCH FULL ON DELETE SET NULL
);

CREATE TABLE InnerSubdivisions (
  ID SERIAL PRIMARY KEY,
  MainSubdID INT NOT NULL,
  InnerSubdID INT NOT NULL,
  CONSTRAINT head_constraint FOREIGN KEY (MainSubdID) REFERENCES Subdivisions(ID) MATCH FULL ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT inner_constraint FOREIGN KEY (InnerSubdID) REFERENCES Subdivisions(ID) MATCH FULL ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE PostsHistory (
  ID SERIAL PRIMARY KEY,
  WorkerID INT NOT NULL,
  PostID INT,
  SubdivisionID INT,
  WorkStart TIMESTAMP NOT NULL,
  WorkEnd TIMESTAMP DEFAULT NULL,
  CONSTRAINT worker_constraint FOREIGN KEY (WorkerID) REFERENCES Workers(ID) MATCH FULL ON DELETE CASCADE,
  CONSTRAINT post_constraint FOREIGN KEY (PostID) REFERENCES Posts(ID) MATCH FULL ON DELETE SET NULL,
  CONSTRAINT subdivision_constraint FOREIGN KEY (SubdivisionID) REFERENCES Subdivisions(ID) MATCH FULL ON DELETE SET NULL
);

ALTER DATABASE workers_test SET search_path = workers_test_schema, public;

INSERT INTO Workers (Name, Address, Graduation, Experience, BirthDate) VALUES
  ('Tori Pena', '9255 Mayfield Ave. Rahway, NJ 07065', 'ABCD', 17, '1975-12-26'),
  ('Lucia Vincent', '72 SW. Myers Ave. Menasha, WI 54952', 'ABCD', 18, '1983-10-20'),
  ('Melvin Galloway', '31 Lakeshore St. Elizabethtown, PA 17022', 'ABCD', 19, '1988-01-08'),
  ('Corey Mueller', '186 Prospect Street Muskogee, OK 74403', 'ABCD', 20, '1988-03-08'),
  ('Jett Moon', '270 Helen Avenue Rosemount, MN 55068', 'ABCD', 2, '1988-09-03'),
  ('Nylah Molina', '7967 East Liberty St. Mahopac, NY 10541', 'ABCD', 16, '1991-04-08'),
  ('Lauryn Torres', '16 Tanglewood St. Altoona, PA 16601', 'ABCD', 15, '1992-01-25'),
  ('Kassidy Shepard', '15 Philmont Street Deland, FL 32720', 'ABCD', 14, '1994-08-01'),
  ('Pranav Shaw', '9345 South Evergreen Road Thibodaux, LA 70301', 'ABCD', 11, '1997-01-18'),
  ('Cortez Waller', '15 Manor Station Street Monroe Township, NJ 08831', 'ABCD', 9, '1998-08-29'),
  ('Madeleine Allen', '591 High Court Muskego, WI 53150', 'ABCD', 19, '1998-12-21'),
  ('Will Shepherd', '35 Bowman Road West Haven, CT 06516', 'ABCD', 20, '1999-04-26'),
  ('Jett Berger', '57 Windfall St. Baton Rouge, LA 70806', 'ABCD', 1, '2000-11-03'),
  ('Kathy Burch', '8495 Oak Road Bridgewater, NJ 08807', 'ABCD', 13, '2002-09-30');

INSERT INTO Posts (Name, Responsibilities) VALUES
  ('subdivision director', 'fall'),
  ('bulb', 'distribute'),
  ('species', 'amend'),
  ('family', 'apply'),
  ('agency', 'push'),
  ('humanity', 'calculate'),
  ('election', 'react'),
  ('formulate', 'attempt'),
  ('tourist', 'bind'),
  ('urge', 'rest');

INSERT INTO Subdivisions (HeadSubdID, DirectorID, Name) VALUES
  (NULL, 1, 'interaction'),
  (1, 2, 'library'),
  (2, 3, 'sir'),
  (2, 4, 'passion'),
  (3, 5, 'ability'),
  (3, 6, 'revolution'),
  (1, 7, 'candidate'),
  (2, 8, 'performance'),
  (4, 9, 'appearance'),
  (3, 10, 'population');

INSERT INTO InnerSubdivisions (MainSubdID, InnerSubdID) VALUES
  (1, 2),
  (1, 7),
  (2, 3),
  (2, 4),
  (2, 8),
  (3, 5),
  (3, 6),
  (3, 10),
  (4, 9);

INSERT INTO PostsHistory (WorkerID, PostID, SubdivisionID, WorkStart, WorkEnd) VALUES
  (1, 1, 1, time_now(-2), NULL),
  (2, 2, 2, time_now(-2), NULL),
  (3, 3, 3, time_now(-2), NULL),
  (4, 4, 4, time_now(-2), NULL),
  (5, 5, 5, time_now(-2), NULL),
  (6, 6, 6, time_now(-2), NULL),
  (7, 7, 7, time_now(-2), NULL),
  (8, 8, 8, time_now(-2), NULL),
  (9, 9, 9, time_now(-2), NULL),
  (10, 10, 10, time_now(-2), NULL),
  (11, 1, 1, time_now(-2), NULL),
  (12, 2, 2, time_now(-2), NULL),
  (13, 3, 3, time_now(-2), NULL),
  (14, 4, 4, time_now(-2), NULL),
  (1, 5, 5, time_now(-2), NULL),
  (5, 6, 6, time_now(-4), time_now(-2)),
  (2, 7, 7, time_now(-4), time_now(-2)),
  (3, 8, 8, time_now(-4), time_now(-2)),
  (4, 9, 9, time_now(-4), time_now(-2)),
  (5, 10, 10, time_now(-4), time_now(-2));
