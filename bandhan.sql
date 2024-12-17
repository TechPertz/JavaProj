
CREATE TABLE IF NOT EXISTS boards (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        date_created DATETIME NOT NULL,
                            matrix_data text NOT NULL,
                        owner_id bigint
);

CREATE TABLE IF NOT EXISTS users (
                       id INTEGER PRIMARY KEY AUTOINCREMENT,
                       username character varying(255) NOT NULL,
                       date_created DATETIME NOT NULL
);
