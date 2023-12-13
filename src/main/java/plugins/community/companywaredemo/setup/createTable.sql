CREATE TABLE Demo (id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY(Start with 1, Increment by 1), 
				   salutation VARCHAR(255),
				   name VARCHAR(255),
				   firstname VARCHAR(255),
				   street VARCHAR(255),
				   streetnumber VARCHAR(255),
				   zipcode VARCHAR(255),
				   city VARCHAR(255),
				   country VARCHAR(255)
);