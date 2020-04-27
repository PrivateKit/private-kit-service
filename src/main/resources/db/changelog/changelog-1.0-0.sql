CREATE TABLE apps(
  app_namespace varchar(80) not null,
  app_key varchar(80) not null,
  app_status varchar(80) not null,
  PRIMARY KEY (app_namespace, app_key)
 );

CREATE TABLE surveys(
  id SERIAL PRIMARY KEY,
  survey_name varchar(80) not null,
  survey_description varchar(255),
  survey_image varchar(255),
  app_namespace varchar(80) not null,
  app_key varchar(80) not null,
  FOREIGN KEY (app_namespace, app_key) REFERENCES apps (app_namespace, app_key)
 );

CREATE TABLE survey_option_groups(
  survey_id int not null,
  option_key varchar(80) not null,
  PRIMARY KEY (survey_id, option_key),
  FOREIGN KEY (survey_id) REFERENCES surveys (id)
);

CREATE TABLE survey_option_values(
  id SERIAL PRIMARY KEY,
  survey_id int not null,
  option_key varchar(80) not null,
  option_label varchar(80) not null,
  option_value varchar(80) not null,
  option_description varchar(255),
  FOREIGN KEY (survey_id, option_key) REFERENCES survey_option_groups (survey_id, option_key)
);


CREATE TABLE screen_types(
	screen_type_key varchar(80) PRIMARY KEY,
	screen_type_description varchar(255)
);

CREATE TABLE survey_screen_types(
	screen_type_key varchar(80) not null,
	survey_id int not null,
	PRIMARY KEY (screen_type_key, survey_id),
	FOREIGN KEY (survey_id) REFERENCES surveys (id),
	FOREIGN KEY (screen_type_key) REFERENCES screen_types (screen_type_key)
);

CREATE TABLE questions(
  survey_id int not null,
  question_key varchar(80) not null,
  question_text varchar(500),
  question_image varchar(255),
  question_type varchar(80) not null,
  question_required boolean,
  screen_type_key varchar(80),
  option_key varchar(80),
  PRIMARY KEY (survey_id, question_key),
  FOREIGN KEY (survey_id) REFERENCES surveys (id),
  FOREIGN KEY (survey_id, option_key) REFERENCES survey_option_groups (survey_id, option_key),
  FOREIGN KEY (screen_type_key) REFERENCES screen_types (screen_type_key)
);

CREATE TABLE question_conditions(
  response varchar(80) not null,
  survey_id int not null,
  question_key varchar(80) not null,
  jump_to_key varchar(80) not null,
  PRIMARY KEY (response, survey_id, question_key),
  FOREIGN KEY (survey_id, question_key) REFERENCES questions (survey_id, question_key),
  FOREIGN KEY (survey_id, jump_to_key) REFERENCES questions (survey_id, question_key)
);


CREATE TABLE survey_responses(
  survey_id int not null,
  question_key varchar(80) not null,
  skipped boolean not null,
  PRIMARY KEY (survey_id, question_key),
  FOREIGN KEY (survey_id, question_key) REFERENCES questions (survey_id, question_key)
);

CREATE TABLE survey_item_responses(
  id SERIAL PRIMARY KEY,
  survey_id int not null,
  question_key varchar(80) not null,
  survey_item_response_value varchar(255) not null,
  FOREIGN KEY (survey_id, question_key) REFERENCES survey_responses (survey_id, question_key)
);
