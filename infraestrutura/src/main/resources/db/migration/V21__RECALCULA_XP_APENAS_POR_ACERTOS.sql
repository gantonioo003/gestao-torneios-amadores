UPDATE PROGRESSO_PALPITE progresso
SET progresso.PONTOS = (
        SELECT COUNT(*) * 25
        FROM PALPITE palpite
        WHERE palpite.usuario_Id = progresso.USUARIO_ID
          AND palpite.apurado = 1
          AND palpite.acertou = 1
    ),
    progresso.TOTAL_ACERTOS = (
        SELECT COUNT(*)
        FROM PALPITE palpite
        WHERE palpite.usuario_Id = progresso.USUARIO_ID
          AND palpite.apurado = 1
          AND palpite.acertou = 1
    ),
    progresso.TOTAL_PALPITES = (
        SELECT COUNT(*)
        FROM PALPITE palpite
        WHERE palpite.usuario_Id = progresso.USUARIO_ID
    );
