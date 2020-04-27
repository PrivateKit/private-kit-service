CREATE TABLE input_types(
  id SERIAL PRIMARY KEY,
  input_type_name varchar(80) NOT NULL
 );

CREATE TABLE option_groups(
  id SERIAL PRIMARY KEY,
  option_group_name varchar(45) NOT NULL
 );

CREATE TABLE unit_of_measures(
  id SERIAL PRIMARY KEY,
  unit_of_measures_name varchar(80) NOT NULL
 );

CREATE TABLE option_choices(
  id SERIAL PRIMARY KEY,
  option_group_id int NOT NULL,
  option_choice_name varchar(45) NOT NULL,
  FOREIGN KEY (option_group_id) REFERENCES option_groups (id)
 );

CREATE TABLE questions(
  id SERIAL PRIMARY KEY,
  survey_section_id int NOT NULL,
  input_type_id int NOT NULL,
  question_name varchar(255) NOT NULL,
  question_subtext varchar(500),
  answer_required_yn boolean,
  option_group_id int,
  allow_multiple_option_answers_yn boolean,
  FOREIGN KEY (input_type_id) REFERENCES input_types (id),
  FOREIGN KEY (option_group_id) REFERENCES option_groups (id)
 );

CREATE TABLE question_options(
  id SERIAL PRIMARY KEY,
  question_id int NOT NULL,
  option_choice_id int NOT NULL,
  FOREIGN KEY (question_id) REFERENCES questions (id),
  FOREIGN KEY (option_choice_id) REFERENCES option_choices (id)
 );

CREATE TABLE answers(
  id SERIAL PRIMARY KEY,
  user_id int NOT NULL,
  question_option_id int NOT NULL,
  answer_numeric int,
  answer_text varchar(255),
  answer_yn boolean,
  unit_of_measure_id int,
  FOREIGN KEY (question_option_id) REFERENCES question_options (id),
  FOREIGN KEY (unit_of_measure_id) REFERENCES unit_of_measures (id)
 );