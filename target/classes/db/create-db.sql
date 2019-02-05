create database postgres_demo ;

\connect postgres_demo ;

CREATE SCHEMA demo ;

CREATE TABLE demo.category
( id uuid,
  name text NOT NULL,
  slug text NOT NULL,
  parentcategory_id uuid,
  is_visible boolean NOT NULL,
  PRIMARY KEY (id),
  UNIQUE(name)
  );

INSERT INTO demo.category
    (id, "name", slug, parentcategory_id, is_visible)
VALUES('2995de7f-13c5-44a0-8e2e-c275e6a8dc03', 'name1', 'slug1', NULL, true);
INSERT INTO demo.category
    (id, "name", slug, parentcategory_id, is_visible)
VALUES('feba9c6e-97e4-4362-b8d7-e8cf195cd42f', 'name2', 'slug2', NULL, true);
INSERT INTO demo.category
    (id, "name", slug, parentcategory_id, is_visible)
VALUES('c3009396-6175-400b-b4b2-508750de54d2', 'name3', 'slug3', NULL, true);