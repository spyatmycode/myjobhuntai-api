
-- DROP TABLE IF EXISTS candidate_resumes;
-- DROP TABLE IF EXISTS job_applications;
-- DROP TABLE IF EXISTS candidate_profiles;
-- DROP TABLE IF EXISTS authorities;
-- DROP TABLE IF EXISTS users;



-- 1. Standard Spring Security Tables (The "Bouncer")
CREATE TABLE IF NOT EXISTS users (
    username VARCHAR(50) NOT NULL PRIMARY KEY, -- This will store the Email
    password VARCHAR(500) NOT NULL,
    enabled BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS authorities (
    username VARCHAR(50) NOT NULL,
    authority VARCHAR(50) NOT NULL,
    CONSTRAINT fk_authorities_users FOREIGN KEY(username) REFERENCES users(username)
);
CREATE UNIQUE INDEX IF NOT EXISTS ix_auth_username ON authorities (username, authority);


-- 2. Your Business Tables (The "Recruiter")

CREATE TABLE IF NOT EXISTS candidate_profiles (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    
    -- THE NEW LINK: Connects this profile to the login user
    email VARCHAR(50) NOT NULL UNIQUE,
    CONSTRAINT fk_profiles_users FOREIGN KEY(email) REFERENCES users(username) ON DELETE CASCADE,

    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    preferred_role VARCHAR(50) NOT NULL,
    country_phone_code VARCHAR(10),
    phone_number VARCHAR(50),
    date_of_birth DATE,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS job_applications (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    company_name VARCHAR(100) NOT NULL,
    job_title VARCHAR(100) NOT NULL,
    job_description TEXT NOT NULL,
    status VARCHAR(100) NOT NULL,
    ai_cover_letter TEXT,
    extra_notes TEXT DEFAULT NULL
    
    candidate_id BIGINT NOT NULL,
    -- Explicit Foreign Key
    CONSTRAINT fk_job_app_candidate 
        FOREIGN KEY (candidate_id) 
        REFERENCES candidate_profiles(id) 
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS candidate_resumes (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    resume_summary TEXT NOT NULL,
    skills TEXT NOT NULL,
    file_path TEXT,
    
    candidate_id BIGINT NOT NULL,
    -- Explicit Foreign Key
    CONSTRAINT fk_resume_candidate 
        FOREIGN KEY (candidate_id) 
        REFERENCES candidate_profiles(id) 
        ON DELETE CASCADE
);