
CREATE TABLE IF NOT EXISTS timesnippet (
  id VARCHAR(250) PRIMARY KEY,
  begin_timestamp timestamp(0), 
  end_timestamp timestamp(0)
);

CREATE TABLE IF NOT EXISTS businessday (
  id VARCHAR(250) PRIMARY KEY,
  current_businessday_increment_id VARCHAR(250)
);

CREATE TABLE IF NOT EXISTS businessday_increment (
  id VARCHAR(250) PRIMARY KEY,
  businessday_id VARCHAR(250),
  CONSTRAINT FK_BD_INCREMENT_BUSINESS_DAY FOREIGN KEY (businessday_id) REFERENCES businessday(id),
  current_timesnippet_id VARCHAR(250),
  CONSTRAINT FK_BD_INCREMENT_CURRENT_TIMESNIPPET FOREIGN KEY (current_timesnippet_id) REFERENCES timesnippet(id),
  description VARCHAR(250),
  ticket_nr VARCHAR(250),
  charge_type INT,
  is_charged BOOLEAN DEFAULT FALSE
);

ALTER TABLE businessday
    ADD CONSTRAINT IF NOT EXISTS FK_CURRENT_BUSINESS_DAY_INCREMENT FOREIGN KEY (current_businessday_increment_id) 
    REFERENCES businessday_increment(id);
