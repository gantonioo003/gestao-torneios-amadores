UPDATE PROGRESSO_PALPITE progresso
SET progresso.PONTOS = (
        SELECT COALESCE(SUM(
            CASE palpite.tipo
                WHEN 'VENCEDOR_PARTIDA' THEN 25
                WHEN 'ARTILHEIRO_TORNEIO' THEN 75
                WHEN 'LIDER_ASSISTENCIAS_TORNEIO' THEN 75
                WHEN 'CAMPEAO_TORNEIO' THEN 100
                ELSE 0
            END
        ), 0)
        FROM PALPITE palpite
        WHERE palpite.usuario_Id = progresso.USUARIO_ID
          AND palpite.apurado = 1
          AND palpite.acertou = 1
    );
