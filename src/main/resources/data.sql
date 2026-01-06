-- -- This is for the Users table

-- INSERT INTO users (username, password, enabled) VALUES
-- ('nifemi.akeju@example.com', '$2a$10$3z/Ph/bipD0u.YBtuQvpJu/NJmX5hBCbee8hNjH230PcctoQkUbiK', true),
-- ('jane.doe@example.com', '$2a$10$3z/Ph/bipD0u.YBtuQvpJu/NJmX5hBCbee8hNjH230PcctoQkUbiK', true);

-- -- This is Candidate Profiles


-- INSERT INTO candidate_profiles (
--     first_name,
--     last_name,
--     email,
--     preferred_role,
--     country_phone_code,
--     phone_number,
--     date_of_birth
-- ) VALUES
-- (
--     'Oluwanifemi',
--     'Akeju',
--     'nifemi.akeju@example.com',
--     'Backend Software Engineer',
--     '+1',
--     '6393821923',
--     '2004-12-04'
-- ),
-- (
--     'Jane',
--     'Doe',
--     'jane.doe@example.com',
--     'Frontend Developer',
--     '+44',
--     '7123456789',
--     '1999-06-15'
-- );

-- -- Job applications

-- INSERT INTO job_applications (
--     company_name,
--     job_title,
--     job_description,
--     status,
--     ai_cover_letter,
--     candidate_id
-- ) VALUES
-- (
--     'Stripe',
--     'Backend Engineer',
--     'Responsible for building scalable payment APIs and backend services.',
--     'APPLIED',
--     'I am excited to apply my backend and fintech experience at Stripe.',
--     1
-- ),
-- (
--     'Shopify',
--     'Frontend Developer',
--     'Work on user-facing commerce features using modern JavaScript frameworks.',
--     'INTERVIEW',
--     'I enjoy building clean, scalable frontend experiences.',
--     2
-- );


-- -- Resumes

-- INSERT INTO candidate_resumes (
--     title,
--     resume_summary,
--     skills,
--     file_path,
--     candidate_id
-- ) VALUES
-- (
--     'Backend Software Engineer',
--     'Backend-focused engineer with experience in Spring Boot, PostgreSQL, and distributed systems.',
--     'Java, Spring Boot, PostgreSQL, REST APIs, Docker, Git',
--     '/resumes/nifemi_backend.pdf',
--     1
-- ),
-- (
--     'Frontend Developer',
--     'Frontend developer experienced in building responsive and accessible web interfaces.',
--     'JavaScript, React, TypeScript, HTML, CSS, Git',
--     '/resumes/jane_frontend.pdf',
--     2
-- );

SELECT 1;

