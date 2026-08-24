CREATE TABLE IF NOT EXISTS users (
    id VARCHAR(36) PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP,
    last_login TIMESTAMP
);

CREATE TABLE IF NOT EXISTS books (
    id VARCHAR(36) PRIMARY KEY,
    isbn VARCHAR(13) UNIQUE,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(200) NOT NULL,
    copies_left INTEGER DEFAULT 1
);

CREATE TABLE IF NOT EXISTS loans (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL REFERENCES users (id),
    book_id VARCHAR(36) NOT NULL REFERENCES books (id),
    loan_date DATE,
    due_date DATE,
    return_date DATE,
    status VARCHAR(20),
    fine_amount DOUBLE PRECISION
);

CREATE TABLE IF NOT EXISTS courses (
    course_id VARCHAR(36) PRIMARY KEY,
    course_name VARCHAR(150) NOT NULL,
    course_code VARCHAR(20) UNIQUE NOT NULL
);


CREATE TABLE IF NOT EXISTS modules (
    module_id VARCHAR(36) PRIMARY KEY,
    module_name VARCHAR(150) NOT NULL,
    module_code VARCHAR(20) NOT NULL UNIQUE
);


CREATE TABLE IF NOT EXISTS course_modules (
    course_id VARCHAR NOT NULL REFERENCES courses (course_id),
    module_id VARCHAR NOT NULL REFERENCES modules (module_id),
    PRIMARY KEY (course_id, module_id)
);


CREATE TABLE IF NOT EXISTS students (
    student_id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR NOT NULL REFERENCES users (id),
    student_number VARCHAR(20) UNIQUE NOT NULL,
    course_id VARCHAR NOT NULL REFERENCES courses (course_id),
    study_year INT NOT NULL DEFAULT 1
);

CREATE TABLE IF NOT EXISTS staff (
    staff_id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR NOT NULL REFERENCES users (id),
    role VARCHAR(32) DEFAULT 'DECISION_MAKER' CHECK (
        role IN ('DECISION_MAKER', 'ADMIN')
    )
);
