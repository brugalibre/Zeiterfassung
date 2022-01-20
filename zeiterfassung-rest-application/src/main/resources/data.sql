CREATE TABLE IF NOT EXISTS timesnippet (
  id VARCHAR(250) PRIMARY KEY,
  begin_timestamp timestamp(0), 
  end_timestamp timestamp(0)
);

CREATE TABLE IF NOT EXISTS businessday (
  id VARCHAR(250) PRIMARY KEY,
  comeandgoes_id VARCHAR(250),
  is_booked BOOLEAN DEFAULT FALSE,
  current_timesnippet_id VARCHAR(250)
);

CREATE TABLE IF NOT EXISTS businessday_increment (
  id VARCHAR(250) PRIMARY KEY,
  businessday_id VARCHAR(250),
  CONSTRAINT FK_BD_INCREMENT_BUSINESS_DAY FOREIGN KEY (businessday_id) REFERENCES businessday(id),
  current_timesnippet_id VARCHAR(250),
  CONSTRAINT FK_BD_INCREMENT_CURRENT_TIMESNIPPET FOREIGN KEY (current_timesnippet_id) REFERENCES timesnippet(id),
  description VARCHAR(250),
  ticket_nr VARCHAR(250),
  service_code INT,
  is_booked BOOLEAN DEFAULT FALSE,
  is_sent BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS comeandgoes (
  id VARCHAR(250) PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS comeandgo (
  id VARCHAR(250) PRIMARY KEY,
  comeandgoes_id VARCHAR(250),
  CONSTRAINT FK_COME_AND_GOES FOREIGN KEY (comeandgoes_id) REFERENCES comeandgoes(id),
  timesnippet_id VARCHAR(250),
  CONSTRAINT FK_COME_AND_GO_TIMESNIPPET FOREIGN KEY (timesnippet_id) REFERENCES timesnippet(id),
  is_recorded BOOLEAN DEFAULT FALSE
);

ALTER TABLE businessday
    ADD CONSTRAINT IF NOT EXISTS FK_CURRENT_TIMESNIPPET FOREIGN KEY (current_timesnippet_id) 
    REFERENCES timesnippet(id);
ALTER TABLE businessday
    ADD CONSTRAINT IF NOT EXISTS FK_COME_AND_GOES FOREIGN KEY (comeandgoes_id) 
    REFERENCES businessday_increment(id);

ALTER TABLE businessday_increment
    ADD COLUMN IF NOT EXISTS is_sent BOOLEAN DEFAULT FALSE;