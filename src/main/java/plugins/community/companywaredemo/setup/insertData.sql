INSERT INTO Demo (salutation, name, firstname, street, streetnumber, zipcode, city, country)    
  (SELECT 'Herr' as salutation, 'Mustermann' as name, 'Hans' as firstname, 'Musterstraße' as street, '1' as streetnumber, '11111' as zipcode, 'Musterhausen' as city, 'Musterland' as country
   FROM Demo
   WHERE name = 'Mustermann'
   HAVING count(*)=0  
);