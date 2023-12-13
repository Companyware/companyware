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

INSERT INTO Demo (salutation, name, firstname, street, streetnumber, zipcode, city, country)    
  (SELECT 'Herr' as salutation, 'Mustermann' as name, 'Hans' as firstname, 'Musterstraße' as street, '1' as streetnumber, '11111' as zipcode, 'Musterhausen' as city, 'Musterland' as country
   FROM Demo
   WHERE name = 'Mustermann'
   HAVING count(*)=0  
);