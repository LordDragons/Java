-- Création de la table Customer
CREATE TABLE Customer (
                          customer_id INT AUTO_INCREMENT PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          email VARCHAR(255) NOT NULL,
                          mobile_number VARCHAR(20),
                          create_dt DATE
);

-- Création de la table Accounts
CREATE TABLE Accounts (
                          account_number BIGINT PRIMARY KEY,
                          customer_id INT NOT NULL,
                          account_type VARCHAR(50),
                          branch_address VARCHAR(255),
                          create_dt DATE,
                          FOREIGN KEY (customer_id) REFERENCES Customer(customer_id)
);
