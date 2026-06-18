update CONTA_USUARIO
set SENHA = 'pbkdf2$310000$h+C7fO5SjVMRAcitZxWUtQ==$28gmrOFlVz7gtxOuwGDzQ5EoRrLbGr4qFkYNpMFiI7U='
where ID = 1
  and EMAIL = 'usuario@torneios.com'
  and SENHA = '123456';
