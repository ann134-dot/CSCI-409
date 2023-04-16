-- insert into experiment(experiment_id, between_word_time, description, experiment_name, is_joinable, number_of_words, word_time, words, creator_id)
--     values (1, 1.5, 'description of EXP3', 'EXP3', true, 4, 1, ARRAY['scared','stick','Democrat','seize'], null);
-- insert into experiment(experiment_id, between_word_time, description, experiment_name, is_joinable, number_of_words, word_time, words, creator_id)
-- values ( 2, .5, 'description of EXP3', 'EXP3', true, 4, 1, ARRAY['scared','stick','Democrat','seize'], 2);


INSERT INTO user_table (user_id, age, degree, first_name, gender, last_name, password, role, user_email)
VALUES (100, 22, 'UG', 'Test', 'm', 'Testov', '$2a$10$ZQvMVM3hKXNnvDZZF8rdB.IufxATIvIar1K4oBOHVq30.k490rfKu', 'ADMIN', 'test@gmail.com')
ON conflict do NOTHING ;
INSERT INTO user_table (user_id, age, degree, first_name, gender, last_name, password, role, user_email)
VALUES (101, 22, 'UG', 'User', 'm', 'Testov', '$2a$10$EFWS/QOUi6yaq5r1vzbMcuisTbstOb07yvfDlZz1QjLDU/1h2iYhu', 'USER', 'user@gmail.com')
on conflict do NOTHING ;
INSERT INTO user_table (user_id, age, degree, first_name, gender, last_name, password, role, user_email)
VALUES (103, 21, 'UG', 'Aiganym', 'f', 'Testov', '$2a$10$EFWS/QOUi6yaq5r1vzbMcuisTbstOb07yvfDlZz1QjLDU/1h2iYhu', 'USER', 'aiganym@gmail.com')
on conflict do NOTHING ;

--
--
-- INSERT INTO experiment (experiment_id, between_word_time, description, experiment_name, frequency_range, is_joinable, length_of_words, number_of_words, overall_results, participant_count, word_time, words, creator_id) VALUES (100, 0.5, 'description of EXP2', 'EXP2', NULL, true, NULL, 4, '{0,0,0,0}', 0, 1, '{promise,reflection,worth,correct}', 100) on conflict do NOTHING;
-- INSERT INTO experiment (experiment_id, between_word_time, description, experiment_name, frequency_range, is_joinable, length_of_words, number_of_words, overall_results, participant_count, word_time, words, creator_id) VALUES (101, 0.5, 'description of EXP2', 'EXP2', NULL, true, NULL, 4, '{0,0,0,0}', 0, 1, '{evil,assistance,primary,sheet}', 100) on conflict do NOTHING;
-- INSERT INTO experiment (experiment_id, between_word_time, description, experiment_name, frequency_range, is_joinable, length_of_words, number_of_words, overall_results, participant_count, word_time, words, creator_id) VALUES (103, 0.5, 'description of EXP2', 'EXP2', NULL, true, NULL, 4, '{0,0,0,0}', 0, 1, '{print,boom,cook,timber}', 100) on conflict do NOTHING;
-- INSERT INTO experiment (experiment_id, between_word_time, description, experiment_name, frequency_range, is_joinable, length_of_words, number_of_words, overall_results, participant_count, word_time, words, creator_id) VALUES (104, 0.5, 'description of EXP2', 'EXP2', NULL, true, NULL, 4, '{0,0,0,0}', 0, 1, '{project,surgery,arm,sign}', 101) on conflict do NOTHING;
-- INSERT INTO experiment (experiment_id, between_word_time, description, experiment_name, frequency_range, is_joinable, length_of_words, number_of_words, overall_results, participant_count, word_time, words, creator_id) VALUES (105, 0.5, 'description of EXP2', 'EXP2', NULL, true, NULL, 4, '{0,0,0,0}', 0, 1, '{tendency,depression,direction,relation}', 101) on conflict do NOTHING;
-- INSERT INTO experiment (experiment_id, between_word_time, description, experiment_name, frequency_range, is_joinable, length_of_words, number_of_words, overall_results, participant_count, word_time, words, creator_id) VALUES (106, 0.5, 'description of EXP2', 'EXP2', NULL, true, NULL, 4, '{0,0,0,0}', 0, 1, '{professional,prisoner,unhappy,summer}', 101) on conflict do NOTHING;
--
--
--
--
-- INSERT INTO participation (participation_id, participant_results, status, experiment_id, participant_id) VALUES (100, '{0,0,0,0}', 0, 104, 100)  on conflict do NOTHING ;
-- INSERT INTO participation (participation_id, participant_results, status, experiment_id, participant_id) VALUES (101, '{0,0,0,0}', 1, 106, 100) on conflict do NOTHING ;
-- INSERT INTO participation (participation_id, participant_results, status, experiment_id, participant_id) VALUES (102, '{0,0,0,0}', 1, 101, 101)  on conflict do NOTHING;
-- -- INSERT INTO participation (participation_id, participant_results, status, experiment_id, participant_id) VALUES (104, '{0,0,0,0}', 1, 104, 100)  on conflict do NOTHING;
--
