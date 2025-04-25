DO $$
DECLARE
    v_sender_account_id BIGINT;
    v_receiver_account_id BIGINT;
    v_sender_bank_id BIGINT;
    v_receiver_bank_id BIGINT;
    v_category_id BIGINT;
    v_person_type VARCHAR(255);
    v_transaction_type VARCHAR(255);
    v_comment TEXT;
    v_amount NUMERIC(15, 2);  -- Изменяем DECIMAL на NUMERIC для лучшей практики
    v_status VARCHAR(255);
    v_operation_datetime TIMESTAMP;
    v_random_status_index INT;  -- Объявляем переменную
BEGIN
    FOR i IN 1..100000 LOOP
        -- Получаем случайные значения для полей
        v_sender_account_id := (SELECT id FROM account ORDER BY RANDOM() LIMIT 1);
        v_receiver_account_id := (SELECT id FROM account ORDER BY RANDOM() LIMIT 1);
        v_sender_bank_id := (SELECT id FROM bank ORDER BY RANDOM() LIMIT 1);
        v_receiver_bank_id := (SELECT id FROM bank ORDER BY RANDOM() LIMIT 1);
        v_category_id := (SELECT id FROM category ORDER BY RANDOM() LIMIT 1);

        v_person_type := CASE WHEN RANDOM() < 0.5 THEN 'PHYSICAL' ELSE 'LEGAL' END;
        v_transaction_type := CASE WHEN RANDOM() < 0.5 THEN 'CREDIT' ELSE 'DEBIT' END;

        -- Приведение типа к NUMERIC
        v_amount := ROUND((RANDOM() * 1000)::numeric, 2);  -- Явное приведение к типу numeric

        -- Генерация случайного индекса для статуса
        v_random_status_index := FLOOR(RANDOM() * 7);

        -- Генерация статуса основаваясь на случайном индексе
        v_status := CASE v_random_status_index
            WHEN 0 THEN 'NEW'
            WHEN 1 THEN 'CONFIRMED'
            WHEN 2 THEN 'IN_PROCESS'
            WHEN 3 THEN 'CANCELED'
            WHEN 4 THEN 'COMPLETED'
            WHEN 5 THEN 'DELETED'
            WHEN 6 THEN 'RETURNED'
        END;

        -- Генерация случайной даты в прошлом году
        v_operation_datetime := DATE_TRUNC('day', NOW() - INTERVAL '1 year' * RANDOM()) + INTERVAL '12 hours';

        -- Вставка транзакции
        INSERT INTO transaction
        (person_type, operation_datetime, transaction_type, comment, amount, status, sender_bank_id, sender_account_id, receiver_bank_id, receiver_account_id, receiver_inn, category_id, receiver_phone)
        VALUES
        (v_person_type, v_operation_datetime, v_transaction_type, v_comment, v_amount, v_status, v_sender_bank_id, v_sender_account_id, v_receiver_bank_id, v_receiver_account_id, '123456789', v_category_id, '1234567890');
    END LOOP;
END $$;