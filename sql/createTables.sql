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

CREATE TABLE instruments (
	instrument_id SERIAL PRIMARY KEY,
	user_id int4 NOT NULL REFERENCES users(id) ON DELETE CASCADE,
	name varchar(256) NOT NULL,
	description varchar(2000) NOT NULL,
	privacy varchar(7) NOT NULL,
	C4 BYTEA,
	Db4 BYTEA,
	D4 BYTEA,
	Eb4 BYTEA,
	E4 BYTEA,
	F4 BYTEA,
	Gb4 BYTEA,
	G4 BYTEA,
	Ab4 BYTEA,
	A4 BYTEA,
	Bb4 BYTEA,
	B4 BYTEA,
	C5 BYTEA,
	Db5 BYTEA,
	D5 BYTEA,
	Eb5 BYTEA,
	E5 BYTEA,
	F5 BYTEA,
	Gb5 BYTEA,
	G5 BYTEA,
	Ab5 BYTEA,
	A5 BYTEA,
	Bb5 BYTEA,
	B5 BYTEA,
	C6 BYTEA
);

