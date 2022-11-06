DROP VIEW IF EXISTS vw_transfer_account_users;
CREATE VIEW vw_transfer_account_users
AS
SELECT
 	t.transfer_id,
	t.transfer_type_id,
	tt.transfer_type_desc,
	t.transfer_status_id,
	ts.transfer_status_desc,
	t.account_from,
	t.account_to,
	t.amount,
	a.user_id,
	(SELECT a.user_id FROM account a JOIN transfer tr ON tr.account_to = a.account_id WHERE tr.account_to = t.account_to GROUP BY a.user_id) AS to_userid, tu.username AS username,
	(SELECT tu.username	FROM tenmo_user tu JOIN account a ON tu.user_id = a.user_id WHERE a.user_id =
	 	(SELECT a.user_id FROM account a JOIN transfer tr ON tr.account_to = a.account_id WHERE tr.account_to = t.account_to GROUP BY a.user_id) GROUP BY a.user_id, tu.username) AS to_username
	
FROM transfer t
	JOIN transfer_status ts ON ts.transfer_status_id = t.transfer_status_id
	JOIN transfer_type tt ON tt.transfer_type_id = t.transfer_type_id
	LEFT OUTER JOIN account a ON a.account_id = t.account_from
	LEFT OUTER JOIN tenmo_user tu ON tu.user_id = a.user_id
ORDER BY t.transfer_id;

SELECT * FROM vw_transfer_account_users ORDER BY transfer_id;