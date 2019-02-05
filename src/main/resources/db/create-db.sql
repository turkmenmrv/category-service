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
