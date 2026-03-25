DELETE FROM tb_user_roles;

-- depois roles
DELETE FROM tb_roles;

-- insere roles
INSERT INTO tb_roles (role_id, name) VALUES (1, 'ADMIN');
INSERT INTO tb_roles (role_id, name) VALUES (2, 'BASIC');