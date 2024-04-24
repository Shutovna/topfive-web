CREATE OR REPLACE FUNCTION check_5_items() RETURNS trigger AS $check_5_items$
declare	ti_cnt INTEGER;
BEGIN
    -- Check that top has max 5 items
    SELECT COUNT(ti.item_id)
    FROM topfive.top_items ti
    WHERE ti.top_id = NEW.top_id
    INTO	ti_cnt;

    IF ti_cnt >= 5 THEN
        RAISE EXCEPTION 'Top % cannot have more than 5 items', NEW.top_id;
    END IF;

    RETURN NEW;
END;
$check_5_items$ LANGUAGE plpgsql;



CREATE OR REPLACE TRIGGER check_5_items BEFORE INSERT OR UPDATE ON topfive.top_items
    FOR EACH ROW EXECUTE FUNCTION check_5_items();