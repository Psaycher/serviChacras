CREATE DATABASE servichacras;
USE servichacras;

SELECT * FROM usuario;
SELECT * FROM servicio;

UPDATE usuario
SET rol = 'ADMIN'
WHERE id = 'e5578d16-fa2a-4d4b-b611-08a0dd61f8be';

UPDATE proveedor
SET cantidad_pedidos = 0
WHERE cantidad_pedidos IS NULL;

DELETE FROM usuario WHERE email = 'erroneo1@mail.com';

SELECT * FROM cliente;

SELECT * FROM cliente
JOIN usuario ON cliente.id_usuario = usuario.id
WHERE usuario.apellido LIKE '%Palma';

INSERT INTO cliente (id, id_usuario) VALUES (UUID(), '786ff504-9d6a-46fc-97bb-6d8c274dfbdf');

SELECT * FROM proveedor;

DELETE proveedor
FROM proveedor
WHERE proveedor.id_usuario = '76dfafdf-2a0b-4918-99c6-5884a4a7033c';

DELETE usuario
FROM usuario
WHERE usuario.email = 'proveedor2@mail.com';

SELECT * FROM notificacion;
SELECT * FROM pedido;
SELECT * FROM pago;

DELETE notificacion FROM notificacion WHERE visto LIKE '%PENDIENTE%';

SELECT * FROM notificacion WHERE id_destinatario = '1c56ca20-a25f-447b-9359-df754efcde5f';