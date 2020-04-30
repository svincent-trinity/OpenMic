CREATE TABLE users (
	id SERIAL PRIMARY KEY,
	username varchar(20) NOT NULL,
	password varchar(200) NOT NULL
);

CREATE TABLE items (
	item_id SERIAL PRIMARY KEY,
	user_id int4 NOT NULL REFERENCES users(id) ON DELETE CASCADE,
	text varchar(2000) NOT NULL,
	privacy varchar(7) NOT NULL,
	midiData varchar(10000)
);


CREATE TABLE recordings (
	recording_id SERIAL PRIMARY KEY,
	user_id int4 NOT NULL REFERENCES users(id) ON DELETE CASCADE,
	name varchar(256) NOT NULL,
	description varchar(2000) NOT NULL,
	privacy varchar(7) NOT NULL,
	audio BYTEA NOT NULL
);

