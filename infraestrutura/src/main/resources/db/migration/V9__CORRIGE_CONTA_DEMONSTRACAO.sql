update CONTA_USUARIO
set SENHA = 'pbkdf2$310000$v7vHq+xkuwtt5owpiq17cA==$dYjxShNIb44fUCg4FaFAoiXyJgyc0507O237vw22xGc=',
    TIPO = 'ORGANIZADOR'
where ID = 1
  and EMAIL = 'usuario@torneios.com';
