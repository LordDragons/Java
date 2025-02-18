
-- Insertion d'un enregistrement dans la table Customer
INSERT INTO Customer (name, email, mobile_number, create_dt)
VALUES ('Jean Dupont', 'jean.dupont@example.com', '0612345678', CURRENT_DATE);


-- Insertion d'un enregistrement dans la table Accounts
INSERT INTO Accounts (account_number, customer_id, account_type, branch_address, create_dt)
VALUES (100200300, 1, 'Checking', '10 rue des banques, Paris', CURRENT_DATE);
