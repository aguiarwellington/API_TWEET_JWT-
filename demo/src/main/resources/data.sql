DELETE FROM tb_user_roles;
DELETE FROM tb_roles;

INSERT INTO tb_roles (role_id, name) VALUES (1, 'ADMIN');
INSERT INTO tb_roles (role_id, name) VALUES (2, 'BASIC');

INSERT INTO tb_user_roles (user_id, role_id)
SELECT u.user_id, r.role_id
FROM tb_user u
JOIN tb_roles r ON r.name = 'ADMIN'
WHERE u.username = 'admin';